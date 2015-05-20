package com.example.track_me;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class LocationActivity extends FragmentActivity implements LocationListener, OnClickListener {

	private GoogleMap gmap;
	private Button normal;
	private Circle mCircle;
	Location myLocation;
	Criteria criteria;
	LocationManager locationManager;
	
	private static int mapType = GoogleMap.MAP_TYPE_NORMAL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	
		ImageView view = (ImageView)findViewById(android.R.id.home);
		// left, top, right, bottom
		view.setPadding(5, 0, 2, 0);
		
		normal = (Button)findViewById(R.id.btnNrmal);
		normal.setOnClickListener(this);
		
		
		setUpMapIfNeeded();	
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setUpMapIfNeeded(){
		if (gmap == null){
			gmap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
			
			if(gmap != null){
				setUpMap();
			}
		}	
	}// end setUpMapIfNeeded

	private void setUpMap() {
		//gmap.setMyLocationEnabled(true);
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, true);
		myLocation = locationManager.getLastKnownLocation(provider);
		gmap.setMapType(mapType);
		
		if(myLocation!=null){
			//onLocationChanged(myLocation);
			double latitude = myLocation.getLatitude();
			double longitude = myLocation.getLongitude();
			
			LatLng latLng = new LatLng(latitude,longitude);
			
			gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

			gmap.animateCamera(CameraUpdateFactory.zoomTo(12));
			gmap.addMarker(new MarkerOptions()
			.position(latLng)
			.title("I am Here")
			.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
		}else{
			Toast.makeText(this, "Location Not Available", Toast.LENGTH_LONG).show();
		}		
	} //end setUpMap

	@Override
	public void onLocationChanged(Location location) {
		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		
		LatLng latLng = new LatLng(latitude,longitude);
		
		gmap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		gmap.animateCamera(CameraUpdateFactory.zoomTo(12));
		gmap.addMarker(new MarkerOptions()
		.position(new LatLng(latitude,longitude))
		.title("I am Here")
		.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker)));
	}
	
	@Override
	public void onProviderEnabled(String Provider){
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this, "GPS Disabled", Toast.LENGTH_SHORT).show();	
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	} 

	@Override
	public void onClick(View v) {
		
		if(mapType == GoogleMap.MAP_TYPE_NORMAL){
			mapType= GoogleMap .MAP_TYPE_SATELLITE;
			gmap.setMapType(mapType);
			normal.setText("Normal");
			
		}
		else{
			mapType= GoogleMap .MAP_TYPE_NORMAL;
			gmap.setMapType(mapType);
			normal.setText("Satellite");
		}

	}
}
