package us.manuelito.styledemo;

import android.app.ActionBar;
import android.app.Dialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    ListView dvsListView;
    ArrayList<String> dvsArrayList;
    MyArrayAdapter dvsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dvsListView = (ListView)findViewById(R.id.listView_dvs);
        dvsArrayList = new ArrayList<String>();
        dvsAdapter = new MyArrayAdapter(this, dvsArrayList);
        dvsListView.setAdapter(dvsAdapter);
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

                String dvsItem = editTextUsername.getText().toString() + "@"
                        + editTextIp.getText().toString();
                dvsArrayList.add(dvsItem);
                dvsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
