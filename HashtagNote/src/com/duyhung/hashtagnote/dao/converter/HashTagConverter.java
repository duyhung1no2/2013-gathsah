package com.duyhung.hashtagnote.dao.converter;

import android.content.ContentValues;
import android.database.Cursor;

import com.duyhung.hashtagnote.dao.DB;
import com.duyhung.hashtagnote.model.Hashtag;

public class HashTagConverter implements IConverter<Hashtag> {

	@Override
	public ContentValues convertModelToContentValues(Hashtag obj) {
		ContentValues cv = new ContentValues();
		cv.put(DB.HASHTAG_ID, obj.getId());
		cv.put(DB.HASHTAG_TAG, obj.getTag());
		return cv;
	}

	@Override
	public Hashtag convertCursorToModel(Cursor cs) {
		return new Hashtag(cs.getLong(cs.getColumnIndex(DB.HASHTAG_ID)), cs.getString(cs.getColumnIndex(DB.HASHTAG_TAG)));
	}

}
