package com.duyhung.hashtagnote.adapter;

import java.util.ArrayList;
import java.util.List;

import com.duyhung.hashtagnote.HashtagListFragment;
import com.duyhung.hashtagnote.NoteListFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ListPagerAdapter extends FragmentPagerAdapter {
	
	List<Fragment> frags;

	public ListPagerAdapter(FragmentManager fm) {
		super(fm);
		frags = new ArrayList<Fragment>();
		frags.add(new HashtagListFragment());
		frags.add(new NoteListFragment());
	}

	@Override
	public Fragment getItem(int position) {
		return frags.get(position);
	}
	
	@Override
	public float getPageWidth(int position) {
		if(position == 0){
			return 0.5f;
		}
		return 1f;
	}

	@Override
	public int getCount() {
		return 2;
	}

}
