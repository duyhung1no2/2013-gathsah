package com.duyhung.hashtagnote;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.duyhung.hashtagnote.adapter.HashtagListAdapter;
import com.duyhung.hashtagnote.adapter.SeparatedListAdapter;
import com.duyhung.hashtagnote.dao.HashtagDAO;
import com.duyhung.hashtagnote.model.Hashtag;

public class HashtagListFragment extends Fragment {

	private ListView listViewHashtag;

	private HashtagDAO hashtagDAO;
	private List<List<Hashtag>> listHashtag;
	
	private HashtagItemClickListener handler;
	private int currentPositionNote = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_hashtag_list, container, false);

		hashtagDAO = new HashtagDAO(getActivity());

		listViewHashtag = (ListView) view.findViewById(R.id.listViewHashtag);
		loadListHashtag();
		
		handler = (HashtagItemClickListener) getActivity();

		return view;
	}

	@Override
	public void onPause() {
		super.onPause();
		currentPositionNote = listViewHashtag.getFirstVisiblePosition();
	}

	@Override
	public void onResume() {
		super.onResume();
		listViewHashtag.setSelection(currentPositionNote);
	}

	private OnItemClickListener onHashtagItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
			Hashtag tag = (Hashtag) listView.getAdapter().getItem(position);
			listViewHashtag.setItemChecked(position, true);
			onPause();
			handler.onHashtagItemClick(tag);
			onResume();
		}
	};

	@SuppressLint("DefaultLocale")
	public void loadListHashtag() {
		listHashtag = hashtagDAO.getMultiList();
		SeparatedListAdapter adapter = new SeparatedListAdapter(getActivity(), R.layout.listview_header_hashtag);
		
		for(List<Hashtag> list : listHashtag){
			String caption = list.get(0).getTag().substring(1, 2).toUpperCase();
			if(caption.matches("[0-9]"))
				caption = "0-9";
			adapter.addSection(caption, new HashtagListAdapter(getActivity(), R.layout.listview_item_hashtag, list));
		}
		
		listViewHashtag.setAdapter(adapter);
		listViewHashtag.setOnItemClickListener(onHashtagItemClickListener);
	}

	interface HashtagItemClickListener{
		void onHashtagItemClick(Hashtag tag);
	}
	
}