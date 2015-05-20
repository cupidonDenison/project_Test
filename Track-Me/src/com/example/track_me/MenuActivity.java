package com.example.track_me;

import java.util.List;

import org.w3c.dom.Document;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.track_me.entity.Place;
import com.example.track_me.others.CustomList;
import com.example.track_me.others.JSONParser;
import com.google.gson.Gson;

public class MenuActivity extends DrawerActivity {

	ListView menuList;
	static String type;
	private static List<Place> places;
	static LocationManager locationmanger;
	JSONParser parser = new JSONParser();
	static Document mapDoc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_menu);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		
		

		Intent i = getIntent();
		type = i.getStringExtra("Type");

		type= HomeActivity.getType();
		
		locationmanger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		
		ActionBar ab = getActionBar();
		ab.setTitle(type);

	//	Log.i("response type", type);
		menuList = (ListView) findViewById(R.id.listView1);
		
		places = HomeActivity.placesList;
		menuList.setAdapter(new CustomList(MenuActivity.this, places,locationmanger));
		
		menuList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> a, View v, int position,
					long id) {
				Object o = menuList.getItemAtPosition(position);
				Place obj_itemDetails = (Place) o;
				String myGson = new Gson().toJson(obj_itemDetails);
				
				

				Intent intent = new Intent(MenuActivity.this,
						MenuDetailsActivity.class);
				intent.putExtra("selectedPlace", myGson);
				startActivity(intent);
				
				
				
			}
		});

		
	}//End onCreate()
	
	

	@Override
	protected int getLayout() {
		// TODO Auto-generated method stub
		return R.layout.activity_menu;
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	//Async task to retrieve xml document from google api
	class GoogleMapDoc extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... arg0) {
			//mapDoc = parser.getDocument(myLatLng, placeLatLng, "");
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(mapDoc !=null){
				
				 Log.i("DistanceValue","xml doc is null");
			}
		}
	}//end GoogleMapDoc
	

	
}// end Actiiyt
