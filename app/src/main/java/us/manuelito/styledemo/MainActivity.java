package us.manuelito.styledemo;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "AiVu-Device";

    ListView dvsListView;
    ArrayList<VsDevice> dvsArrayList;
    MyArrayAdapter dvsAdapter;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dvsListView = (ListView)findViewById(R.id.listView_dvs);
        dvsArrayList = new ArrayList<VsDevice>();
        dvsAdapter = new MyArrayAdapter(this, dvsArrayList);
        dvsListView.setAdapter(dvsAdapter);
        handler = new Handler();

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            int verCode = pInfo.versionCode;
            Log.v(TAG, "Version: " + version + "." + verCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addDvs(View view) {
        // custom dialog
        final Dialog dialog = new Dialog(view.getContext());
        dialog.setContentView(R.layout.add_dvs);
        dialog.setTitle(getResources().getString(R.string.title_add_dvs));

        final EditText editTextIp = (EditText)dialog.findViewById(R.id.editText_ip);
        final EditText editTextUsername = (EditText)dialog.findViewById(R.id.editText_username);
        final EditText editTextPasswowd = (EditText)dialog.findViewById(R.id.editText_password);
        Button dialogButton = (Button) dialog.findViewById(R.id.button_insert);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DvsDevice dvs = new DvsDevice(handler, dvsAdapter,
                                              editTextIp.getText().toString(),
                                              editTextUsername.getText().toString(),
                                              editTextPasswowd.getText().toString());
                dvs.login();
                dvsArrayList.add(dvs);
                dvsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
