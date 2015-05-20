package com.example.track_me.others;

public class GridImage {
	
	int imageId;
	public String imageName;
	
	public GridImage(int imageId,String imageName){
		this.imageId = imageId;
		this.imageName = imageName;
	}
	
	public int getImageId(){
		return imageId;
	}
	
	public void setImageId(int imageId){
		this.imageId = imageId;
	}
	
	public String getImageName(){
		return imageName;
	}
	
	public void setImageName(String imageName){
		this.imageName = imageName;
	}

}
