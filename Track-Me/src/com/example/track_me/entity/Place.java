package com.example.track_me.entity;

import java.util.ArrayList;

import android.graphics.Bitmap;

public class Place {
	String placeId;
	String category;
	ArrayList<String> types;
	public ArrayList<String> getTypes() {
		return types;
	}



	public void setTypes(ArrayList<String> types) {
		this.types = types;
	}
	String name;
	String icon;
	double latitude;
	double longitude;
	Bitmap image;
	double distance;
	
	
	
	
	public Place(String placeId, String category, String name,String icon, double latitude,
			double longitude) {
		super();
		//types = new ArrayList<String>();
		this.placeId = placeId;
		this.category = category;
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.icon =icon;
	}
	
	
	
	public double getDistance() {
		return distance;
	}



	public void setDistance(double distance) {
		this.distance = distance;
	}



	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
	public String getPlcaeId() {
		return placeId;
	}
	public void setPlcaeId(String plcaeId) {
		this.placeId = plcaeId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	


	


	public String getIcon() {
		return icon;
	}



	public void setIcon(String icon) {
		this.icon = icon;
	}



	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	 
	
	
	
	

}//End class Place
