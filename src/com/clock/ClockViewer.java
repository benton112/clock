package com.clock;



import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClockViewer extends View{

	private int width;
	private int height;
	private int marginLeft;
	private int marginRight;
	private int marginTop;
	private int marginBottom;

	private int year;
	private int month;
	private int day;
	private boolean _past;
	
	private String text;
	private GregorianCalendar _dataEvent; 
	
	
	
	private RelativeLayout baseLayout;
	protected TextView textView;
	
	private TextView textRuler;
	private TextView textDate;
	
	protected Picture ruler;
	private int rulerHeight;
	
	private int picID;
	
	private int backID;
	private int tickID;
	
	
	
	private void inicialiseStuff(Context context){
		_dataEvent = null;
		ruler = null;
		//установка DP размера шрифта
		// Convert the dips to pixels
		//final float scale = getContext().getResources().getDisplayMetrics().density;
		//textSize = (int) (GESTURE_THRESHOLD_DIP * scale + 0.5f);
		
		
		
		baseLayout = new RelativeLayout(this.getContext());
		textView = new TextView(context);
		textRuler = new TextView(context);
		textDate = new TextView(context);
		//Устанавливает стандартный для темы цвет текста?
		textView.setTextAppearance(context, android.R.style.TextAppearance);
		textDate.setTextAppearance(context, android.R.style.TextAppearance);
		textRuler.setTextAppearance(context, android.R.style.TextAppearance);
		baseLayout.addView(textView);
		baseLayout.addView(textDate);
		baseLayout.addView(textRuler);
		
		setPic(R.drawable.fig_r);
		setTick(R.drawable.line_r);
		setBack(R.drawable.point_r);
		

	
	}
	public ClockViewer(Context context) {
		super(context);
		inicialiseStuff(context);
		
		// TODO Auto-generated constructor stub
	}
	public ClockViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		inicialiseStuff(context);
		_past = false;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClockAttributeSet);
		
		long d = (long) a.getFloat(R.styleable.ClockAttributeSet_date,new Date().getTime());
		setEventDate(d); 
		
		CharSequence s = a.getString(R.styleable.ClockAttributeSet_android_text);
		if(s != null){
			setText(s.toString());
		} else {
			setText("No smockingn already");
		}
		


		//textView.setText("onCreate");
		
		//textDate.setText(dataStart.toString());
		textRuler.setText("ZZZZ");
		
		a.recycle();
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		
		Log.d("112", "measure ");

		/*
		Log.d("112", "Width spec: " + MeasureSpec.toString(widthMeasureSpec));
		Log.d("112", "Height spec: " + MeasureSpec.toString(heightMeasureSpec));
*/
	    ViewGroup.MarginLayoutParams lp =(ViewGroup.MarginLayoutParams) this.getLayoutParams();
	    marginLeft = lp.leftMargin;
	    marginRight = lp.rightMargin;
	    marginTop = lp.topMargin;
	    marginBottom = lp.bottomMargin;
	    
		//определение ширины
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ||
			MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST)
			this.width = MeasureSpec.getSize(widthMeasureSpec);
		else //MeasureSpec.UNSPECIFIED
			this.width = 400;
		textView.measure(MeasureSpec.makeMeasureSpec(width-marginLeft-marginRight, MeasureSpec.AT_MOST), 
				heightMeasureSpec);
		
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY// ||
			//MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST
			)	{
			this.height = MeasureSpec.getSize(heightMeasureSpec);
			textView.measure(MeasureSpec.makeMeasureSpec(width-marginLeft-marginRight, MeasureSpec.AT_MOST), 
							 MeasureSpec.makeMeasureSpec(height-rulerHeight-marginTop-marginBottom, MeasureSpec.AT_MOST));
		}
		
		else {//MeasureSpec.UNSPECIFIED
			this.height = rulerHeight+ textView.getMeasuredHeight()+marginBottom+marginTop;
			
			
		}
		
		textRuler.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), 
				 MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
		//textDate.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST), 
		//		 MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST));
		
		setMeasuredDimension(width,height);
	}
	@Override
	protected void onLayout (boolean changed,int l, int t, int r, int b) {
		
	    super.onLayout(changed, l, t, r, b);
		textView.layout(0, 0,
				r-marginRight, b-marginBottom - rulerHeight);
	    
		//textView.layout(0, 0, 100, 100);
		textRuler.layout(width-textRuler.getMeasuredWidth(), height-textRuler.getMeasuredHeight(), r-marginRight,  b-marginBottom);
		//textDate.layout(width-textRuler.getMeasuredWidth(), height-textRuler.getMeasuredHeight(), r-marginRight,  b-marginBottom);
		//Log.d("113", "this.height "+this.height);
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldW,int oldH){
		super.onSizeChanged(w, h, oldW, oldH);
		Log.d("112", "sizeChanged");
	}
	
	@Override
	public void onDraw(Canvas canvas){
		canvas.drawPicture(pictureRuler());
		baseLayout.draw(canvas);

		
	}
	
	private Picture pictureRuler(){
		if (ruler == null){
			
			if (backID == 0)
				backID = R.drawable.line_r;
			if (tickID == 0)
				tickID = R.drawable.point_r;
			
			
			ruler = new Picture();
			Canvas canvas =  ruler.beginRecording(width, height);
			
			//canvas.drawColor(Color.LTGRAY);
			canvas.translate(0,height-rulerHeight);

			Paint p = new Paint();
			p.setAntiAlias(true);
			p.setStyle(Style.STROKE);
			p.setStrokeWidth(2);
			//p.setColor(Color.BLUE);
			p.setTypeface(textView.getTypeface());
			p.setTextSize(textView.getTextSize());
			
			//canvas.drawRect(0, 0, width, rulerHeight, p);
			
			//линия
			canvas.save();
			int i = 0;
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), backID);
			
			int widthLine = bitmap.getWidth();
			canvas.translate(0, rulerHeight / 2.0f - bitmap.getHeight() / 2.0f);
			
			while (i <= width){
				canvas.drawBitmap(bitmap,0,0,p);
				canvas.translate(widthLine, 0);
				i+=widthLine;
			}
			canvas.restore();
			//точки
			canvas.save();
			bitmap = BitmapFactory.decodeResource(getResources(), tickID);  
			int heightPoint = bitmap.getHeight();
			canvas.translate(0, rulerHeight / 2.0f - bitmap.getHeight() / 2.0f);
			
			//бегунок
			int divider = 1;
			
			//Calendar d = new GregorianCalendar(dataStart.get(Calendar.YEAR), dataStart.get(Calendar.MONTH), dataStart.get(Calendar.DATE));
			Calendar dataLimit = (Calendar) _dataEvent.clone();
			if (year > 5){
				divider = year * 2;
				textRuler.setText(R.string.main_years);
				if (_past == true)
					dataLimit.add(Calendar.YEAR, year*2);
				else
					dataLimit.add(Calendar.YEAR, -year*2);
			}
			else if (year > 0){
				divider = 5;
				textRuler.setText(R.string.main_years);
				if (_past == true) 
					dataLimit.add(Calendar.YEAR, 5);
				else
					dataLimit.add(Calendar.YEAR, -5);
			}
			else if (month > 0){
				divider = 12;
				textRuler.setText(R.string.main_months);
				if (_past == true) 
					dataLimit.add(Calendar.MONTH, 12);
				else
					dataLimit.add(Calendar.MONTH, -12);
			}
			else if (day > 7){
				divider = 4;
				textRuler.setText(R.string.main_weeks);
				if (_past == true) 
					dataLimit.add(Calendar.MONTH, 1);
				else
					dataLimit.add(Calendar.MONTH, -1);
			}
			else{
				divider = 7;
				
				textRuler.setText(R.string.main_days);
				if (_past == true) 
					dataLimit.add(Calendar.DAY_OF_MONTH, 7);
				else
					dataLimit.add(Calendar.DAY_OF_MONTH, -7);
			}
			requestLayout();
			
			float widthBetween = (width - bitmap.getWidth()) / ((float)(divider));
			
			for (int i1 = 0; i1 < divider+1; i1++){
				canvas.drawBitmap(bitmap,0,0,p);
				canvas.translate(widthBetween, 0);
			}
			canvas.restore();
			
			
			if ( picID != 0){
				bitmap = BitmapFactory.decodeResource(getResources(), picID);
		
				canvas.save();
				canvas.translate(0, rulerHeight / 2.0f - bitmap.getHeight() / 2.0f);
				long secondsAll;
				
				if (_past == true)
					secondsAll = dataLimit.getTimeInMillis() - _dataEvent.getTimeInMillis();
				else
					secondsAll =  _dataEvent.getTimeInMillis() - dataLimit.getTimeInMillis();
						
				GregorianCalendar dataNow = new GregorianCalendar();
				dataNow.set(dataNow.get(Calendar.YEAR),dataNow.get(Calendar.MONTH),dataNow.get(Calendar.DATE),0,0,0);
				
				long secondsOffset;
				if (_past == true)
					secondsOffset = dataNow.getTimeInMillis() - _dataEvent.getTimeInMillis();
				else
					secondsOffset = _dataEvent.getTimeInMillis() - dataNow.getTimeInMillis(); 
				
				//if ( secondsOffset >= secondsAll)

				if (_past == true)
					canvas.translate((width-bitmap.getWidth())  * secondsOffset / secondsAll, 
									0);
				else
					canvas.translate( (width-bitmap.getWidth()) - (width-bitmap.getWidth())  * secondsOffset / secondsAll, 
							0);
					
				canvas.drawBitmap(bitmap,0,0,p);
				canvas.restore();
			}
			
			
			ruler.endRecording();

		}
		return ruler;
	}
	public void setPic(int picID){
		this.picID = picID;
		if (picID != 0){
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), picID);
		rulerHeight = bitmap.getHeight();
		}
	}
	public void setBack(int backID){
		this.backID = backID;
	}
	public void setTick(int tickID){
		this.tickID = tickID;
	}
	public void setData(String s, long date, boolean past, int picID, int backID, int tickID){
		_past = past;
		setEventDate(date);
		setText(s);
		setPic(picID);
		setBack(backID);
		setTick(tickID);

		

		
		//TODO как-то криво обнуляется
		//обнуление линейки, чтобы перерисовалось повыше
		ruler = null;
		requestLayout();
		invalidate();
		
	}
	public void setData(String s, long date, short date_past, int picID, int backID, int tickID){
		if (date_past == 1)
			setData(s, date, true, picID, backID, tickID);
		else
			setData(s, date, false, picID, backID, tickID);
		
	}
	private void setEventDate(long date) {
		Date then = new Date (date);
		_dataEvent = new GregorianCalendar(then.getYear()+1900,then.getMonth(),then.getDate());
		GregorianCalendar dataNow = new GregorianCalendar();
	if (_past == false && _dataEvent.before(dataNow) ){
		//TODO добавить если будущее уже прошло
	}
	else if (_dataEvent.before(dataNow))
			calcDayMOnthYear(_dataEvent, dataNow);
	else
			calcDayMOnthYear(dataNow, _dataEvent);
	}
	private void calcDayMOnthYear(Calendar dataStart, Calendar dataEnd){

		day = dataEnd.get(Calendar.DATE) - dataStart.get(Calendar.DATE);
		month = 0;
		year = 0;
		
		Calendar c = Calendar.getInstance();
		c.set(dataEnd.get(Calendar.YEAR), dataEnd.get(Calendar.MONTH), dataEnd.get(Calendar.DATE));
		if (day < 0){
			if ( dataEnd.get(Calendar.MONTH) == Calendar.JANUARY){
				c.set (dataEnd.get(Calendar.YEAR)-1, Calendar.DECEMBER, dataEnd.get(Calendar.DATE));
			}
			else	{
				c.set(Calendar.MONTH, dataEnd.get(Calendar.MONTH)-1);
			}
		
			month -= 1;	
			//Log.d("113", "actMAx "+ c.getActualMaximum(Calendar.DAY_OF_MONTH) +" day  "+  day + "date " +dataEnd.get(Calendar.DATE) );			
			day = c.getActualMaximum(Calendar.DAY_OF_MONTH) + day;// + dataEnd.get(Calendar.DATE);
		}
		
		month += dataEnd.get(Calendar.MONTH) - dataStart.get(Calendar.MONTH);
		
		if (month < 0){

			month = 12 + month;// + dataEnd.get(Calendar.MONTH);
			year -= 1;
		}
		year += dataEnd.get(Calendar.YEAR) - dataStart.get(Calendar.YEAR);
	}
	private void setText(String s) {
		
		text = "";
		
		if (year != 0){
			s  +=" " + year + " year";
				if (year != 1)
					s += "s";
		}
		if (month != 0){
		s  +=" " + month + " month";
		if (month != 1)
			s += "s";
		}
		if (day != 0){
		s  +=" " + day + " day";
		if (day != 1)
			s += "s";
		}
		
		//TODO локализация
		text = s;
		textView.setText(text);
	
	}


}
