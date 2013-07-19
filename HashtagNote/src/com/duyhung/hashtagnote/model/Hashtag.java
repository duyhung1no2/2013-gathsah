package com.duyhung.hashtagnote.model;

import java.io.Serializable;

public class Hashtag implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String tag;

	public Hashtag() {

	}

	public Hashtag(long id, String tag) {
		this.id = id;
		this.tag = tag;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
	
	@Override
	public String toString() {
		return this.getTag();
	}

}
