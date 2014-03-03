package com.cs628.helper;

public class Course implements Comparable<Course>{

	@Override
	public String toString() {
		return "Course [coursename=" + coursename + ", coursenum=" + coursenum
				+ "]";
	}

	String coursename;
	String coursenum;
	
	public Course(String coursename, String coursenum) {
		this.coursename = coursename;
		this.coursenum = coursenum;
	}
	
	public String getCoursename() {
		return coursename;
	}
	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}
	public String getCoursenum() {
		return coursenum;
	}
	public void setCoursenum(String coursenum) {
		this.coursenum = coursenum;
	}

	@Override
	public int compareTo(Course other) {
		return this.coursename.compareTo(other.coursename);
	}

	
}
