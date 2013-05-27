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


public class PicActivity extends ListActivity {
	
	private PicAdapter adapter;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpic);
        
        final ListView picListView = getListView();
        
        
        PicList picList = new PicList();
        
        
        adapter = new PicAdapter(this, 
                R.layout.editpicrow, picList.getData());
               
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
				
				extras.putInt(DBAdapter.KEY_PIC, adapter.getItem(position).getPicId());
				
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