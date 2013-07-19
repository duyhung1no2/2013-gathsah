package com.duyhung.hashtagnote.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.duyhung.hashtagnote.dao.converter.TagDetailConverter;
import com.duyhung.hashtagnote.model.TagDetail;

public class TagDetailDAO extends DatabaseDAO<TagDetail>{

	public TagDetailDAO(Context context) {
		super(context);
		converter = new TagDetailConverter();
	}

	@Override
	public Cursor getAllCursor() {
		openDB();
		return db.rawQuery("SELECT * FROM " + DB.TABLE_TAGDETAIL, new String[]{});
	}

	@Override
	public List<TagDetail> getAll() {
		openDB();
		List<TagDetail> details = new ArrayList<TagDetail>();
		
		Cursor cs = db.query(true, DB.TABLE_TAGDETAIL, new String[]{}, null, null, null, null, null, null);
		
		for(cs.moveToFirst(); !cs.isAfterLast(); cs.moveToNext()){
			details.add(converter.convertCursorToModel(cs));
		}
		
		closeDB();
		return details;
	}

	@Override
	public TagDetail get(long id) {
		openDB();
		Cursor cs = db.query(true, DB.TABLE_TAGDETAIL, new String[]{}, null, null, null, null, null, null);
		cs.moveToFirst();
		TagDetail detail = converter.convertCursorToModel(cs);
		
		closeDB();
		return detail;
	}

	@Override
	public void insert(TagDetail obj) {
		openDB();
		db.insert(DB.TABLE_TAGDETAIL, null, converter.convertModelToContentValues(obj));
		closeDB();
	}

	@Override
	public void delete(TagDetail obj) {
		openDB();
		db.delete(DB.TABLE_TAGDETAIL, DB.TAGDETAIL_ID + "=" + obj.getId(), null);
		closeDB();
	}

	@Override
	public void update(TagDetail obj) {
		openDB();
		db.update(DB.TABLE_TAGDETAIL, converter.convertModelToContentValues(obj), DB.TAGDETAIL_ID + "=" + obj.getId(), null);
		closeDB();
	}

}
