package com.duyhung.hashtagnote.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.duyhung.hashtagnote.dao.converter.NoteConverter;
import com.duyhung.hashtagnote.model.Hashtag;
import com.duyhung.hashtagnote.model.Note;
import com.duyhung.hashtagnote.model.TagDetail;

public class NoteDAO extends DatabaseDAO<Note> {

	private HashtagDAO hashtagDAO;
	private TagDetailDAO tagDetailDAO;
	String pattern = "[A-Za-z0-9ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềếểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ_-]";

	public NoteDAO(Context context) {
		super(context);
		converter = new NoteConverter();
		hashtagDAO = new HashtagDAO(context);
		tagDetailDAO = new TagDetailDAO(context);
	}

	@Override
	public Cursor getAllCursor() {
		openDB();
		return db.rawQuery("SELECT * FROM " + DB.TABLE_NOTE
				+ " ORDER BY " + DB.TABLE_NOTE + "." + DB.NOTE_ID + " DESC", new String[] {});
	}

	@Override
	public List<Note> getAll() {
		openDB();
		List<Note> notes = new ArrayList<Note>();
		Cursor cs = db.rawQuery("SELECT * FROM " + DB.TABLE_NOTE
				+ " ORDER BY " + DB.TABLE_NOTE + "." + DB.NOTE_ID + " DESC", new String[] {});
		for (cs.moveToFirst(); !cs.isAfterLast(); cs.moveToNext()) {
			Note note = converter.convertCursorToModel(cs);
			notes.add(note);
		}
		
		closeDB();
		return notes;
	}
	
	@SuppressWarnings({ "deprecation" })
	public List<List<Note>> getMultiList(){
		List<List<Note>> multiList = new ArrayList<List<Note>>();
		List<Note> notes = getAll();
		int currentYear = notes.size() > 0 ? notes.get(0).getCreate().getYear() : 0;
		int currentMonth = notes.size() > 0 ? notes.get(0).getCreate().getMonth() : 0;
		List<Note> sameMonthNoteList = new ArrayList<Note>();
		
		for(Note n : notes){			
			int year = n.getCreate().getYear();
			int month = n.getCreate().getMonth();
			
			if(year == currentYear && month == currentMonth){
				sameMonthNoteList.add(n);
			} else{
				currentMonth = month;
				currentYear = year;
				multiList.add(sameMonthNoteList);
				sameMonthNoteList = null;
				sameMonthNoteList = new ArrayList<Note>();
				sameMonthNoteList.add(n);
			}
			
			if (notes.indexOf(n) + 1 == notes.size()) {
				multiList.add(sameMonthNoteList);
			}

		}
		
		return multiList;
	}
	
	@SuppressWarnings("serial")
	public List<List<Note>> searchNote(String query){
		final List<Note> notes = new ArrayList<Note>();
		openDB();
		String querySearch = "SELECT DISTINCT N." + DB.NOTE_ID + ", " + DB.NOTE_TITLE + ", " + DB.NOTE_CONTENT + ", " + DB.NOTE_CREATE + ", "+ DB.NOTE_MODIFY + " FROM " + DB.TABLE_NOTE + " N "
				+ "INNER JOIN " + DB.TABLE_TAGDETAIL + " D ON N." + DB.NOTE_ID + " == D." + DB.TAGDETAIL_NOTE_ID + " "
				+ "INNER JOIN " + DB.TABLE_HASHTAG + "  H ON H." + DB.HASHTAG_TAG + " LIKE ('#" + query + "%') AND D."+DB.TAGDETAIL_TAG_ID+" == H." + DB.HASHTAG_ID
				+ " ORDER BY N." + DB.NOTE_ID + " DESC";
		
		Cursor cs = db.rawQuery(querySearch, new String[] {});
		for (cs.moveToFirst(); !cs.isAfterLast(); cs.moveToNext()) {
			Note note = converter.convertCursorToModel(cs);
			notes.add(note);
		}
		
		closeDB();
		return new ArrayList<List<Note>>() {
			{
				add(notes);
			}
		};
	}

	@Override
	public Note get(long id) {
		openDB();

		Cursor cs = db.query(true, DB.TABLE_NOTE, new String[] {}, DB.NOTE_ID
				+ " = " + id, null, null, null, null, null);

		cs.moveToFirst();
		Note note = converter.convertCursorToModel(cs);

		closeDB();
		return note;
	}

	public long getNextNoteId() {
		List<Note> notes = getAll();
		return notes.size() > 0 ? notes.get(0).getId() + 1 : 1; 
	}

	@Override
	public void insert(Note obj) {
		obj.setId(getNextNoteId());
		openDB();
		db.insert(DB.TABLE_NOTE, null, converter.convertModelToContentValues(obj));
		saveTag(getTag(obj.getContent()));
		closeDB();
	}

	@Override
	public void delete(Note obj) {
		deleteTagDetail(obj);
		deleteNotUseTag();
		openDB();
		db.delete(DB.TABLE_NOTE, DB.NOTE_ID + "=" + obj.getId(), null);
		closeDB();
	}

	@Override
	public void update(Note obj) {
		deleteTagDetail(obj);
		saveTag(getTag(obj.getContent()));
		deleteNotUseTag();
		openDB();
		db.update(DB.TABLE_NOTE, converter.convertModelToContentValues(obj), DB.NOTE_ID + "=" + obj.getId(), null);
		closeDB();
	}
	
	private void saveTag(List<String> tags) {
		long noteId = getNextNoteId() - 1;
		
		for(String tag : tags){
			long hashtagId = hashtagDAO.getIdByTag(tag);
			if(hashtagId != 0){
				tagDetailDAO.insert(new TagDetail(0, noteId, hashtagId));
			} else {
				long nextHashtagId = hashtagDAO.getNextTagId();
				hashtagDAO.insert(new Hashtag(nextHashtagId, tag));
				tagDetailDAO.insert(new TagDetail(0, noteId, nextHashtagId));
			}
		}
	}

	public List<String> getTag(String content) {
		List<String> listTag = new ArrayList<String>();
		content = content + " ";
		String tag;

		for (int i = 0; i < content.length(); i++) {
			if (content.charAt(i) == '#') {
				tag = "";
				for (int j = ++i; j < content.length(); j++) {
					if (String.valueOf(content.charAt(j)).matches(pattern)) {
						tag = tag + content.charAt(j);
					} else {
						if(tag.length() > 0 && !listTag.equals("#" + tag))
							listTag.add("#" + tag);
						i = j-1;
						break;
					}
				}
			}
		}
		return listTag;
	}

	private void deleteTagDetail(Note obj) {
		for (TagDetail detail : tagDetailDAO.getAll()) {
			if (detail.getNoteId() == obj.getId()) {
				tagDetailDAO.delete(detail);
			}
		}
	}
	
	private void deleteNotUseTag(){
		List<Hashtag> tags = hashtagDAO.getAll();
		List<TagDetail> details = tagDetailDAO.getAll();
		boolean isTagNotUse;
		for(Hashtag tag : tags){
			isTagNotUse = true;
			for(TagDetail detail : details){
				if(tag.getId() == detail.getTagId()){
					isTagNotUse = false;
				}
			}
			if(isTagNotUse){
				hashtagDAO.delete(tag);
			}
		}
	}
}
