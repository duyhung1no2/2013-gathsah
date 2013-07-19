package com.duyhung.hashtagnote.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.duyhung.hashtagnote.R;
import com.duyhung.hashtagnote.dao.HashtagDAO;
import com.duyhung.hashtagnote.model.Hashtag;

public class HashtagListAdapter extends ArrayAdapter<Hashtag> {

	HashtagDAO dao;
	LayoutInflater inflater;
	List<Hashtag> list;
	private Context mContext;
	
	public HashtagListAdapter(Context context, int textViewResourceId, List<Hashtag> objects) {
		super(context, textViewResourceId, objects);
		inflater = LayoutInflater.from(context);
		dao = new HashtagDAO(context);
		list = objects;
		mContext = context;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		view = inflater.inflate(R.layout.listview_item_hashtag, null);
			
		TextView tvHashtag = (TextView) view.findViewById(R.id.tvHashtagName);
		TextView tvNumberOfNote = (TextView) view.findViewById(R.id.tvHashtagNumberOfNote);
		
		Hashtag tag = list.get(position);
		tvHashtag.setText(tag.getTag());
		tvNumberOfNote.setText(dao.getNumberOfNote(tag)+"");
		
		tvHashtag.setTextColor(mContext.getResources().getColor(android.R.color.white));
		tvNumberOfNote.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
		return view;
	}
}
