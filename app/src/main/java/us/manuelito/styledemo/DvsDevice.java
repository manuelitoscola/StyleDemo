package us.manuelito.styledemo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

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

    private boolean jsonLogin() {

        return false;
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
                        jsonLogin();
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
