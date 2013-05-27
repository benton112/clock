package com.clock;

//test commit

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

import android.widget.TextView;



public class ClockActivity extends ListActivity implements OnClickListener {
    /** Called when the activity is first created. */
	
    private DBAdapter mDbHelper;
    private Cursor mNotesCursor;
    
    private static final int ADD_TIME_REZULT = 0;
    private static final int EDIT_TIME_REZULT = 1;
    
    
	public void onClick(View v) {
		switch (v.getId()){
		/*
			case R.id.btnAdd:
				Intent i = new Intent(this,EditActivity.class);
				startActivityForResult(i, ADD_TIME_REZULT);
				break;
*/
			default:
				Log.d("Clock","default");
		}

	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mDbHelper = new DBAdapter(this);
        mDbHelper.open();
        
        
        fillData();
        
        //final Button btnAdd = (Button) findViewById(R.id.btnAdd);
    	//btnAdd.setOnClickListener(this);
    	
    	registerForContextMenu(this.getListView());

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle(R.string.main_contextMenuHeader);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()){
    		case R.id.context_menu_edit: //edit
   				editListItem(info.position);
   				fillData();
   		        return true;
    		case R.id.context_menu_delete: //delete
    			
    			new AlertDialog.Builder(this)
    			.setMessage(R.string.main_delQuestion)
    			//.setCancelable(false)
    			.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
    				public void onClick(DialogInterface dialog, int id) {
    					mDbHelper.deleteNote(info.id);
    	   				fillData();	
    					}      
    				})       
    				.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
    					public void onClick(DialogInterface dialog, int id) {
    						dialog.cancel();           
    						}       
    				})
    				.show();
    			return true;                
    	}
		return true;
    	

    	
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_add:
				Intent i = new Intent(this,PrefActivity.class);
				startActivityForResult(i, ADD_TIME_REZULT);
                return true;
            case R.id.menu_edit:
            	return true;
            case R.id.menu_delete:
                //mDbHelper.deleteNote(info.id);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        //editListItem(position);
        
    }
    protected void editListItem(int position){
    	Cursor c = mNotesCursor;
        c.moveToPosition(position);
        
        //Intent intent = new Intent(this, EditActivity.class);
        Intent intent = new Intent(this, PrefActivity.class);
        intent.putExtra(DBAdapter.KEY_ROWID, c.getLong(
                c.getColumnIndexOrThrow(DBAdapter.KEY_ROWID)));
        intent.putExtra(DBAdapter.KEY_TEXT, c.getString(
                c.getColumnIndexOrThrow(DBAdapter.KEY_TEXT)));
        intent.putExtra(DBAdapter.KEY_DATE, c.getLong(
                c.getColumnIndexOrThrow(DBAdapter.KEY_DATE)));
        
        if (c.getShort(c.getColumnIndexOrThrow(DBAdapter.KEY_DATE_PAST)) == 0)
        	 	intent.putExtra(DBAdapter.KEY_DATE_PAST, false);
        else
        	 intent.putExtra(DBAdapter.KEY_DATE_PAST, true);
        
        intent.putExtra(DBAdapter.KEY_PIC, c.getInt(
                c.getColumnIndexOrThrow(DBAdapter.KEY_PIC)));
        intent.putExtra(DBAdapter.KEY_PIC_BACK, c.getInt(
                c.getColumnIndexOrThrow(DBAdapter.KEY_PIC_BACK)));
        intent.putExtra(DBAdapter.KEY_PIC_TICK, c.getInt(
                c.getColumnIndexOrThrow(DBAdapter.KEY_PIC_TICK)));
        
        startActivityForResult(intent, EDIT_TIME_REZULT);
        //startActivity(intent);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = new Bundle();
        if (intent != null)
        	extras = intent.getExtras();
        switch(requestCode) {
	        case ADD_TIME_REZULT:
	            switch(resultCode) {
		        	case RESULT_OK:
			            String text = extras.getString(DBAdapter.KEY_TEXT);
			            long date = extras.getLong(DBAdapter.KEY_DATE);
			            boolean date_past = extras.getBoolean(DBAdapter.KEY_DATE_PAST);
			            int picID = extras.getInt(DBAdapter.KEY_PIC);
			            int backID = extras.getInt(DBAdapter.KEY_PIC_BACK);
			            int tickID = extras.getInt(DBAdapter.KEY_PIC_TICK);
			            mDbHelper.createNote(text, date, date_past, picID, tickID, backID);
			            fillData();
			            break;
		        	case RESULT_CANCELED:
		        		break;
	            }
	            break;
	        case EDIT_TIME_REZULT:
	            switch(resultCode) {
		        	case RESULT_OK:
		                Long rowId = extras.getLong(DBAdapter.KEY_ROWID);
		                if (rowId != null) {
		                    String text = extras.getString(DBAdapter.KEY_TEXT);
		                    long date = extras.getLong(DBAdapter.KEY_DATE);
		                    boolean date_past = extras.getBoolean(DBAdapter.KEY_DATE_PAST);
				            int picID = extras.getInt(DBAdapter.KEY_PIC);
				            int backID = extras.getInt(DBAdapter.KEY_PIC_BACK);
				            int tickID = extras.getInt(DBAdapter.KEY_PIC_TICK);
				            
		                    mDbHelper.updateNote(rowId, text, date, date_past, picID, tickID, backID);
		                }
		                fillData();
	            }
        }
    }
    
    private void fillData() {
        // Get all of the rows from the database and create the item list
    	mNotesCursor = mDbHelper.fetchAllNotes();
    	
    	class  bindDateClass implements SimpleCursorAdapter.ViewBinder {
    		public boolean setViewValue(View v, Cursor c, int columnIndex ){

    			switch (v.getId()){
    				case R.id.clockViewer:
    					((ClockViewer)v).setData(c.getString(mNotesCursor.getColumnIndex("TEXT")),
    											 c.getLong(mNotesCursor.getColumnIndex("DATE")),
    											 c.getShort(mNotesCursor.getColumnIndex("DATE_PAST")),
    											 c.getInt(mNotesCursor.getColumnIndex("PIC_ID")),
    											 c.getInt(mNotesCursor.getColumnIndex("PIC_BACK_ID")),
    											 c.getInt(mNotesCursor.getColumnIndex("PIC_TICK_ID")));
    					return true;
    			}
    			return false;
        	}
    	};
    	
    	
    	
    	
    	String[] from  = new String[] {DBAdapter.KEY_TEXT, DBAdapter.KEY_DATE};
    	int[] to = new int[]{R.id.clockViewer};
    	
    	bindDateClass bindDate = new bindDateClass();
        SimpleCursorAdapter dateCursorAdapter = new SimpleCursorAdapter(this, R.layout.row, mNotesCursor, from, to);
        dateCursorAdapter.setViewBinder(bindDate);
        setListAdapter(dateCursorAdapter);

    }


   
}