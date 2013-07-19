package com.duyhung.hashtagnote.dao.converter;

import android.content.ContentValues;
import android.database.Cursor;

import com.duyhung.hashtagnote.dao.DB;
import com.duyhung.hashtagnote.model.TagDetail;

public class TagDetailConverter implements IConverter<TagDetail> {

	@Override
	public ContentValues convertModelToContentValues(TagDetail obj) {
		ContentValues cv = new ContentValues();
		cv.putNull(DB.TAGDETAIL_ID);
		cv.put(DB.TAGDETAIL_NOTE_ID, obj.getNoteId());
		cv.put(DB.TAGDETAIL_TAG_ID, obj.getTagId());
		return cv;
	}

	@Override
	public TagDetail convertCursorToModel(Cursor cs) {
		return new TagDetail(cs.getLong(cs.getColumnIndex(DB.TAGDETAIL_ID)), 
				cs.getLong(cs.getColumnIndex(DB.TAGDETAIL_NOTE_ID)), 
				cs.getLong(cs.getColumnIndex(DB.TAGDETAIL_TAG_ID)));
	}

}
