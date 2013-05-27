package com.clock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class PicBackActivity extends ListActivity {
	
	private PicBackAdapter adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpic);
        
        final ListView picListView = getListView();
        
        
        PicBack data[] = new PicBack[]
                {
                    new PicBack(R.drawable.line_r,R.drawable.point_r,"one"),
                    new PicBack(R.drawable.line_r2,R.drawable.point_r2,"two")

                };
        
        
        adapter = new PicBackAdapter(this, 
                R.layout.editpicbackrow, data);
        
            
        picListView.setAdapter(adapter);
        
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		
	        //this.adapter.getItem(position).click(this.getApplicationContext());
		
		new AlertDialog.Builder(this)
		.setMessage(R.string.pic_selQuestion)
		//.setCancelable(false)
		.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Bundle extras = new Bundle();
				
				extras.putInt(DBAdapter.KEY_PIC_BACK, adapter.getItem(position).getBackID());
				extras.putInt(DBAdapter.KEY_PIC_TICK, adapter.getItem(position).getTickID());
				
				Intent data = new Intent();
				data.putExtras(extras);
		        
		        setResult(RESULT_OK,data);
		        finish();
				}      
			})       
			.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();           
					}       
			})
			.show();
		
		
	}
}