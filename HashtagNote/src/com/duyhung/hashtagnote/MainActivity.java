package com.duyhung.hashtagnote;

import android.annotation.SuppressLint;
import java.util.*;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.duyhung.hashtagnote.HashtagListFragment.HashtagItemClickListener;
import com.duyhung.hashtagnote.NoteListFragment.NoteContextItemSelectedListener;
import com.duyhung.hashtagnote.adapter.ListPagerAdapter;
import com.duyhung.hashtagnote.model.Hashtag;

public class MainActivity extends FragmentActivity implements HashtagItemClickListener, NoteContextItemSelectedListener {

	private ActionBar actionBar;
	private ViewPager pager;

	private SearchView searchView;
	private String currentSearch = "";

	private NoteListFragment noteListFragment;
	private HashtagListFragment hashtagListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getView();
	}

	@SuppressLint("NewApi")
	private void getView() {

		actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		setActionBarTitle();

		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ListPagerAdapter(getSupportFragmentManager()));
		pager.setCurrentItem(1);

		hashtagListFragment = (HashtagListFragment) ((ListPagerAdapter) pager.getAdapter()).getItem(0);
		noteListFragment = (NoteListFragment) ((ListPagerAdapter) pager.getAdapter()).getItem(1);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		searchView = (SearchView) menu.findItem(R.id.action_note_search).getActionView();
		searchView.setOnQueryTextListener(onQueryTextListener);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.note_detail, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		switch (item.getItemId()) {
		case android.R.id.home:
			currentSearch = "";
			searchNote();
			break;

		case R.id.action_note_add:
			Intent i = new Intent(this, NoteAddActivity.class);
			startActivityForResult(i, 0);
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		currentSearch = "";
		searchNote();
		hashtagListFragment.loadListHashtag();
	}

	@Override
	public void onBackPressed() {
		if (!currentSearch.equals("")) {
			currentSearch = "";
			searchNote();
		} else {
			super.onBackPressed();
		}
	}

	@SuppressLint("NewApi")
	OnQueryTextListener onQueryTextListener = new OnQueryTextListener() {

		@Override
		public boolean onQueryTextSubmit(String query) {
			currentSearch = query;
			searchNote();
			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			currentSearch = newText;
			searchNote();
			return false;
		}
	};

	@Override
	public void onHashtagItemClick(Hashtag tag) {
		currentSearch = tag.getTag().substring(1);
		searchNote();
	}

	@Override
	public void refreshList() {
		hashtagListFragment.onPause();
		hashtagListFragment.loadListHashtag();
		hashtagListFragment.onResume();
	}

	private void searchNote() {
		noteListFragment.search(currentSearch);
		hashtagListFragment.onPause();
		hashtagListFragment.loadListHashtag();
		hashtagListFragment.onResume();
		setActionBarTitle();
		pager.setCurrentItem(1);
	}

	@SuppressLint("NewApi")
	private void setActionBarTitle() {
		if (currentSearch.equals("")) {
			actionBar.setTitle(getResources().getString(R.string.app_name));
			actionBar.setHomeButtonEnabled(false);
			actionBar.setDisplayHomeAsUpEnabled(false);
			return;
		}

		actionBar.setTitle("#" + currentSearch);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

}
