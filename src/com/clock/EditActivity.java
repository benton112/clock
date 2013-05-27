package com.clock;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.R.array;
import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity implements OnClickListener {

	private static final int EDIT_PIC = 0;
	private static final int EDIT_BACK = 1;

	private final int EVENT_DIALOG = 0;
	private final int DATE_DIALOG = 1;
	private final int DATE_TYPE_DIALOG = 2;

	private TextView _eventText;
	private TextView _textView;
	private TextView _dateType;

	private TextView _picText;
	private TextView _picBackText;

	private Long mRowId;

	private int picID;
	private int backID;
	private int tickID;

	private SharedPreferences pref;

	private int _year;
	private int _month;
	private int _day;
	private boolean _past;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editstr);

		
		_textView = (TextView) findViewById(R.id.dateText);
		_dateType = (TextView) findViewById(R.id.dateType);
		_eventText = (TextView) findViewById(R.id.eventText);
		_picText = (TextView) findViewById(R.id.picText);

		_picBackText = (TextView) findViewById(R.id.picBackText);

		_eventText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				updateExample();
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		mRowId = null;

		final Button btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(this);

		final Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(this);

		final View eventTextLayout = (View) findViewById(R.id.eventTextLayout);
		eventTextLayout.setOnClickListener(this);

		final View dateLayout = (View) findViewById(R.id.dateLayout);
		dateLayout.setOnClickListener(this);

		final View dateTypeLayout = (View) findViewById(R.id.dateTypeLayout);
		dateTypeLayout.setOnClickListener(this);

		final View picLayout = (View) findViewById(R.id.picLayout);
		picLayout.setOnClickListener(this);

		final View picBackLayout = (View) findViewById(R.id.picBackLayuot);
		picBackLayout.setOnClickListener(this);

		long date;
		String text;

		pref = getPreferences(MODE_PRIVATE);

		// Подстановка редактируемых полей
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			text = extras.getString(DBAdapter.KEY_TEXT);
			date = extras.getLong(DBAdapter.KEY_DATE);
			_past = extras.getBoolean(DBAdapter.KEY_DATE_PAST);
			mRowId = extras.getLong(DBAdapter.KEY_ROWID);
			picID = extras.getInt(DBAdapter.KEY_PIC);
			backID = extras.getInt(DBAdapter.KEY_PIC_BACK);
			tickID = extras.getInt(DBAdapter.KEY_PIC_TICK);

		} else {
			// Подстановка ранее введенных полей

			text = pref.getString(DBAdapter.KEY_TEXT, "");
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

		_textView.setText("" + _day + "/" + (_month + 1) + "/" + _year);
		_eventText.setText(text);

		String[] arr = getResources().getStringArray(R.array.edit_dateType);

		if (_past == true)
			_dateType.setText(arr[0]);
		else
			_dateType.setText(arr[1]);

		// picView.setImageResource(picID);
		_picText.setText(getImageText(picID));
		_picBackText.setText(getBackText(backID, tickID));

	}

	public String getImageText(int picID) {
		Pic data[] = new Pic[] { new Pic(R.drawable.fig_r, "TrollFace"),
				new Pic(R.drawable.fig_r1, "Baby"),
				new Pic(R.drawable.fig_r2, "MageBaby"),
				new Pic(R.drawable.fig_r_, "Car"), };
		for (int i = 0; i < data.length; i++) {
			if (data[i].getPicId() == picID) {
				return data[i].getPicText();
			}
		}
		return "";
	}

	public String getBackText(int backID, int tickID) {
		PicBack data[] = new PicBack[] {
				new PicBack(R.drawable.line_r, R.drawable.point_r, "one"),
				new PicBack(R.drawable.line_r2, R.drawable.point_r2, "two")

		};
		for (int i = 0; i < data.length; i++) {
			if (data[i].getTickID() == tickID && data[i].getBackID() == backID) {
				return data[i].getBackText();
			}
		}
		return "";
	}

	public void updateExample() {
		final ClockViewer clockV = (ClockViewer) findViewById(R.id.clockViewer1);
		Date d = new Date(_year - 1900, _month, _day);
		clockV.setData(_eventText.getText().toString(), d.getTime(), _past,
				picID, backID, tickID);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.eventTextLayout:
			showDialog(EVENT_DIALOG);
			break;
		case R.id.dateLayout:
			showDialog(DATE_DIALOG);
			break;
		case R.id.dateTypeLayout:
			showDialog(DATE_TYPE_DIALOG);
			break;

		case R.id.picLayout:
			Intent intent = new Intent(this, PicActivity.class);
			startActivityForResult(intent, EDIT_PIC);
			break;

		case R.id.picBackLayuot:
			Intent intent2 = new Intent(this, PicBackActivity.class);
			startActivityForResult(intent2, EDIT_BACK);
			break;

		case R.id.btnSave:
			
			Bundle bngl = new Bundle();
			Date d = new Date(_year - 1900, _month, _day);

			bngl.putString(DBAdapter.KEY_TEXT, _eventText.getText().toString());
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
			break;
		case R.id.btnCancel:
			// showDialog(DATE_DIALOG);
			onBackPressed();
		}
	}

	public void updateDate(int year, int monthOfYear, int dayOfMonth,
			boolean past) {
		_year = year;
		_month = monthOfYear;
		_day = dayOfMonth;
		_textView.setText("" + _day + "/" + (_month + 1) + "/" + _year);
		_past = past;
		updateExample();
	}

	// Выбор даты
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case EVENT_DIALOG:

			break;
		case DATE_DIALOG:
			GregorianCalendar dataNow = new GregorianCalendar();
			((DatePickerDialogExtended) dialog).setDateLimit(
					dataNow.get(Calendar.YEAR), dataNow.get(Calendar.MONTH),
					dataNow.get(Calendar.DAY_OF_MONTH), _past);
			((DatePickerDialogExtended) dialog).updateDate(_year, _month, _day);
		}
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case EVENT_DIALOG:
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(R.string.edit_textH);

			// Set an EditText view to get user input
			final EditText input = new EditText(this);
			InputFilter[] FilterArray = new InputFilter[1];
			FilterArray[0] = new InputFilter.LengthFilter(160);
			input.setFilters(FilterArray);
			input.setText(_eventText.getText());
			
			alert.setView(input);

			alert.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							_eventText.setText(input.getText());
							// Do something with value!
						}
					});

			alert.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// Canceled.
						}
					});

			return alert.create();
		case DATE_DIALOG:
			DatePickerDialogExtended tpd = new DatePickerDialogExtended(this,
					myCallBack, _year, _month, _day);
			return tpd;
		case DATE_TYPE_DIALOG:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setTitle(R.string.edit_dateH);
			builder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialogInterface,
								int item) {
							// Toast.makeText(getApplicationContext(),
							// items[item], Toast.LENGTH_SHORT).show();
							// _dateType.setText(getResources().getStringArray(R.array.edit_dateType)[item]);
							return;
						}
					});

			builder.setSingleChoiceItems(R.array.edit_dateType,
					_past == true ? 0 : 1,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialogInterface,
								int item) {
							// Toast.makeText(getApplicationContext(),
							// items[item], Toast.LENGTH_SHORT).show();
							_dateType.setText(getResources().getStringArray(
									R.array.edit_dateType)[item]);
							boolean past;
							if (item == 0)
								past = true;
							else
								past = false;

							if (past != _past) {

								Date cal = new GregorianCalendar().getTime();

								_past = past;
								updateDate(cal.getYear() + 1900,
										cal.getMonth(), cal.getDate(), _past);
							}

							dialogInterface.dismiss();
							return;
						}
					});
			return builder.create();

		default:
			return super.onCreateDialog(id);
		}
	}

	OnDateSetListener myCallBack = new OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {

			updateDate(year, monthOfYear, dayOfMonth, _past);

		}
	};

	@Override
	public void onBackPressed() {

		SharedPreferences.Editor editor = pref.edit();
		if (mRowId != null) {

			// editor.putString("text","");
			// editor.putLong("date",0);
		} else {
			editor.putString("text", _eventText.getText().toString());
			editor.putLong("date",
					new Date(_year - 1900, _month, _day).getTime());
			editor.putBoolean("date_past", _past);

			editor.putInt(DBAdapter.KEY_PIC, picID);
			editor.putInt(DBAdapter.KEY_PIC_BACK, backID);
			editor.putInt(DBAdapter.KEY_PIC_TICK, picID);
		}
		editor.commit();
		setResult(RESULT_CANCELED);
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
				_picText.setText(getImageText(picID));
				updateExample();
				break;
			}
			break;
		case EDIT_BACK:
			switch (resultCode) {
			case RESULT_OK:
				backID = bundle.getInt(DBAdapter.KEY_PIC_BACK);
				tickID = bundle.getInt(DBAdapter.KEY_PIC_TICK);

				_picBackText.setText(getBackText(backID, tickID));

				updateExample();
				break;
			}
			break;
		}
	}

}
