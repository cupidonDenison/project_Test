package com.example.gcmexample;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {

	public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    NotificationManager notificationManager;
	TextView myLabel;
	Context myContext;
	String registrationID ="Default value";
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	
	String SENDER_ID ="609320741786";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		myLabel = (TextView) findViewById(R.id.myLabel);
		
		myContext = getApplicationContext();
		checkPlayServices();
		
		if(checkPlayServices()){
			 gcm = GoogleCloudMessaging.getInstance(this);
	         
		}//end if statement
		
		new RegistrationInBackground().execute();
		
	}//End onCreate()

	
	//check if google play services is available
	private boolean checkPlayServices() {
	    int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	    if (resultCode != ConnectionResult.SUCCESS) {
	        if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	            GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                    PLAY_SERVICES_RESOLUTION_REQUEST).show();
	        } else {
	            Log.i("GMS", "This device is not supported.");
	            finish();
	        }
	        return false;
	    }else{
	    	myLabel.setText("Google play services available");
	    }
	    return true;
	}//End checkPlayServices
	
	class RegistrationInBackground extends AsyncTask<Void, Void, Void>{

		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			/*dialog = new ProgressDialog(getApplicationContext());
			dialog.setMessage("Registering App");
			dialog.setIndeterminate(false);
			dialog.setCancelable(false);
			dialog.show();
			*/
		}
		@Override
		protected Void doInBackground(Void... params) {
			try{
				if(gcm == null){
					gcm = GoogleCloudMessaging.getInstance(myContext);
				}
				
				registrationID = gcm.register(SENDER_ID);
			}catch(Exception e){
				Log.e("GCM registration error", "Error occur", e);
			}
			
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			//dialog.dismiss();
			
			myLabel.setText("Registration Id = "+registrationID);
			Log.i("Gcm registration id  ; ", registrationID);
			super.onPostExecute(result);
		}
		
				
	}//end asyncTask
	
	
	public void notify(View view){
		Toast.makeText(getApplicationContext(), "Notification button clicked", Toast.LENGTH_LONG).show();
		
		notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, AnotherActivity.class), PendingIntent.FLAG_CANCEL_CURRENT);
		String msg = "my test notification";
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("GCM Notification")
				.setStyle(new NotificationCompat.BigTextStyle()
				.bigText(msg))
				.setContentText(msg).setAutoCancel(true);
		
		mBuilder.setContentIntent(contentIntent);
		notificationManager.notify(1, mBuilder.build());
	}
	

class SendData extends AsyncTask<Void, Void, Void>{

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	@Override
	protected Void doInBackground(Void... arg0) {
		String msg = "";
		try {
		Bundle data = new Bundle();
		data.putString("my_message", "Hello World");
		data.putString("my_action",
		"com.google.android.gcm.demo.app.ECHO_NOW");
		String id = Integer.toString(msgId.incrementAndGet());
		gcm.send(SENDER_ID + "@gcm.googleapis.com", id, data);
		msg = "Sent message";
		
		Log.i("Gcm send", msg);
		} catch (IOException ex) {
		msg = "Error :" + ex.getMessage();
		Log.i("Gcm send error ", msg);
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	}
	
}//End asyncTask

public void onClick(View view){
	new SendData().execute();
}


}
