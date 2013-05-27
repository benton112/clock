package com.clock;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.widget.DatePicker;

public class DatePickerDialogExtended extends DatePickerDialog {

	private int _year;
	private int _month;
	private int _day;
	private boolean _past;
	
	
	public DatePickerDialogExtended(Context context,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		// TODO Auto-generated constructor stub
	}
	public void setDateLimit(int year, int month, int day, boolean past){
		_year = year;
		_month = month;
		_day = day;
		_past = past;
	}
@Override
public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth)
{
	if (_past == false){
        if (year < _year)
            view.updateDate(_year, _month, _day);

            if (monthOfYear < _month && year == _year)
            view.updateDate(_year, _month, _day);

            if (dayOfMonth < _day && year == _year && monthOfYear == _month)
            view.updateDate(_year, _month, _day);
	}
	else {

        if (year > _year)
            view.updateDate(_year, _month, _day);

            if (monthOfYear > _month && year == _year)
            view.updateDate(_year, _month, _day);

            if (dayOfMonth > _day && year == _year && monthOfYear == _month)
            view.updateDate(_year, _month, _day);
	}
}
}
