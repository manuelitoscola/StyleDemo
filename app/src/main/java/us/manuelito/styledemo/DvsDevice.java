package us.manuelito.styledemo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by scola on 3/11/16.
 */
public class DvsDevice implements AiVuDevice {

    private static final String TAG = "AiVu-Device";

    final private String MANUFACTURER = "Aitek SpA";
    final private String BRAND = "Device C";

    private Handler handler;
    private MyArrayAdapter dvsAdapter;

    private boolean logged;
    private boolean logging;
    private String address;
    private String username;
    private String password;
    private String name;
    private ProgressBar progressBar;
    private ImageView imageView;

    public DvsDevice(Handler handler, MyArrayAdapter dvsAdapter, String address, String username, String password) {
        this.logged = false;
        this.logging = false;
        this.address = address;
        this.username = username;
        this.password = password;

        this.handler = handler;
        this.dvsAdapter = dvsAdapter;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public class NullHostNameVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            Log.i("RestUtilImpl", "Approving certificate for " + hostname);
            return true;
        }
    }

    class TheTaskJsonLogin extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... arg0) {
            String text = null;
            String urlPath = "https://" + address + "/api/aivu_cfg/v1/create_token";
            /*
            StringBuilder stringBuilder = new StringBuilder();
            HttpClient httpClient = new DefaultHttpClient();
            Log.v(TAG, "Http Post on URL: " +  urlPath);
            HttpPost httpPost = new HttpPost(urlPath);
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username", username));
            pairs.add(new BasicNameValuePair("password", password));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity ent = response.getEntity();
                text = EntityUtils.toString(ent);
            } catch (IOException e) {
                e.printStackTrace();
            }
            */
            try {
                URL url = new URL(urlPath);
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

                // Create a trust manager that does not validate certificate chains
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                            public void checkClientTrusted(
                                    java.security.cert.X509Certificate[] certs, String authType) {
                            }
                            public void checkServerTrusted(
                                    java.security.cert.X509Certificate[] certs, String authType) {
                            }
                        }
                };

                // Create the SSL connection
                SSLContext sc;
                sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                conn.setSSLSocketFactory(sc.getSocketFactory());
                conn.setDefaultHostnameVerifier(new NullHostNameVerifier());

                // Use this if you need SSL authentication
                //String userpass = user + ":" + password;
                //String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
                //conn.setRequestProperty("Authorization", basicAuth);

                // set Timeout and method
                conn.setReadTimeout(7000);
                conn.setConnectTimeout(7000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);

                // Add any data you wish to post here
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                out.write("username=" + username);
                out.write("password=" + password);
                out.close();

                conn.connect();
                conn.getInputStream();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                text = readIt(in, 2048);
                in.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            return text;
        }

        @Override
        protected void onPostExecute(String response) {
            // TODO Auto-generated method stub
            //super.onPostExecute(response);
            Log.v(TAG, "Http Post Response: " + response);
        }

    }

    public boolean isLogging() {
        return logging;
    }

    @Override
    public VsDevice[] getCameras() {
        return new VsDevice[0];
    }

    @Override
    public void login() {
        logging = true;
        new Thread(new Runnable() {
            public void run() {
                // Execute some code after 10 seconds have passed
                boolean delayed = handler.postDelayed(new Runnable() {
                    public void run() {
                        logging = false;
                        new TheTaskJsonLogin().execute();
                        if (address.equals("10.10.33.110"))
                            logged = true;
                        progressBar.post(new Runnable() {
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                imageView.setVisibility(View.VISIBLE);
                                dvsAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                }, 10000);
            }
        }).start();
    }

    @Override
    public void logout() {

    }

    @Override
    public boolean isLogged() {
        return logged;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getManufacturer() {
        return MANUFACTURER;
    }

    @Override
    public String getBrand() {
        return BRAND;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
