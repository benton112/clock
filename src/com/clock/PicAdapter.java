package com.clock;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class PicAdapter extends ArrayAdapter<Pic> {

    private Pic[] items;
    private Context context;

    public PicAdapter(Context context, int textViewResourceId, Pic[] items) {
            super(context, textViewResourceId, items);
            this.items = items;
            this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
          
    	View v = convertView;
            
            
            if (v == null) {
            	 
                    LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.editpicrow, null);
            }

            Pic item = items[position];
            if (item != null) {
                    ImageView iv = (ImageView) v.findViewById(R.id.picView);
                    if (iv != null) {
                            iv.setImageResource(item.getPicId());
                    }
                    TextView tv = (TextView) v.findViewById(R.id.picTextView);
                    if (tv != null) {
                            tv.setText(item.getPicText());
                    }
            }

            return v;
    
    }
}
