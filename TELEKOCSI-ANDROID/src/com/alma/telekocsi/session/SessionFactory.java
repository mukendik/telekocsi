package com.alma.telekocsi.session;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 
 * @author Rv
 *
 */
public class SessionFactory{

	private static Session theSession;
	private static ServiceConnection mConnection = new ServiceConnection() {
	    public void onServiceConnected(ComponentName className, IBinder service) {
	        // This is called when the connection with the service has been
	        // established, giving us the service object we can use to
	        // interact with the service.  Because we have bound to a explicit
	        // service that we know is running in our own process, we can
	        // cast its IBinder to a concrete class and directly access it.
	        theSession = ((SessionImpl.LocalBinder)service).getService();
	    }

	    public void onServiceDisconnected(ComponentName className) {
	        // This is called when the connection with the service has been
	        // unexpectedly disconnected -- that is, its process crashed.
	        // Because it is running in our same process, we should never
	        // see this happen.
	        theSession = null;
	    }
	};
		
	/**
	 *  Point d'accès à la session courante
	 *  Si elle n'existe pas encore elle est crée
	 * @return La session courante
	 */
	public static Session getCurrentSession(){
		return theSession;
	}

	/**
	 * Initializer la factory
	 * @param context
	 */
	public static void init(Context context){
		if(theSession==null){
			context.bindService(new Intent(context, SessionImpl.class), mConnection, Context.BIND_AUTO_CREATE);
		}
	}
}
