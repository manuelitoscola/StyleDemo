package us.manuelito.styledemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by scola on 3/10/16.
 */
public class MyArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final ArrayList<String> values;

  public MyArrayAdapter(Context context, ArrayList<String> values) {
    super(context, R.layout.row, R.id.label, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.row, parent, false);
    TextView textView = (TextView) rowView.findViewById(R.id.label);
    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
    textView.setText(values.get(position));
    // Change the icon for Windows and iPhone
    String s = values.get(position);
    if (s.startsWith("Windows7") || s.startsWith("iPhone")
        || s.startsWith("Solaris")) {
      imageView.setImageResource(R.drawable.lock);
    } else {
      imageView.setImageResource(R.drawable.lock);
    }

    return rowView;
  }
} 
