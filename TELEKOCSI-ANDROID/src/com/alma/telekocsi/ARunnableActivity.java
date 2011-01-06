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
			progress.dismiss();
		}
	};
	
	@Override
	abstract public void run();
	
	protected void showProgressDialog(Context context
										,String title
										,String description
										,boolean t
										,boolean u){
		progress = ProgressDialog.show(context, title, description, t, u);
	}
	
	protected void showProgressDialog(Context context, String title){
		showProgressDialog(context, title, "", true, false);
	}
	
	protected void stopProgressDialog(){
		handler.sendEmptyMessage(0);
	}

}
