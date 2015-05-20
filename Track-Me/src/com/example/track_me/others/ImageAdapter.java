package com.example.track_me.others;

import com.example.track_me.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {

	private Activity mContext;
	
	public ImageAdapter(Activity c) {
		mContext = c; 
	}

	public int getCount() {
		return mThumbIds.length;
	}

	public Object getItem(int position) {
		return null;
	}
	
	public long getItemId(int position) {
	return 0;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
	 ImageView imageView;
	    if (convertView == null) {  // if it's not recycled, initialize some attributes
	        imageView = new ImageView(mContext);
	        imageView.setLayoutParams(new GridView.LayoutParams(50, 50));
	        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	        imageView.setPadding(4, 4, 4, 4);
	        
	        
	    } else {
	        imageView = (ImageView) convertView;
	    }
	
	    imageView.setImageResource(mThumbIds[position]);
	    return imageView;
	}
	
	private Integer[] mThumbIds = {
	    R.drawable.ic_doctor , R.drawable.ic_doctor,
	    R.drawable.ic_doctor , R.drawable.ic_doctor,
	    R.drawable.ic_doctor 
	};
}