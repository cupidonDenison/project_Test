package com.example.track_me.entity;

import com.google.android.gms.maps.model.LatLng;

public class Contact {
	
	private String name;
	private String phoneNumber;
	private boolean isFriend;
	private LatLng location;
	private String pictureLocation;
	
	
	
	public String getPictureLocation() {
		return pictureLocation;
	}


	public void setPictureLocation(String pictureLocation) {
		this.pictureLocation = pictureLocation;
	}


	public Contact() {
		super();
	}
	
	
	public Contact(String name, String phoneNumber, boolean isFriend,
			LatLng location) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.isFriend = isFriend;
		this.location = location;
	}

	

	public Contact(String name, String phoneNumber) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	


	public Contact(String name, String phoneNumber, boolean isFriend) {
		super();
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.isFriend = isFriend;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean isFriend() {
		return isFriend;
	}
	public void setFriend(boolean isFriend) {
		this.isFriend = isFriend;
	}
	public LatLng getLocation() {
		return location;
	}
	public void setLocation(LatLng location) {
		this.location = location;
	}
	
	

}
