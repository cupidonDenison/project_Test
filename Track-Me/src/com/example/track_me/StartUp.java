package com.example.track_me;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.track_me.others.JSONParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StartUp extends Activity {

	private static final String numberURL = SplashScreen.SERVER_IP+"project/addNumber.php";
	private static String NUMBER_PREFERENCES_FILE = "/data/data/com.example.track_me/shared_prefs/number.xml";
	
	private static String result;
	ImageButton mainBtn;
	String num, name;
	EditText nameEditText, numberEditText;
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		mainBtn = (ImageButton) findViewById(R.id.btnOk);
		mainBtn.setBackgroundColor(Color.TRANSPARENT);
		numberEditText = ((EditText) findViewById(R.id.edtPhone));
		nameEditText = ((EditText) findViewById(R.id.edtName));
		mainBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				openAlert(v);
			}

		});
	}

	private void openAlert(View view) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				StartUp.this);
		num = numberEditText.getText().toString();
		name = nameEditText.getText().toString();
		num = "+230"+num;
		LinearLayout layout = new LinearLayout(this);
		LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		layout.setLayoutParams(parms);

		layout.setGravity(Gravity.CLIP_VERTICAL);
		layout.setPadding(2, 2, 2, 2);

		TextView phone_num = new TextView(this);
		phone_num.setText(num);

		LinearLayout.LayoutParams tv1Params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tv1Params.bottomMargin = 5;
		layout.addView(phone_num, tv1Params);

		alertDialogBuilder.setView(layout);
		alertDialogBuilder.setTitle("Phone Number Verification");
		alertDialogBuilder.setMessage("Verify Your Phone Number : ");

		// set positive button: Yes message
		alertDialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						// send number to server

						new SendNumber().execute();
						// create sharedPreferences to store phone number

						// go to a new activity of the app

					}
				});
		// set negative button: No message
		alertDialogBuilder.setNegativeButton("Edit",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// cancel the alert box and put a Toast to the user
						dialog.cancel();
						Toast.makeText(getApplicationContext(), "",
								Toast.LENGTH_LONG).show();
					}
				});
		// set neutral button: Exit the app message
		alertDialogBuilder.setNeutralButton("Exit the app",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// exit the app and go to the HOME
						StartUp.this.finish();
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		// show alert
		alertDialog.show();
	}

	private class SendNumber extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {

			pDialog = new ProgressDialog(StartUp.this);
			pDialog.setMessage("Registering Users.....");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			JSONParser parser = new JSONParser();
			List<NameValuePair> details = new ArrayList<NameValuePair>();

			details.add(new BasicNameValuePair("number", num));
			details.add(new BasicNameValuePair("name", name));

			JSONObject responseObject = parser.makeHttpRequest(numberURL,
					"POST", details);

			Log.i("Number", responseObject.toString());

			try {

				result = responseObject.getString("result");

				

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void resul) {
			pDialog.dismiss();
			
			if (result.equals("true")) {
				//Log.i("Number result t ", result);
				SharedPreferences numberPref = getSharedPreferences(
						"number",
						Context.MODE_PRIVATE);
				
				SharedPreferences.Editor preferencesEditor = numberPref.edit();
				preferencesEditor.putString("phone", num);
				preferencesEditor.commit();
				
				
				Intent positveActivity = new Intent(
						getApplicationContext(), VerificationActivity.class);
				
				StartUp.this.finish();
				
				startActivity(positveActivity);

			} else if(result.equals("false")){
				Log.i("Number result f ", result);
				Toast.makeText(getApplicationContext(), "Invalid number",
						Toast.LENGTH_LONG).show();
			}else if(result.equals("no data")){
				Log.i("Number result n ", result);
				Toast.makeText(getApplicationContext(), "All field are required",
						Toast.LENGTH_LONG).show();
			}
		}

	}// End class SendNumber

}// End Activity
