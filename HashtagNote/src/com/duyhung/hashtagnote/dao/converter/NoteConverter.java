package com.duyhung.hashtagnote.dao.converter;

import java.text.ParseException;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;

import com.duyhung.hashtagnote.dao.DB;
import com.duyhung.hashtagnote.model.Note;

public class NoteConverter implements IConverter<Note> {

	@Override
	public ContentValues convertModelToContentValues(Note obj) {
		ContentValues cv = new ContentValues();

		cv.put(DB.NOTE_ID, obj.getId());
		cv.put(DB.NOTE_TITLE, obj.getTitle());
		cv.put(DB.NOTE_CONTENT, obj.getContent());
		cv.put(DB.NOTE_CREATE, obj.getCreate() == null ? null : DB.DB_DATE_FORMAT.format(obj.getCreate()));
		cv.put(DB.NOTE_MODIFY, obj.getModify() == null ? null : DB.DB_DATE_FORMAT.format(obj.getModify()));

		return cv;
	}

	@Override
	public Note convertCursorToModel(Cursor cs) {

		long id = cs.getLong(cs.getColumnIndex(DB.NOTE_ID));
		String title = cs.getString(cs.getColumnIndex(DB.NOTE_TITLE));
		String content = cs.getString(cs.getColumnIndex(DB.NOTE_CONTENT));
		try {
			Date create = DB.DB_DATE_FORMAT.parse(cs.getString(cs.getColumnIndex(DB.NOTE_CREATE)));
			Date modify = DB.DB_DATE_FORMAT.parse(cs.getString(cs.getColumnIndex(DB.NOTE_MODIFY)));
			return new Note(id, title, content, create, modify);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return null;
	}

}
