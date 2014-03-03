package com.example.hw2_xmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs628.helper.ContactInfo;
import com.cs628.helper.Course;
import com.cs628.helper.XMLParser;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.view.MotionEvent;

public class ContactActivity extends Activity {

	XMLParser parser; 
	ContactInfo contact;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		this.parser = new XMLParser();
		this.contact = parser.getContactInfo();
		
		ArrayList<EditText> addListeners = new ArrayList<EditText>();
		addListeners.add((EditText)findViewById(R.id.email_field));
		addListeners.add((EditText)findViewById(R.id.name_field));
		addListeners.add((EditText)findViewById(R.id.phone_number_field));
		addListeners.add((EditText)findViewById(R.id.office_location_field));
		addListeners.add((EditText)findViewById(R.id.type_field));
		addListeners.add((EditText)findViewById(R.id.office_hours_field));
		addListeners.add((EditText)findViewById(R.id.position_field));
		
		((EditText)findViewById(R.id.email_field)).setText(contact.getEmail());
		((EditText)findViewById(R.id.name_field)).setText(contact.getName());
		((EditText)findViewById(R.id.phone_number_field)).setText(contact.getPhone());
		((EditText)findViewById(R.id.office_location_field)).setText(contact.getOffice());
		((EditText)findViewById(R.id.type_field)).setText(contact.getType());
		((EditText)findViewById(R.id.office_hours_field)).setText(contact.getOffice_hours());
		((EditText)findViewById(R.id.position_field)).setText(contact.getPosition());

		
		findViewById(R.id.contact_layout).setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
              
            	InputMethodManager inputMethodManager = (InputMethodManager)  ContactActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(ContactActivity.this.getCurrentFocus().getWindowToken(), 0);
                findViewById(R.id.contact_layout).requestFocus();
                return true;
            }
            
		});
		
		for(int i = 0; i < addListeners.size(); i++)
		{
			addListeners.get(i).setOnFocusChangeListener(new OnFocusChangeListener(){
					@Override
					public void onFocusChange(View arg0, boolean focus) {						
						String val = ((EditText)arg0).getText().toString();
						
						switch(arg0.getId())
						{
						case R.id.office_location_field:
							contact.setOffice(val);
							break;
						case R.id.office_hours_field:
							contact.setOfficeHours(val);
							break;
						case R.id.phone_number_field:
							contact.setPhone(val);
							break;
						case R.id.position_field:
							contact.setPosition(val);
							break;
						case R.id.email_field:
							contact.setEmail(val);
							break;
						case R.id.type_field:
							contact.setType(val);
							break;
						case R.id.name_field:
							contact.setName(val);
							break;
						}
						
						parser.editContactInfo(contact);
					}
		        	
		        	
		        });
		    }
		
		
		 ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		  
		  XMLParser tmp = new XMLParser();
		  ArrayList<Course> courses = tmp.getContactInfo().getCourses();
		  for(Course c:courses)
		  {
			  HashMap<String,String> map = new HashMap<String,String>();
			  map.put("cn", c.getCoursenum());
			  map.put("name", c.getCoursename());
			  list.add(map);
		  }
		  
	
	
		SimpleAdapter adapter = new SimpleAdapter(this,
				  list,
				  R.layout.contact_rows,
				  new String[] {"cn","name"},
				  new int[] {R.id.crn,R.id.name});
				  ((ListView)findViewById(R.id.listView1)).setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}

}
