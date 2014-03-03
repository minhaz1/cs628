package com.cs628.helper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.os.Environment;

public class XMLParser {
	Document doc;
	Node item;
	DocumentBuilder docBuilder;
	String filepath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/diary.xml";
	//String XMLFILE = "<?xml version = '1.0' encoding = 'UTF-8'?>\r\n<items>\r\n<contact\r\ntype=\"faculty\"\r\nname=\"Dr. Nilanjan Banerjee\"\r\nposition=\"Assistant Professor\"\r\noffice=\"ITE 362\"\r\nemail=\"nilanb@umbc.edu\"\r\nphone=\"999999999\"\r\noffice_hour=\"10-11am Mon,thursday\">\r\n<course name=\"Wireless Network and Systems\" CRN=\"CS 795\"/>\r\n<course name=\"App Development for Smart Devices\" CRN=\"CS 495\"/>\r\n</contact>\r\n\r\n<courses\r\nCN=\"16746\"\r\ncourseTitle=\"Database Concept\"\r\ncreditHours=\"3.00\"\r\nDays=\"TR\"\r\nTime=\"1:30-2:45pm\"\r\n/>\r\n\r\n<events\r\ntype=\"class\"\r\ntime=\"1:30-2:45pm\"\r\nday=\"TR\"\r\nnote=\"Class Database\" />\r\n\r\n<events\r\ntype=\"appointment\"\r\ntime=\"1:30-2:45pm\"\r\nday=\"W\"\r\nnote=\"TA Database\" />\r\n\r\n<news\r\ntitle=\"Talk: Self-sustainable Cyber-physical Systems Design\"\r\nkeyword=\"Sustainability\"\r\nhighlights=\"Nilanjan Banerjee\"\r\n/>\r\n</items>\r\n";
	public XMLParser(){
		try {
		//	File file = new File(filepath);
			//FileOutputStream fos = new FileOutputStream(file);
			//fos.write(XMLFILE.getBytes());
			//fos.close();
			File fileToOpen = new File(filepath);
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			System.out.println(filepath);
			doc = docBuilder.parse(fileToOpen);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ContactInfo getContactInfo(){
		if(doc == null){
			return null;
		}
		
		// grab contact node
		Node contactNode = doc.getElementsByTagName("contact").item(0);
		
		// no contact info in file
		if(contactNode == null){
			return null;
		}
		
		String type = contactNode.getAttributes().getNamedItem("type").getNodeValue();
		String name = contactNode.getAttributes().getNamedItem("name").getNodeValue();
		String pos = contactNode.getAttributes().getNamedItem("position").getNodeValue();
		String office = contactNode.getAttributes().getNamedItem("office").getNodeValue();
		String email = contactNode.getAttributes().getNamedItem("email").getNodeValue();
		String phone = contactNode.getAttributes().getNamedItem("phone").getNodeValue();
		String office_hours = contactNode.getAttributes().getNamedItem("office_hour").getNodeValue();

		NodeList courses = doc.getElementsByTagName("course");
		
		ArrayList<Course> courseList = new ArrayList<Course>();

		for(int i = 0; i < courses.getLength(); i++){
			String coursename = courses.item(i).getAttributes().getNamedItem("name").getNodeValue();
			String coursecode = courses.item(i).getAttributes().getNamedItem("CRN").getNodeValue();
			courseList.add(new Course(coursename, coursecode));
		}
	
		return new ContactInfo(type, name, pos, office, email, phone, office_hours, courseList);
	}
	

	public void editContactInfo(ContactInfo c){
		Node info = doc.getElementsByTagName("contact").item(0); 
		info.getAttributes().getNamedItem("type").setTextContent(c.getType());
		info.getAttributes().getNamedItem("name").setTextContent(c.getName());
		info.getAttributes().getNamedItem("position").setTextContent(c.getPosition());
		info.getAttributes().getNamedItem("office").setTextContent(c.getOffice());
		info.getAttributes().getNamedItem("email").setTextContent(c.getEmail());
		info.getAttributes().getNamedItem("phone").setTextContent(c.getPhone());
		info.getAttributes().getNamedItem("office_hour").setTextContent(c.getOffice_hours());
		writeXMLFile(filepath);
	}
	
	public void addCourse(Course course) {
		// get contact tag
		Node contact = doc.getElementsByTagName("contact").item(0);
		
		// create new course element
		Element courseElement = doc.createElement("course");
		
		// set attributes
		courseElement.setAttribute("name", course.getCoursename());
		courseElement.setAttribute("CRN", course.getCoursenum());
		
		// append children and write to file
		contact.appendChild(courseElement);
		writeXMLFile(filepath);
	}

	public void editCourse(ContactInfo c){
		NodeList courses = doc.getElementsByTagName("course");

		for(int i = 0; i < courses.getLength(); i++){
			courses.item(i).getAttributes().getNamedItem("name").setNodeValue(c.getCourses().get(i).getCoursename());
			courses.item(i).getAttributes().getNamedItem("CRN").setNodeValue(c.getCourses().get(i).getCoursenum());
		}
		
		writeXMLFile(filepath);	
	}
	

	public ArrayList<CourseDetail> getCourses(){
		NodeList courses = doc.getElementsByTagName("courses"); 
		
		ArrayList<CourseDetail> courseList = new ArrayList<CourseDetail>();
		
		for(int i = 0; i < courses.getLength(); i++){
			Node course = courses.item(i);
			String CN = course.getAttributes().getNamedItem("CN").getNodeValue();
			String title = course.getAttributes().getNamedItem("courseTitle").getNodeValue();
			String credits = course.getAttributes().getNamedItem("creditHours").getNodeValue();
			String days = course.getAttributes().getNamedItem("Days").getNodeValue();
			String time = course.getAttributes().getNamedItem("Time").getNodeValue();

			courseList.add(new CourseDetail(CN, title, credits, days, time));
		}
		
		return (!courseList.isEmpty()) ? courseList : null;
	}
	
	public void editCourseDetail(CourseDetail course, int pos) {
		// get all courses and select the one corresponding to pos
		Node courseNode = doc.getElementsByTagName("courses").item(pos);	
		
		// set all of the fields
		courseNode.getAttributes().getNamedItem("CN").setNodeValue(course.getCoursenum());
		courseNode.getAttributes().getNamedItem("courseTitle").setNodeValue(course.getTitle());
		courseNode.getAttributes().getNamedItem("creditHours").setNodeValue(course.getCredits());
		courseNode.getAttributes().getNamedItem("Days").setNodeValue(course.getDays());
		courseNode.getAttributes().getNamedItem("Time").setNodeValue(course.getTime());

		writeXMLFile(filepath);
	}

	public ArrayList<Event> getEvents() {
		NodeList events = doc.getElementsByTagName("events"); 

		ArrayList<Event> eventlist = new ArrayList<Event>();
		
		for(int i = 0; i < events.getLength(); i++){
			Node event = events.item(i);
			String type = event.getAttributes().getNamedItem("type").getNodeValue();
			String time = event.getAttributes().getNamedItem("time").getNodeValue();
			String day = event.getAttributes().getNamedItem("day").getNodeValue();
			String note = event.getAttributes().getNamedItem("note").getNodeValue();
			eventlist.add(new Event(type, time, day, note));
		}

		return (!eventlist.isEmpty()) ? eventlist : null;
	}

	public void addEvent(Event event) {
		// get root tag
		Node items = doc.getElementsByTagName("items").item(0);
		
		// create new event
		Element newevent = doc.createElement("events");
		
		// set attribtues
		newevent.setAttribute("type", event.getType());
		newevent.setAttribute("time", event.getTime());
		newevent.setAttribute("day", event.getDay());
		newevent.setAttribute("note", event.getNote());

		// add to root and write
		items.appendChild(newevent);
		writeXMLFile(filepath);
	}

	public void editEvent(int childPosition, Event event) {
		// get event corresponding to item
		Node eventNode = doc.getElementsByTagName("events").item(childPosition);
		
		// set all values
		eventNode.getAttributes().getNamedItem("type").setNodeValue(event.getType());
		eventNode.getAttributes().getNamedItem("time").setNodeValue(event.getTime());
		eventNode.getAttributes().getNamedItem("day").setNodeValue(event.getDay());
		eventNode.getAttributes().getNamedItem("note").setNodeValue(event.getNote());
		
		// write to file
		writeXMLFile(filepath);
	}

	public void deleteEvent(int childPosition) {
		Node items = doc.getElementsByTagName("items").item(0);
		NodeList allItems = items.getChildNodes();

		int numEvents = 0;
		for(int i = 0; i < allItems.getLength(); i++){
			if(allItems.item(i).getNodeName().equals("events")){
				System.out.println(numEvents);
				if(numEvents == childPosition){
					items.removeChild(allItems.item(i));
					break;
				}
				numEvents++;
			}
		}
		
		writeXMLFile(filepath);
	}

	public ArrayList<News> getNews() {
		NodeList news = doc.getElementsByTagName("news"); 
		ArrayList<News> newslist = new ArrayList<News>();
		
		for(int i = 0; i < news.getLength(); i++){
			Node newsItems = news.item(i);
			String title = newsItems.getAttributes().getNamedItem("title").getNodeValue();
			String keyword = newsItems.getAttributes().getNamedItem("keyword").getNodeValue();
			String highlights = newsItems.getAttributes().getNamedItem("highlights").getNodeValue();
			newslist.add(new News(title, keyword, highlights));
		}
	
		return (!newslist.isEmpty()) ? newslist : null; 
	}

	public void editNews(int childPosition, News news) {
		Node newsNode = doc.getElementsByTagName("news").item(childPosition);

		newsNode.getAttributes().getNamedItem("title").setNodeValue(news.getTitle());
		newsNode.getAttributes().getNamedItem("keyword").setNodeValue(news.getKeyword());
		newsNode.getAttributes().getNamedItem("highlights").setNodeValue(news.getHighlights());
		
		writeXMLFile(filepath);
	}


	private void writeXMLFile(String filePath) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(filePath));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*public static void main(String[] args) {
		XMLParser parser = new XMLParser("/Users/minhazm/Downloads/diary.xml");
		
//		System.out.println(parser.getContactInfo());
//		System.out.println();
//		System.out.println(parser.getCourses());
//		System.out.println();
//		System.out.println(parser.getEvents());
//		System.out.println();
		System.out.println(parser.getEvents());
	
		Event event = new Event("Class", "1pm", "TR", "something is happening");
		parser.addEvent(event);
		
		System.out.println();
		System.out.println(parser.getEvents());
	}*/
	

}
