package com.example.track_me.others;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class MyLocation implements LocationListener {
	Context context;
	LocationManager locationManager;
	Criteria criteria = new Criteria();
	String provider;
	Location location;

	public MyLocation(Context context, LocationManager locationManager) {
		this.context = context;
		this.locationManager = locationManager;
		this.provider = this.locationManager.getBestProvider(criteria, true);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				2000, 10, this);
		location = locationManager.getLastKnownLocation(provider);
	}// end constructor

	public double getLatitude() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				2000, 10, this);
		location = locationManager.getLastKnownLocation(provider);
		if (location == null)
			return 0.0;
		return location.getLatitude();
	}// End getLatitude

	public double getLongitude() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				2000, 10, this);
		location = locationManager.getLastKnownLocation(provider);
		if (location == null)
			return 0.0;
		return location.getLongitude();
	}

	public Location getLocation() {
		if(location == null){
			Location loc = new Location("");
			loc.setLatitude(0.0);
			loc.setLongitude(0.0);
			
			return loc;
					
		}
		return this.location;
	}

	@Override
	public void onLocationChanged(android.location.Location location) {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				2000, 10, this);
		location = locationManager.getLastKnownLocation(provider);

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

}// end class Location
