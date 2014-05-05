package com.cs628.helper;

public class Event {

	@Override
	public String toString() {
		return "Event [type=" + type + ", time=" + time + ", day=" + day
				+ ", note=" + note + "]";
	}

	private String type, time, day, note;

	public Event(){
		// no arg constructor
	}
	
	public Event(String type, String time, String day, String note) {
		super();
		this.type = type;
		this.time = time;
		this.day = day;
		this.note = note;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
}
