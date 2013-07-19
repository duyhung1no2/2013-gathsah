package com.duyhung.hashtagnote.dao;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat") public class DB {

	public static final String DB_NAME = "HashtagNoteDB";
	public static final int DB_VERSION = 33;

	public static final String TABLE_NOTE = "Note";
	public static final String NOTE_ID = "_id";
	public static final String NOTE_TITLE = "Title";
	public static final String NOTE_CONTENT = "Content";
	public static final String NOTE_CREATE = "CreateDate";
	public static final String NOTE_MODIFY = "ModifyDate";

	public static final String TABLE_HASHTAG = "HashTag";
	public static final String HASHTAG_ID = "_id";
	public static final String HASHTAG_TAG = "Tag";

	public static final String TABLE_TAGDETAIL = "TagDetail";
	public static final String TAGDETAIL_ID = "_id";
	public static final String TAGDETAIL_NOTE_ID = "NoteId";
	public static final String TAGDETAIL_TAG_ID = "TagId";
	
	public static final SimpleDateFormat DB_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static final SimpleDateFormat DISPLAY_DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

}
