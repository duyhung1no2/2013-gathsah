package com.duyhung.hashtagnote.dao;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;

import com.duyhung.hashtagnote.dao.converter.HashTagConverter;
import com.duyhung.hashtagnote.model.Hashtag;
import com.duyhung.hashtagnote.model.TagDetail;

public class HashtagDAO extends DatabaseDAO<Hashtag> {
	Context mContext;

	public HashtagDAO(Context context) {
		super(context);
		converter = new HashTagConverter();
		mContext = context;
	}

	@Override
	public Cursor getAllCursor() {
		openDB();
		return db.rawQuery("SELECT H.[_id], Tag FROM Hashtag H" 
						+ " INNER JOIN TagDetail D ON H.[_id] == D.TagId" 
						+ " GROUP BY H.[_id], Tag" 
						+ " ORDER BY COUNT(*) DESC", new String[]{});
	}

	@Override
	public List<Hashtag> getAll() {
		List<Hashtag> tags = new ArrayList<Hashtag>();
		openDB();
		Cursor cs = db.rawQuery("SELECT * FROM " + DB.TABLE_HASHTAG + " ORDER BY " + DB.HASHTAG_TAG + " COLLATE NOCASE", new String[]{});
		for(cs.moveToFirst(); !cs.isAfterLast(); cs.moveToNext()){
			tags.add(converter.convertCursorToModel(cs));
		}
		
		closeDB();
		return tags;
	}
	
	@SuppressLint("DefaultLocale")
	public List<List<Hashtag>> getMultiList(){
		List<List<Hashtag>> multiList = new ArrayList<List<Hashtag>>();
		List<Hashtag> hashtags = getAll();
		
		String currentAnpha = "[0-9]";
		List<Hashtag> sameStartWith = new ArrayList<Hashtag>();
		
		for(Hashtag tag : hashtags){			
			String firstChar = tag.getTag().substring(1, 2).toLowerCase();
			
			if(firstChar.matches(currentAnpha)){
				sameStartWith.add(tag);
			} else{
				currentAnpha = firstChar;
				if (sameStartWith.size() > 0) 
					multiList.add(sameStartWith);
				sameStartWith = null;
				sameStartWith = new ArrayList<Hashtag>();
				sameStartWith.add(tag);
			}
			
			if (hashtags.indexOf(tag) + 1 == hashtags.size()) {
				multiList.add(sameStartWith);
			}

		}
		return multiList;
		
	}

	@Override
	public Hashtag get(long id) {
		openDB();
		
		Cursor cs = db.query(true, DB.TABLE_HASHTAG, new String[]{}, DB.HASHTAG_ID + " = " + id, null, null, null, null, null);
		cs.moveToFirst();
		Hashtag tag = converter.convertCursorToModel(cs);
		
		closeDB();
		return tag;
	} 
	
	public int getNumberOfNote(Hashtag tag){
		int total = 0;
		for(TagDetail d : new TagDetailDAO(mContext).getAll()){
			if(d.getTagId() == tag.getId())
				total++;
		}
		return total;
	}
	
	public long getIdByTag(String tagName){
		for(Hashtag tag : getAll()){
			if(tag.getTag().equals(tagName))
				return tag.getId();
		}
		return 0;
	}
	
	public long getNextTagId() {
		long id = 0;
		openDB();
		Cursor cs = db.rawQuery("SELECT * FROM " + DB.TABLE_HASHTAG + " ORDER BY " + DB.HASHTAG_ID + " DESC LIMIT 1", new String[]{});
		if(cs.moveToFirst()){
			id = converter.convertCursorToModel(cs).getId();
		}
		closeDB();
		return ++id;
	}

	@Override
	public void insert(Hashtag obj) {
		openDB();
		db.insert(DB.TABLE_HASHTAG, null, converter.convertModelToContentValues(obj));
		closeDB();
	}

	@Override
	public void delete(Hashtag obj) {
		openDB();
		db.delete(DB.TABLE_HASHTAG, DB.HASHTAG_ID + "=" + obj.getId(), null);
		closeDB();
	}

	@Override
	public void update(Hashtag obj) {
		openDB();
		db.update(DB.TABLE_HASHTAG, converter.convertModelToContentValues(obj), DB.HASHTAG_ID + "=" + obj.getId(), null);
		closeDB();
	}

}
