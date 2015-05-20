package com.example.track_me;


import com.example.track_me.others.ListViewImage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

public class DrawerActivity extends Activity {
	
	DrawerLayout mainLayout;
	FrameLayout mainContent;
	ListView drawerList;
	private String[] ListItems;
	ActionBarDrawerToggle drawerListener;
    private float lastTranslate = 0.0f;
    
	Integer[] imageId = {
			R.drawable.home,
			R.drawable.home,
			R.drawable.location,
			R.drawable.profile,
			R.drawable.favorites,
			R.drawable.help,
			R.drawable.help,
			R.drawable.logout	
	};

	@Override
	public void setContentView(final int layoutID) {
		
		ListItems = getResources().getStringArray(R.array.items);
		mainLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer, null);
		mainContent = (FrameLayout) mainLayout.findViewById(R.id.mainContent);
		drawerList = (ListView) mainLayout.findViewById(R.id.drawerList);

		
		ListViewImage adapter = new ListViewImage(DrawerActivity.this,ListItems,imageId);
		drawerList.setAdapter(adapter);
		drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                  selectItem(position);
            }
		});

		getLayoutInflater().inflate(layoutID, mainContent, true);
		super.setContentView(mainLayout);
	} //end setContentView
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayout());

		drawerListener = new ActionBarDrawerToggle(
				this, 
				mainLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			//@SuppressLint("NewApi")
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				float moveFactor = (drawerList.getWidth() * slideOffset);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mainContent.setTranslationX(moveFactor);
				} 
				else {
					TranslateAnimation anim = new TranslateAnimation(
							lastTranslate, moveFactor, 0.0f, 0.0f);
					anim.setDuration(0);
					anim.setFillAfter(true);
					mainContent.startAnimation(anim);

					lastTranslate = moveFactor;
				}
			} // end onDrawerSlide

			@Override
			public void onDrawerClosed(View drawerView) {
			}

			@Override
			public void onDrawerOpened(View drawerView) {
			}
		}; // end drawerListener
		
		mainLayout.setDrawerListener(drawerListener);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}// End onCreate

	protected int getLayout() {
		return R.layout.content_layout;
	}
	
	public void selectItem(int position){
		
		Intent intent = null;
	    drawerList.setItemChecked(position, true);
	    mainLayout.closeDrawer(drawerList);
		switch(position){
			case 0 :
				intent = new Intent(this,HomeActivity.class);
				startActivity(intent);
				break;
			
			case 1 :
				intent = new Intent(this,MainMenu.class);
				startActivity(intent);
				break;
				
			case 2 :
				intent = new Intent(this,LocationActivity.class);
				startActivity(intent);
				break;
				
			case 3 :
				intent = new Intent(this,ProfileActivity.class);
				startActivity(intent);
				break;
			case 4:
				intent = new Intent(this,MenuDetailsActivity.class);
				startActivity(intent);
				break;
			
			case 5 :
				intent = new Intent(this,EmergencyActivity.class);
				startActivity(intent);
				break;
				
			default:
				intent = new Intent(DrawerActivity.this, HomeActivity.class);
				startActivity(intent);
				break;
    		
    	} // end switch
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
	}
}
