package com.cs628.helper;

/** 
 * For the more detailed course.
 * 
 * @author minhazm
 *
 */
public class CourseDetail implements Comparable<CourseDetail>{

	@Override
	public String toString() {
		return "CourseDetail [coursenum=" + coursenum + ", title=" + title
				+ ", credits=" + credits + ", days=" + days + ", time=" + time
				+ "]";
	}

	private String coursenum, title, credits, days, time;
	
	public CourseDetail(String coursenum, String title, String credits,
			String days, String time) {
		this.coursenum = coursenum;
		this.title = title;
		this.credits = credits;
		this.days = days;
		this.time = time;
	}
	
	public String getCoursenum() {
		return coursenum;
	}

	public void setCoursenum(String coursenum) {
		this.coursenum = coursenum;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCredits() {
		return credits;
	}

	public void setCredits(String credits) {
		this.credits = credits;
	}

	public String getDays() {
		return days;
	}

	public void setDays(String days) {
		this.days = days;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public int compareTo(CourseDetail another) {
		return this.title.compareTo(another.title);
	}
	
	
}
