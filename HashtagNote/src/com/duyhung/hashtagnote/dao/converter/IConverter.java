package com.duyhung.hashtagnote.dao.converter;

import android.content.ContentValues;
import android.database.Cursor;

public interface IConverter<T> {
	
	public ContentValues convertModelToContentValues(T obj);
		
	public T convertCursorToModel(Cursor cs);

}
