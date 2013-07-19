package com.duyhung.hashtagnote.model;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String title;
	private String content;
	private Date create;
	private Date modify;

	public Note() {
	}

	public Note(String title, String content, Date create, Date modify) {
		this.title = title;
		this.content = content;
		this.create = create;
		this.modify = modify;
	}

	public Note(long id, String title, String content, Date create, Date modify) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.create = create;
		this.modify = modify;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreate() {
		return create;
	}

	public void setCreate(Date create) {
		this.create = create;
	}

	public Date getModify() {
		return modify;
	}

	public void setModify(Date modify) {
		this.modify = modify;
	}

	@Override
	public String toString() {
		return this.getContent();
	}

}
