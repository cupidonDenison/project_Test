package com.example.drawerexample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

public class SecondActivity extends MainActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}//end onCreate()

	//method  get called automatically in superclass when super.onCreate is invoked
	@Override
	protected int getLayout() {
		return R.layout.activity_second;
	}//End getLayout

}//End Activity
