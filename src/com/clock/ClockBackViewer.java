package com.clock;

import java.util.Date;


import android.content.Context;
import android.util.AttributeSet;

public class ClockBackViewer extends ClockViewer {

	public ClockBackViewer(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ClockBackViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		Date date = new Date();
		textView.setHeight(0);
		setData("", date.getTime(), false, 0, R.drawable.line_r,R.drawable.point_r);
	}

	public void setResource(int backID, int tickID) {
		// TODO Auto-generated method stub
		setBack(backID);
		setTick(tickID);
		//TODO дурацкое обнуление
		ruler = null;
		invalidate();
		
	}
	
	
	

}
