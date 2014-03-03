package com.example.hw2_xmlparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.cs628.helper.Course;
import com.cs628.helper.Event;
import com.cs628.helper.XMLParser;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class EventsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);
	
		 ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		  
		  XMLParser tmp = new XMLParser();
		  ArrayList<Event> events = tmp.getEvents();
		  for(Event c:events)
		  {
			  HashMap<String,String> map = new HashMap<String,String>();
			  map.put("day", c.getDay());
			  map.put("note", c.getNote());
			  map.put("time", c.getTime());
			  map.put("type", c.getType());
			  list.add(map);
		  }
		  
	
	
		SimpleAdapter adapter = new SimpleAdapter(this,
				  list,
				  R.layout.events_row,
				  new String[] {"day","note","time","type"},
				  new int[] {R.id.events_day,R.id.events_note,R.id.events_time,R.id.events_type});
				  ((ListView)findViewById(R.id.eventsList)).setAdapter(adapter);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}

}
