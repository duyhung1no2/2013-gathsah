package com.duyhung.hashtagnote.dao;

import java.util.List;

import com.duyhung.hashtagnote.dao.converter.IConverter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DatabaseDAO<T> {

	public DatabaseHelper helper;
	public SQLiteDatabase db;
	public IConverter<T> converter;

	public DatabaseDAO(Context context) {
		helper = new DatabaseHelper(context);
	}

	public void openDB() {
		db = helper.getWritableDatabase();
	}

	public void closeDB() {
		db.close();
	}

	public abstract Cursor getAllCursor();

	public abstract List<T> getAll();

	public abstract T get(long id);

	public abstract void insert(T obj);

	public abstract void delete(T obj);

	public abstract void update(T obj);

}
