package com.duyhung.hashtagnote.dao;

import java.util.Date;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	static final String createTableNote = "CREATE TABLE " + DB.TABLE_NOTE + "(" + 
																				DB.NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
																				DB.NOTE_TITLE + " TEXT," + 
																				DB.NOTE_CONTENT + " TEXT," +
																				DB.NOTE_CREATE + " DATETIME," +
																				DB.NOTE_MODIFY + " DATETIME );";
	static final String createTableHashtag = "CREATE TABLE " + DB.TABLE_HASHTAG + "(" +
																				DB.HASHTAG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
																				DB.HASHTAG_TAG + " TEXT);";
	static final String createTableTagDetail = "CREATE TABLE " + DB.TABLE_TAGDETAIL + "(" +
																				DB.TAGDETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
																				DB.TAGDETAIL_NOTE_ID + " INTEGER," + 
																				DB.TAGDETAIL_TAG_ID + " INTEGER," + 
																				"FOREIGN KEY (" + DB.TAGDETAIL_NOTE_ID + ") REFERENCES " + DB.TABLE_NOTE + "(" + DB.NOTE_ID + ")," + 
																				"FOREIGN KEY (" + DB.TAGDETAIL_TAG_ID + ") REFERENCES " + DB.TABLE_HASHTAG+ "(" + DB.HASHTAG_ID + "));";
	
	public DatabaseHelper(Context context) {
		super(context, DB.DB_NAME, null, DB.DB_VERSION);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		super.onOpen(db);
		if (!db.isReadOnly()) {
			db.execSQL("PRAGMA foreign_keys=ON;");
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createTableNote);
		db.execSQL(createTableHashtag);
		db.execSQL(createTableTagDetail);
		
		insertFirstNote(db);
				
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXIST " + DB.TABLE_TAGDETAIL);
		db.execSQL("DROP TABLE IF EXIST " + DB.TABLE_NOTE);
		db.execSQL("DROP TABLE IF EXIST " + DB.TABLE_HASHTAG);
		onCreate(db);
	}

	@Override
	public synchronized void close() {
		super.close();
	}

	@SuppressWarnings("deprecation")
	private void insertFirstNote(SQLiteDatabase db) {
		db.execSQL("INSERT INTO " + DB.TABLE_HASHTAG + " VALUES(1, '#Thank')");
		db.execSQL("INSERT INTO " + DB.TABLE_HASHTAG + " VALUES(2, '#Hashtag')");
		db.execSQL("INSERT INTO " + DB.TABLE_HASHTAG + " VALUES(3, '#Add')");
		db.execSQL("INSERT INTO " + DB.TABLE_HASHTAG + " VALUES(4, '#Edit')");
		db.execSQL("INSERT INTO " + DB.TABLE_HASHTAG + " VALUES(5, '#Delete')");
		db.execSQL("INSERT INTO " + DB.TABLE_HASHTAG + " VALUES(6, '#Note')");
		db.execSQL("INSERT INTO " + DB.TABLE_HASHTAG + " VALUES(7, '#Easy')");
		
		Date now = new Date();
		int year = now.getYear() + 1990;
		int month = now.getMonth() + 1;			
		String date = year + "-" + month + "-" + now.getDate() + " " + now.getHours() + ":" + now.getMinutes() + ":00";
		db.execSQL("INSERT INTO " + DB.TABLE_NOTE + " VALUES(1, '', '#Thank you', '"+ date +"', '"+ date +"')");
		db.execSQL("INSERT INTO " + DB.TABLE_NOTE + " VALUES(2, '', 'Slide left to show #Hashtag', '"+ date +"', '"+ date +"')");
		db.execSQL("INSERT INTO " + DB.TABLE_NOTE + " VALUES(3, '', '#Add #Edit #Delete #Note', '"+ date +"', '"+ date +"')");
		db.execSQL("INSERT INTO " + DB.TABLE_NOTE + " VALUES(4, '', '#Easy way to write #Note', '"+ date +"', '"+ date +"')");
		db.execSQL("INSERT INTO " + DB.TABLE_NOTE + " VALUES(5, '', '#Hashtag #Note Android App', '"+ date +"', '"+ date +"')");
		
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(1, 1, 1)");
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(2, 2, 2)");
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(3, 3, 3)");
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(4, 3, 4)");
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(5, 3, 5)");
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(6, 3, 6)");
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(7, 4, 7)");
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(8, 4, 6)");
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(9, 5, 2)");
		db.execSQL("INSERT INTO " + DB.TABLE_TAGDETAIL + " VALUES(10, 5, 6)");
	}
	
}
