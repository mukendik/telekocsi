package com.alma.telekocsi;

import android.app.Activity;
import android.app.ProgressDialog;
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

}
