package com.battery.saver.G.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import com.battery.saver.G.R;

import java.util.List;
import java.util.Map;

/**
 * Created by mkida on 4/08/2014.
 */
public class GeofencesAdapter extends SimpleAdapter {
    public GeofencesAdapter(Context context, List<? extends Map<String, String>> data,
                            int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.location_black);

        return view;
    }

}
