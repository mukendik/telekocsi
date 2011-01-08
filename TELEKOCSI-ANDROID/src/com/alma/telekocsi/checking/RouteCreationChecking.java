package com.alma.telekocsi.checking;

import android.app.Activity;
import android.os.Bundle;

public class RouteCreationChecking extends Activity {
	
	public static final int INVALID_DEPARTURE_PLACE = 1;
	public static final int INVALID_DEPARTURE_TIME = 2;
	public static final int INVALID_ARRIVAL_PLACE = 3;
	public static final int INVALID_ARRIVAL_TIME = 4;
	public static final int INVALID_ARRIVAL_TIMES = 5;
	public static final int INVALID_FREQUENCY = 6;
	public static final int INVALID_PRICE = 7;
	
	private static final String DEFAULT_TIME = "Sélectionner..";
	private static final int MAX_PRICE_AVAILABLE = 50;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		 
		Bundle extras = getIntent().getExtras();
		String departurePlace = extras.getString("departurePlace").toString();
		String departureTime = extras.getString("departureTime").toString();
		String arrivalPlace = extras.getString("arrivalPlace").toString();
		String arrivalTime = extras.getString("arrivalTime").toString();
		boolean frequency = extras.getBoolean("frequency");
		String price = extras.getString("price").toString();
		
		if(wrongDeparturePlace(departurePlace)){
			setResult(INVALID_DEPARTURE_PLACE);
		}
		else if(wrongArrivalPlace(arrivalPlace)){
			setResult(INVALID_ARRIVAL_PLACE);
		}
		else if(wrongDepartureTime(departureTime)){
			setResult(INVALID_DEPARTURE_TIME);
		}
		else if(wrongArrivalTime(arrivalTime)){
			setResult(INVALID_ARRIVAL_TIME);
		}
		else if(wrongArrivalTimes(departureTime, arrivalTime)){
			setResult(INVALID_ARRIVAL_TIMES);
		}
		else if(!frequency){
			setResult(INVALID_FREQUENCY);
		}
		else if(wrongPrice(price)){
			setResult(INVALID_PRICE);
		}
		else{
			setResult(RESULT_OK);
		}
		finish();
	}
	
	private boolean wrongDeparturePlace(String departure){
		return departure.equals("");
	}

	private boolean wrongArrivalPlace(String arrival){
		return arrival.equals("");
	}

	private boolean wrongDepartureTime(String departure){
		return departure.equals(DEFAULT_TIME);
	}

	private boolean wrongArrivalTime(String arrival){
		return arrival.equals(DEFAULT_TIME);
	}

	private boolean wrongArrivalTimes(String departure, String arrival){
		//recuperer les date java en fonction des string
		//et voir si departure < arrival ou non
		return false;
	}

	private boolean wrongPrice(String price){
		return price.equals("") || Integer.valueOf(price)>MAX_PRICE_AVAILABLE;
	}

}
