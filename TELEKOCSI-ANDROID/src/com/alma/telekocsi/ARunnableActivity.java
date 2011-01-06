package com.alma.telekocsi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public abstract class ARunnableActivity extends Activity implements Runnable {

	protected ProgressDialog progress = null;
	
	protected Handler handler = new Handler() {
		
		@Override
		public void handleMessage(Message msg) {
			if(progress!=null){
				progress.dismiss();
			}
		}
		
	};
	
	@Override
	abstract public void run();
	
	protected void showProgressDialog(Context context
										,String title
										,String message
										,boolean indeterminate
										,boolean cancelable){
		progress = ProgressDialog.show(context, title, message, indeterminate, cancelable);
	}
	
	protected void startProgressDialog(Context context){
		showProgressDialog(context, "Chargement...", "", true, false);
	}
	
	protected void stopProgressDialog(){
		handler.sendEmptyMessage(0);
	}

}
