package com.alma.telekocsi;


import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class TrajetDetail extends ARunnableActivity {

	private OnClickListener onClickListener = null;
	private Button btRetour;
	private TextView tvDepartArrivee;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.trajetdetail);
        
        tvDepartArrivee = (TextView)findViewById(R.id.tvDTDepartArrivee);
        tvDepartArrivee.setText("LES HERBIERS -> ANGERS");
        
        btRetour = (Button)findViewById(R.id.btDTRestour);
        btRetour.setOnClickListener(getOnClickListener());
    }
    
    
    public OnClickListener getOnClickListener() {
    	
    	if (onClickListener == null) {
    		onClickListener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (v == btRetour) {
						setResult(RESULT_OK);
						finish();
					}
				}
			};
    	}
    	
    	return onClickListener;
    }    
    
    

	@Override
	public void run() {

	}

}
