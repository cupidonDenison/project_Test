package com.example.drawerexample;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class MainActivity extends Activity {

	DrawerLayout mainLayout;
	FrameLayout mainContent;
	ListView drawerList;
	private String[] planets;
	ActionBarDrawerToggle drawerListener;
	
    private float lastTranslate = 0.0f;

	@Override
	public void setContentView(final int layoutID) {
		planets = getResources().getStringArray(R.array.planets);
		mainLayout = (DrawerLayout) getLayoutInflater().inflate(
				R.layout.activity_main, null);
		mainContent = (FrameLayout) mainLayout.findViewById(R.id.mainContent);

		drawerList = (ListView) mainLayout.findViewById(R.id.drawerList);

		drawerList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, planets));

		getLayoutInflater().inflate(layoutID, mainContent, true);
		super.setContentView(mainLayout);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getLayout());

		drawerListener = new ActionBarDrawerToggle(this, mainLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {

			//@SuppressLint("NewApi")
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				float moveFactor = (drawerList.getWidth() * slideOffset);

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					mainContent.setTranslationX(moveFactor);
				} else {
					TranslateAnimation anim = new TranslateAnimation(
							lastTranslate, moveFactor, 0.0f, 0.0f);
					anim.setDuration(0);
					anim.setFillAfter(true);
					mainContent.startAnimation(anim);

					lastTranslate = moveFactor;
				}
			}

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
	}// End oncreate

	public void nextActivity(View view) {
		Intent i = new Intent(this, SecondActivity.class);
		startActivity(i);
	}

	protected int getLayout() {
		return R.layout.content_layout;
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
