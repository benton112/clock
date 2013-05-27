package com.clock;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class PicBackAdapter extends ArrayAdapter<PicBack> {

    private PicBack[] items;
    private Context context;

    public PicBackAdapter(Context context, int textViewResourceId, PicBack[] data) {
            super(context, textViewResourceId, data);
            this.items = data;
            this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
          
    	View v = convertView;
            
            
            if (v == null) {
            	 
                    LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.editpicbackrow, null);
            }

            PicBack item = items[position];
            if (item != null) {
                    ClockBackViewer cv = (ClockBackViewer) v.findViewById(R.id.backViewer);
                    if (cv != null) {
                            cv.setResource(item.getBackID(),item.getTickID());
                    }
                    TextView tv = (TextView) v.findViewById(R.id.backText);
                    if (tv != null)
                    	tv.setText(item.getBackText());
            }

            return v;
    
    }
}
