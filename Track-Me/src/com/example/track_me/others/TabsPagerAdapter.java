package com.example.track_me.others;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.track_me.FragmentFriends;
import com.example.track_me.FragmentNearby;
import com.example.track_me.NearbyEventsFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		switch (index) {
		case 0:
			return new FragmentNearby();
		case 1:
			return new FragmentFriends();
		case 2:
			return new NearbyEventsFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		return 3;
	}

}