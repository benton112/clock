package com.clock;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.widget.DatePicker;



public class PrefActivity extends PreferenceActivity implements DatePickerDialogExtended.OnDateSetListener, 
OnSharedPreferenceChangeListener{

	private int picID;
	private int backID;
	private int tickID;

	private SharedPreferences pref;
	private Long mRowId;

	private String _event;
	private int _year;
	private int _month;
	private int _day;
	private boolean _past;
	
	private static final int EDIT_PIC = 0;
	private static final int EDIT_BACK = 1;
	
	@Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    addPreferencesFromResource(R.xml.preference);
	    
		long date;

		mRowId = null;
		
		pref = getPreferences(MODE_PRIVATE);
	    
	    // Подстановка редактируемых полей
	 		Bundle extras = getIntent().getExtras();
	 		if (extras != null) {
	 			_event = extras.getString(DBAdapter.KEY_TEXT);
	 			date = extras.getLong(DBAdapter.KEY_DATE);
	 			_past = extras.getBoolean(DBAdapter.KEY_DATE_PAST);
	 			mRowId = extras.getLong(DBAdapter.KEY_ROWID);
	 			picID = extras.getInt(DBAdapter.KEY_PIC);
	 			backID = extras.getInt(DBAdapter.KEY_PIC_BACK);
	 			tickID = extras.getInt(DBAdapter.KEY_PIC_TICK);

	 		} else {
	 			// Подстановка ранее введенных полей

	 			_event = pref.getString(DBAdapter.KEY_TEXT, "");
	 			date = pref.getLong(DBAdapter.KEY_DATE, 0);
	 			_past = pref.getBoolean(DBAdapter.KEY_DATE_PAST, true);

	 			picID = pref.getInt(DBAdapter.KEY_PIC, R.drawable.fig_r);
	 			backID = pref.getInt(DBAdapter.KEY_PIC_BACK, R.drawable.line_r);
	 			tickID = pref.getInt(DBAdapter.KEY_PIC_TICK, R.drawable.point_r);

	 		}
	 		Date cal;
			if (date != 0)
				cal = new Date(date);
			else
				cal = new GregorianCalendar().getTime();

			_year = cal.getYear() + 1900;
			_month = cal.getMonth();
			_day = cal.getDate();
			
			EditTextPreference textPreference = (EditTextPreference) findPreference("event");
			textPreference.setSummary(_event);
			textPreference.setText(_event);
			
			Preference preference = findPreference("date");
			preference.setSummary("" + _day + "/" + (_month + 1) + "/" + _year);
			
			CheckBoxPreference chkBoxPreference = (CheckBoxPreference) findPreference("past");
			chkBoxPreference.setChecked(_past);
	
			PicList picList = new PicList();
			preference = findPreference("pic");
			preference.setSummary(picList.getName(picID));
			
			PicBackList picBackList = new PicBackList();
			preference = findPreference("back");
			preference.setSummary(picBackList.getName(backID,tickID));
			

	}
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener( this );
	}

	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener( this );
	}
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		
		Preference pref = findPreference(key);
	    if (pref instanceof EditTextPreference) {
	        EditTextPreference etp = (EditTextPreference) pref;
	        _event = etp.getText();
	        pref.setSummary(_event);
	    }
	    if (pref instanceof CheckBoxPreference) {
	    	CheckBoxPreference etp = (CheckBoxPreference) pref;
			PreferenceManager editor = etp.getPreferenceManager();
	        _past =  editor.getSharedPreferences().getBoolean("past",false);
	        Date cal = new GregorianCalendar().getTime();
	        updateDate(cal.getYear() + 1900,
					cal.getMonth(), cal.getDate(), _past);
	    }
	    Log.d("113", ""+_past);
	}
	@Override 
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
	Preference preference) {

	if (preference == (Preference) findPreference("date") ) {
		DatePickerDialogExtended dialog = new DatePickerDialogExtended(this, this, _year, _month, _day);
		GregorianCalendar dataNow = new GregorianCalendar();
		dialog.setDateLimit(
			dataNow.get(Calendar.YEAR), dataNow.get(Calendar.MONTH),
			dataNow.get(Calendar.DAY_OF_MONTH), _past);
		dialog.updateDate(_year, _month, _day);
		dialog.show();
	}
	else if (preference == (CheckBoxPreference) findPreference("past")){
		preference = (CheckBoxPreference)preference;
		boolean past;
		PreferenceManager editor = preference.getPreferenceManager();
		past = editor.getSharedPreferences().getBoolean("past",false);
		if (past != _past) {

			Date cal = new GregorianCalendar().getTime();

			_past = past;
			updateDate(cal.getYear() + 1900,
					cal.getMonth(), cal.getDate(), _past);
		}
	}
	else if (preference == (Preference) findPreference("pic")){
		Intent intent = new Intent(this, PicActivity.class);
		startActivityForResult(intent, EDIT_PIC);
	}
	else if (preference == (Preference) findPreference("back")){
		Intent intent2 = new Intent(this, PicBackActivity.class);
		startActivityForResult(intent2, EDIT_BACK);
	}
	

	return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		updateDate(year, monthOfYear, dayOfMonth, _past);
		
	}
	public void updateDate(int year, int monthOfYear, int dayOfMonth,
			boolean past) {
		_year = year;
		_month = monthOfYear;
		_day = dayOfMonth;
		Preference preference = (Preference) findPreference("date");
		preference.setSummary("" + _day + "/" + (_month + 1) + "/" + _year);
		//_textView.setText("" + _day + "/" + (_month + 1) + "/" + _year);
		_past = past;
	}
	@Override
	public void onBackPressed() {

		Bundle bngl = new Bundle();
		Date d = new Date(_year - 1900, _month, _day);

		bngl.putString(DBAdapter.KEY_TEXT, _event);
		bngl.putLong(DBAdapter.KEY_DATE, d.getTime());
		bngl.putBoolean(DBAdapter.KEY_DATE_PAST, _past);
		bngl.putInt(DBAdapter.KEY_PIC, picID);
		bngl.putInt(DBAdapter.KEY_PIC_BACK, backID);
		bngl.putInt(DBAdapter.KEY_PIC_TICK, tickID);

		if (mRowId != null)
			bngl.putLong(DBAdapter.KEY_ROWID, mRowId);
		else {
			SharedPreferences.Editor editor = pref.edit();
			editor.putString(DBAdapter.KEY_TEXT, "");
			editor.putLong(DBAdapter.KEY_DATE, 0);
			editor.putBoolean(DBAdapter.KEY_DATE_PAST, true);

			editor.putInt(DBAdapter.KEY_PIC, R.drawable.fig_r);
			editor.putInt(DBAdapter.KEY_PIC_BACK, R.drawable.line_r);
			editor.putInt(DBAdapter.KEY_PIC_TICK, R.drawable.point_r);

			editor.commit();
		}

		Intent rezIntend = new Intent();
		rezIntend.putExtras(bngl);
		setResult(RESULT_OK, rezIntend);
		finish();

	}	
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		Bundle bundle = new Bundle();
		if (intent != null)
			bundle = intent.getExtras();
		switch (requestCode) {
		case EDIT_PIC:
			switch (resultCode) {
			case RESULT_OK:
				picID = bundle.getInt(DBAdapter.KEY_PIC);
				
				PicList picList = new PicList();
				Preference preference = findPreference("pic");
				preference.setSummary(picList.getName(picID));
				break;
			}
			break;
		case EDIT_BACK:
			switch (resultCode) {
			case RESULT_OK:
				backID = bundle.getInt(DBAdapter.KEY_PIC_BACK);
				tickID = bundle.getInt(DBAdapter.KEY_PIC_TICK);

				PicBackList picList = new PicBackList();
				Preference preference = findPreference("back");
				preference.setSummary(picList.getName(backID,tickID));
				break;
			}
			break;
		}
	}
}
