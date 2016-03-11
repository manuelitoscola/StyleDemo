package us.manuelito.styledemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by scola on 3/10/16.
 */
public class MyArrayAdapter extends ArrayAdapter<VsDevice> {

    private static final String TAG = "AiVu-Device";

    private final Context context;
    private final ArrayList<VsDevice> values;

    public MyArrayAdapter(Context context, ArrayList<VsDevice> values) {
        super(context, R.layout.row, R.id.label, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DvsDevice dvs = (DvsDevice)values.get(position);
        View rowView = inflater.inflate(R.layout.row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        ProgressBar progressBar = (ProgressBar) rowView.findViewById(R.id.progress);
        textView.setText(dvs.getUsername() + "@" + dvs.getAddress());

        dvs.setImageView(imageView);
        dvs.setProgressBar(progressBar);

        // Change the icon if logged or NOT logged in
        if (dvs.isLogging() == true) {
            rowView.findViewById(R.id.icon).setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.lock);
        } else {
            rowView.findViewById(R.id.progress).setVisibility(View.GONE);
            if (dvs.isLogged())
                imageView.setImageResource(R.drawable.login);
            else
                imageView.setImageResource(R.drawable.lock);
        }

        return rowView;
    }
} 
