package com.duyhung.hashtagnote.model;

import java.io.Serializable;

public class TagDetail implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private long noteId;
	private long tagId;

	public TagDetail() {

	}

	public TagDetail(long id, long noteId, long tagId) {
		this.id = id;
		this.noteId = noteId;
		this.tagId = tagId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getNoteId() {
		return noteId;
	}

	public void setNoteId(long noteId) {
		this.noteId = noteId;
	}

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

}
