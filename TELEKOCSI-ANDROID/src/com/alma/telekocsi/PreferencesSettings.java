package com.alma.telekocsi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alma.telekocsi.checking.PreferencesChecking;
import com.alma.telekocsi.dao.profil.Profil;
import com.alma.telekocsi.session.Session;
import com.alma.telekocsi.session.SessionFactory;

public class PreferencesSettings extends Activity {
	
	private static final int CHECKING = 1;
	private static final int MAIN_MENU = 2;

	private OnClickListener onClickListener = null;
	private Button backButton;
	private Button registeringSubmitButton;
	private RadioGroup smoker;
	private RadioGroup animals;
	private RadioGroup music;
	private RadioGroup discussion;
	private RadioGroup detours;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.preferences_settings);

        smoker = (RadioGroup)findViewById(R.id.smoker_radio_group);
        animals = (RadioGroup)findViewById(R.id.animals_radio_group);
        detours = (RadioGroup)findViewById(R.id.detours_radio_group);
        discussion = (RadioGroup)findViewById(R.id.discussion_radio_group);
        music = (RadioGroup)findViewById(R.id.music_radio_group);
        
        backButton = (Button)findViewById(R.id.preferences_settings_back_button);
        backButton.setOnClickListener(getOnClickListener()); 
         
        registeringSubmitButton = (Button)findViewById(R.id.registering_submit_button);
        registeringSubmitButton.setOnClickListener(getOnClickListener());
    }
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    	switch (requestCode) {
    	case CHECKING:
    		switch(resultCode) {
    		case RESULT_OK:
    			saveSettings();
    			startActivityForResult(new Intent(this, MainMenu.class), MAIN_MENU);
    			break;
    		}
		case MAIN_MENU:
			switch(resultCode){
			case RESULT_CANCELED:
				break;
			}
			break;
    	}
    }
	
	private OnClickListener getOnClickListener(){
		if(onClickListener==null){
			onClickListener = makeOnClickListener();
		}
		return onClickListener;
	}
	
	private OnClickListener makeOnClickListener(){
		return new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(v==backButton){
					goBack();
				}
				else if(v==registeringSubmitButton){
					startMainMenu();
				}
			}
			
		};
	}
	
	private void goBack(){
		finish();
	}
	
	private void startMainMenu(){
		Intent intent = new Intent(this, PreferencesChecking.class);
    	startActivityForResult(intent, CHECKING);
	}

	private void saveSettings(){
		Session session = SessionFactory.getCurrentSession(this);
		Profil prof = new Profil();
		Intent intent = getIntent();
		prof.setNom(intent.getStringExtra("name"));
		prof.setPrenom(intent.getStringExtra("firstName"));
		prof.setEmail(intent.getStringExtra("email"));
		prof.setMotDePasse(intent.getStringExtra("password"));
		if(smoker!=null){
			RadioButton rb = (RadioButton)findViewById(smoker.getCheckedRadioButtonId());
			if(rb!=null){
				prof.setDetours(rb.getText().toString());
			}
		}
		if(animals!=null){
			RadioButton rb = (RadioButton)findViewById(animals.getCheckedRadioButtonId());
			if(rb!=null){
				prof.setAnimaux(rb.getText().toString());
			}
		}
		if(detours!=null){
			RadioButton rb = (RadioButton)findViewById(detours.getCheckedRadioButtonId());
			if(rb!=null){
				prof.setDetours(rb.getText().toString());
			}
		}
		if(discussion!=null){
			RadioButton rb = (RadioButton)findViewById(discussion.getCheckedRadioButtonId());
			if(rb!=null){
				prof.setDiscussion(rb.getText().toString());
			}
		}
		if(music!=null){
			RadioButton rb = (RadioButton)findViewById(music.getCheckedRadioButtonId());
			if(rb!=null){
				prof.setMusique(rb.getText().toString());
			}
		}
		prof.setDateNaissance(String.format("%s-%s-%s"
				, intent.getStringExtra("jj")
				,intent.getStringExtra("mm")
				,intent.getStringExtra("aaaa")));
		prof.setSexe(intent.getStringExtra("sexe"));
		session.saveProfile(prof);
	}
}

