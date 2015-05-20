package com.example.track_me.entity;

import android.graphics.Bitmap;

public class Friends {
	
	private String name, thumbnailUrl,num;
	private Bitmap image;
	private double lat,lng;
	private double distance;
 
    public Friends(String name, String thumbnailUrl,Bitmap image, String num) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.image = image;
        this.num = num;
    }
    
    public Friends(String num,double lat,double lng, double distance){
    	this.lat = lat;
    	this.lng = lng;
    	this.distance = distance;
    	this.num = num;
    }
    
    

    public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
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
    
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnailUrl) {
		this.thumbnailUrl = thumbnailUrl;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
 
  
}
