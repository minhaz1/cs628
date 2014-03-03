package com.cs628.helper;

import java.util.ArrayList;
import java.util.Collections;

public class ContactInfo {

	private String type;
	private String name;
	private String position;
	private String office;
	private String email;
	private String phone;
	private String officeHours;
	private ArrayList<Course> courses;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContactInfo(String type, String name, String position, String office,
			String email, String phone, String office_hours, ArrayList<Course> courses) {
		this.type = type;
		this.name = name;
		this.position = position;
		this.office = office;
		this.email = email;
		this.phone = phone;
		this.officeHours = office_hours;
		this.courses = courses;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getOffice_hours() {
		return officeHours;
	}

	public void setOfficeHours(String officeHours) {
		this.officeHours = officeHours;
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}


	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
	
	public boolean addCourse(Course course){
		if(course == null)
			return false;
		
		courses.add(course);
		return true;
	}

	@Override
	public String toString() {
		return "ContactInfo [type=" + type + ", name=" + name + ", position="
				+ position + ", office=" + office + ", email=" + email
				+ ", phone=" + phone + ", office_hours=" + officeHours
				+ ", courses=" + courses + "]";
	}
	
	
	
}
