package com.example.track_me.others;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.track_me.R;
import com.example.track_me.entity.Place;
import com.google.android.gms.maps.model.LatLng;

public class CustomList extends BaseAdapter {

	private static List<Place> list;
	LocationManager locationManager;
	Context context;
	

	LatLng myLatLng, placeLatLng;

	private Integer[] imgid = { R.drawable.kfc, R.drawable.home,
			R.drawable.favorites };

	private LayoutInflater l_Inflater;

	public CustomList(Context context, List<Place> results,
			LocationManager locationManager) {

		this.context = context;
		list = results;
		this.locationManager = locationManager;
		l_Inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = l_Inflater.inflate(R.layout.menu_row, null);
			holder = new ViewHolder();
			holder.txt_itemName = (TextView) convertView
					.findViewById(R.id.txt_Name);
			holder.txt_itemLocation = (TextView) convertView
					.findViewById(R.id.txt_Location);
			holder.itemImage = (ImageView) convertView
					.findViewById(R.id.imageView1);
			holder.txt_descr = (TextView) convertView
					.findViewById(R.id.txt_description);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txt_itemName.setText(list.get(position).getName());
		
		MyLocation loc = new MyLocation(context, locationManager);
		
		
		Place currentPlace = list.get(position);
		
		ArrayList<String> typeList = currentPlace.getTypes();
		String typeStr ="";
		int typeSize = typeList.size();
		for(int i =0;i<4;i++){
			if(i<typeSize){
				typeStr +=typeList.get(i)+", ";
				
			}
		}
		
		int commaIndex = typeStr.lastIndexOf(',');
		StringBuilder sb = new StringBuilder(typeStr);
		sb.deleteCharAt(commaIndex);
		typeStr = sb.toString();
		Bitmap image = currentPlace.getImage();
		image = Bitmap.createScaledBitmap(image, 70	, 70, false);
		holder.txt_descr.setText(typeStr);
		holder.txt_itemLocation.setText(currentPlace.getDistance() + " KM");
		holder.itemImage.setImageBitmap(image);
		//holder.itemImage.setImageResource(R.drawable.kfc);
		
		return convertView;
	}

	static class ViewHolder {
		TextView txt_itemName;
		TextView txt_itemLocation;
		ImageView itemImage;
		TextView txt_descr;
	}
}
