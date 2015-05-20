package com.example.track_me;

import com.example.track_me.R;
import com.example.track_me.others.TabsPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;

public class MainMenu extends FragmentActivity implements ActionBar.TabListener {
	
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	 
	 private String[] tabs = { "Places", "Friends","Events"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
 
        
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        ActionBar ab = getActionBar();
        ab.setTitle("NearBy");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);       
 
        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
            
        }
        
    	/**
    	 * on swiping the viewpager make respective tab selected
    	 * */
    	viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    	 
    	    @Override
    	    public void onPageSelected(int position) {
    	        // on changing the page
    	        // make respected tab selected
    	        actionBar.setSelectedNavigationItem(position);
    	      
    	    }
    	 
    	    @Override
    	    public void onPageScrolled(int arg0, float arg1, int arg2) {
    	    }
    	 
    	    @Override
    	    public void onPageScrollStateChanged(int arg0) {
    	    }
    	});
	} // end onCreate

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		
		
        viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		//Log.e("tab selected", "Selected tab "+tab.getPosition());
		viewPager.setCurrentItem(tab.getPosition());
		
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {	
	}
}
