package com.alma.telekocsi.map;


import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.widget.Toast;

import com.alma.telekocsi.R;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapOverlay extends Overlay {
	
	private GeoPoint p;
	private ContextWrapper context;

	public MapOverlay(ContextWrapper context, GeoPoint p) {
		this.context = context;
		this.p = p;
	}
	
	@Override
	public boolean draw(Canvas canvas, MapView mapView, 
			boolean shadow, long when) 
	{
		super.draw(canvas, mapView, shadow);                   

		//---translate the GeoPoint to screen pixels---
		Point screenPts = new Point();
		mapView.getProjection().toPixels(p, screenPts);

		//---add the marker---
		Bitmap bmp = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.pin_sq_down);           
		canvas.drawBitmap(bmp, screenPts.x-24, screenPts.y-48, null);         
		return true;
	}
	
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
      }
} 

