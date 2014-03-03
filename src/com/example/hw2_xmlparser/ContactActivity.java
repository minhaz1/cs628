package com.example.hw2_xmlparser;

import java.util.ArrayList;

import android.os.Bundle;
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
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.view.MotionEvent;

public class ContactActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		ArrayList<EditText> addListeners = new ArrayList<EditText>();
		addListeners.add((EditText)findViewById(R.id.email_field));
		addListeners.add((EditText)findViewById(R.id.name_field));
		addListeners.add((EditText)findViewById(R.id.phone_number_field));
		addListeners.add((EditText)findViewById(R.id.office_location_field));
		addListeners.add((EditText)findViewById(R.id.type_field));
		addListeners.add((EditText)findViewById(R.id.office_hours_field));
		addListeners.add((EditText)findViewById(R.id.position_field));
		
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
						// TODO Auto-generated method stub
		
						
						String val = ((EditText)arg0).getText().toString();
						
						switch(arg0.getId())
						{
						case R.id.office_location_field:
							//new XMLParser().getContactInfo().setOffice(val);
							break;
						case R.id.office_hours_field:
							//new XMLParser().getContactInfo().setOffice(val);
							break;
						case R.id.phone_number_field:
							//new XMLParser().getContactInfo().setOffice(val);
							break;
						case R.id.position_field:
							//new XMLParser().getContactInfo().setOffice(val);
							break;
						case R.id.email_field:
							//new XMLParser().getContactInfo().setOffice(val);
							break;
						case R.id.type_field:
							//new XMLParser().getContactInfo().setOffice(val);
							break;
						case R.id.name_field:
							//new XMLParser().getContactInfo().setOffice(val);
							break;
						default:
							
							
						}
					}
		        	
		        	
		        });
		    }
		
		/*
		SimpleAdapter adapter = new SimpleAdapter(this,
				  this.dh.selectAll(),
				  R.layout.contact_rows,
				  new String[] {"HI@!","12345"},
				  new int[] {R.id.cn,R.id.name});
				  setListAdapter(adapter);*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}

}
