package com.example.track_me;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.track_me.entity.Place;
import com.example.track_me.others.JSONParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class MenuDetailsActivity extends FragmentActivity {

	ViewPager pager;
	TextView textViewPlaceName, textViewAddress, textViewDistance;
	RatingBar ratingBar;
	ImageView imageViewPlace;
	Button buttonFindPlace;
	Place selectedPlace;
	static String placeId,placeAddress;
	static double placeLat, placeLng;
	JSONParser parser;
	JSONObject jsonObj;
	static int placeRating;
	String placeDetailURL = SplashScreen.SERVER_IP
			+ "project/placeDetail.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_details);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		textViewPlaceName = (TextView) findViewById(R.id.textView_menu_details_activity_placeName);
		textViewAddress = (TextView) findViewById(R.id.textView_menu_details_activity_address);
		textViewDistance = (TextView) findViewById(R.id.textView_menu_details_activity_distance);
		buttonFindPlace = (Button) findViewById(R.id.button_menu_detail_activity);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar1);
		Bundle extras = getIntent().getExtras();

		String jsonMyObject = "";
		if (extras != null) {
			jsonMyObject = extras.getString("selectedPlace");
		}

		selectedPlace = new Gson().fromJson(jsonMyObject, Place.class);

		ActionBar ab = getActionBar();
		ab.setTitle(selectedPlace.getName());
		
		placeId = selectedPlace.getPlcaeId();
		textViewPlaceName.setText(selectedPlace.getName());
		textViewDistance.setText(selectedPlace.getDistance() + " KM");
		 Log.i("Place Selected", selectedPlace.getPlcaeId());
		 parser = new JSONParser();

	
		 new placeDetails().execute();
	}// End onCreate()

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}// end onOptionItemSelected()
	
	class placeDetails extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			
			super.onPreExecute();
		}
		@Override
		protected Void doInBackground(Void... arg0) {List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("place_id", placeId));
		jsonObj = parser.makeHttpRequest(placeDetailURL, "POST", params);

		Log.i("Place Json String", jsonObj.toString());
		
		try {
			placeAddress = jsonObj.getString("address");
			placeLat = jsonObj.getDouble("latitude");
			placeLng = jsonObj.getDouble("longitude");
			placeRating = jsonObj.getInt("rating");
			
			selectedPlace.setLatitude(placeLat);
			selectedPlace.setLongitude(placeLng);
			
		} catch (JSONException e) {
			
			Log.e("Detail thread error", "Thread error", e);
		}
		// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			textViewAddress.setText(placeAddress);
			ratingBar.setProgress(placeRating);
			buttonFindPlace.setEnabled(true);
			super.onPostExecute(result);
		}
		
	}//End Async Task

}//End Activity
