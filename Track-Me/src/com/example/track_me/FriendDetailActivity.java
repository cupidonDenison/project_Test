package com.example.track_me;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class FriendDetailActivity extends Activity {
	
	ImageView profilePic,phone;
	TextView number;
	
	Bitmap bmp;
	String fname,fnum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_detail);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);	
		getActionBar().setIcon(
				   new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		
		Intent i = getIntent();
		fname = i.getStringExtra("name");
		getActionBar().setTitle(fname);	
		
		
		fnum =  i.getStringExtra("num");
		number = (TextView)findViewById(R.id.tvnumber);
		number.setText(fnum);
		
		bmp = (Bitmap) i.getParcelableExtra("image");
		profilePic = (ImageView)findViewById(R.id.profileImage);
		profilePic.setImageBitmap(bmp); 
		
		phone = (ImageView)findViewById(R.id.phone);
		phone.setOnClickListener(new OnClickListener() {
			
			@Override			
			public void onClick(View view) {			
				makePhone(fnum);
			}	
		});
	}
	
	
	public void makePhone(String numb){
		Intent sIntent = new Intent(Intent.ACTION_CALL, Uri
  		      .parse("tel:"+numb));
  		    startActivity(sIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_detail, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		
		if (id == android.R.id.home) {
			Intent intent = new Intent(this, PeopleActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
