package com.alma.telekocsi.map;

import com.google.android.maps.Overlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Projection;

public class MapOverlay extends Overlay
{
	public static final int DEPART = 1;
	public static final int PATH = 2;
	public static final int ARRIVE = 3;
	private static final int MARKER = 4;
	
	private GeoPoint gp1;
	private GeoPoint gp2;
	private int mRadius=6;
	private int mode=0;
	private int defaultColor;
	@SuppressWarnings("unused")
	private String text="";
	@SuppressWarnings("unused")
	private Bitmap img = null;
	private int marker;
	
	public MapOverlay(GeoPoint gp, int pinMarker) {
		this.gp1 = gp;
		this.marker = pinMarker;
		mode = MARKER;
	}
	

	public MapOverlay(GeoPoint gp1,GeoPoint gp2,int mode) // GeoPoint is a int. (6E)
	{
		this.gp1 = gp1;
		this.gp2 = gp2;
		this.mode = mode;
		defaultColor = 999; // no defaultColor

	}

	public MapOverlay(GeoPoint gp1,GeoPoint gp2,int mode, int defaultColor)
	{
		this.gp1 = gp1;
		this.gp2 = gp2;
		this.mode = mode;
		this.defaultColor = defaultColor;
	}
	public void setText(String t)
	{
		this.text = t;
	}
	public void setBitmap(Bitmap bitmap)
	{
		this.img = bitmap;
	}
	public int getMode()
	{
		return mode;
	}

	@Override
	public boolean draw	(Canvas canvas, MapView mapView, boolean shadow, long when)	{
		try {
		Projection projection = mapView.getProjection();
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		
		if(mode==MARKER){			
			//---translate the GeoPoint to screen pixels---
			Point screenPts = new Point();
			projection.toPixels(gp1, screenPts);

			//---add the marker---
			Bitmap bmp = BitmapFactory.decodeResource(
					mapView.getContext().getResources(), marker);           
			canvas.drawBitmap(bmp, screenPts.x-24, screenPts.y-48, paint);         
			//return true;
		}else if (shadow == false)
		{
	
			Point screenPts = new Point();
			projection.toPixels(gp1, screenPts);
			// mode=1&#65306;start
			if(mode==DEPART)
			{
				if(defaultColor==999)
					paint.setColor(Color.BLUE);
				else
					paint.setColor(defaultColor);
				RectF oval=new RectF(screenPts.x - mRadius, screenPts.y - mRadius,
						screenPts.x + mRadius, screenPts.y + mRadius);
				// start point
				canvas.drawOval(oval, paint);
			}
			// mode=2&#65306;path
			else if(mode==PATH)
			{
				if(defaultColor==999)
					paint.setColor(Color.RED);
				else
					paint.setColor(defaultColor);
				Point screenPts2 = new Point();
				projection.toPixels(gp2, screenPts2);
				paint.setStrokeWidth(5);
				paint.setAlpha(120);
				canvas.drawLine(screenPts.x, screenPts.y, screenPts2.x,screenPts2.y, paint);
			}
			/* mode=3&#65306;end */
			else if(mode==ARRIVE)
			{
				/* the last path */

				if(defaultColor==999)
					paint.setColor(Color.GREEN);
				else
					paint.setColor(defaultColor);
				Point screenPts2 = new Point();
				projection.toPixels(gp2, screenPts2);
				paint.setStrokeWidth(5);
				paint.setAlpha(120);
				canvas.drawLine(screenPts.x, screenPts.y, screenPts2.x,screenPts2.y, paint);
				RectF oval=new RectF(screenPts2.x - mRadius,screenPts2.y - mRadius,
						screenPts2.x + mRadius,screenPts2.y + mRadius);
				/* end point */
				paint.setAlpha(255);
				canvas.drawOval(oval, paint);
			}
		}
		
		}catch(Exception e){
			Log.e(getClass().getSimpleName(), e.toString());
		}
		return super.draw(canvas, mapView, shadow, when);
		
		
	}
	
	
	
	
/*
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) 
	{   
		//---when user lifts his finger---
		if (event.getAction() == 1) {                
			GeoPoint p = mapView.getProjection().fromPixels(
					(int) event.getX(),
					(int) event.getY());
			Toast.makeText(context.getBaseContext(), 
					p.getLatitudeE6() / 1E6 + "," + 
					p.getLongitudeE6() /1E6 , 
					Toast.LENGTH_SHORT).show();
		}                            
		return false;
	}*/
	
	

}



