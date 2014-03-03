package com.example.hw2_xmlparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.cs628.helper.Course;
import com.cs628.helper.CourseDetail;
import com.cs628.helper.XMLParser;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CoursesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses);
		
		 ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		  
		  XMLParser tmp = new XMLParser();
		  ArrayList<CourseDetail> courses = tmp.getCourses();
		  for(CourseDetail c:courses)
		  {
			  HashMap<String,String> map = new HashMap<String,String>();
			  map.put("cn", c.getCoursenum());
			  map.put("title", c.getTitle());
			  map.put("credit", c.getCredits());
			  map.put("time", c.getTime());
			  map.put("days", c.getDays());
			  list.add(map);
		  }
		  

		  
		SimpleAdapter adapter = new SimpleAdapter(this,
				  list,
				  R.layout.courses_row,
				  new String[] {"cn","title","credit","time","days"},
				  new int[] {R.id.courses_cn,R.id.courses_course_title,R.id.courses_credit_hours,R.id.courses_time,R.id.courses_days});
				  ((ListView)findViewById(R.id.coursesList)).setAdapter(adapter);
				  
				  
				  
	}
		

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.courses, menu);
		return true;
	}

}
