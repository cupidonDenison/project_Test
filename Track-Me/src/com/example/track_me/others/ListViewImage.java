package com.example.track_me.others;

import com.example.track_me.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewImage extends ArrayAdapter<String> {

	private final Activity context;
	private final String[] listTitles;
	private final Integer[] imageId;
	
	public ListViewImage(Activity context,
				String[] listTitles, 
				Integer[] imageId) {
		super(context, R.layout.custom_drawer_row, listTitles);
		this.context = context;
		this.listTitles = listTitles;
		this.imageId = imageId;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.custom_drawer_row, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
		txtTitle.setText(listTitles[position]);
		imageView.setImageResource(imageId[position]);
		return rowView;
	}
}
