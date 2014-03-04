package com.example.hw2_xmlparser;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cs628.helper.ContactInfo;
import com.cs628.helper.Course;
import com.cs628.helper.XMLParser;

public class ContactActivity extends Activity {

	XMLParser parser;
	ContactInfo contact;
	ArrayList<HashMap<String, String>> courselist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		this.courselist = new ArrayList<HashMap<String, String>>();
		this.parser = new XMLParser();
		this.contact = parser.getContactInfo();

		ArrayList<EditText> addListeners = new ArrayList<EditText>();
		addListeners.add((EditText) findViewById(R.id.email_field));
		addListeners.add((EditText) findViewById(R.id.name_field));
		addListeners.add((EditText) findViewById(R.id.phone_number_field));
		addListeners.add((EditText) findViewById(R.id.office_location_field));
		addListeners.add((EditText) findViewById(R.id.type_field));
		addListeners.add((EditText) findViewById(R.id.office_hours_field));
		addListeners.add((EditText) findViewById(R.id.position_field));

		((EditText) findViewById(R.id.email_field)).setText(contact.getEmail());
		((EditText) findViewById(R.id.name_field)).setText(contact.getName());
		((EditText) findViewById(R.id.phone_number_field)).setText(contact
				.getPhone());
		((EditText) findViewById(R.id.office_location_field)).setText(contact
				.getOffice());
		((EditText) findViewById(R.id.type_field)).setText(contact.getType());
		((EditText) findViewById(R.id.office_hours_field)).setText(contact
				.getOffice_hours());
		((EditText) findViewById(R.id.position_field)).setText(contact
				.getPosition());

		findViewById(R.id.contact_layout).setOnTouchListener(
				new OnTouchListener() {

					public boolean onTouch(View v, MotionEvent event) {

						InputMethodManager inputMethodManager = (InputMethodManager) ContactActivity.this
								.getSystemService(Activity.INPUT_METHOD_SERVICE);
						inputMethodManager.hideSoftInputFromWindow(
								ContactActivity.this.getCurrentFocus()
										.getWindowToken(), 0);
						findViewById(R.id.contact_layout).requestFocus();
						return true;
					}

				});

		for (int i = 0; i < addListeners.size(); i++) {
			addListeners.get(i).setOnFocusChangeListener(
					new OnFocusChangeListener() {
						@Override
						public void onFocusChange(View arg0, boolean focus) {
							String val = ((EditText) arg0).getText().toString();

							switch (arg0.getId()) {
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

		updateCourseList();

		final SimpleAdapter adapter = new SimpleAdapter(this, courselist,
				R.layout.contact_rows, new String[] { "cn", "name" },
				new int[] { R.id.crn, R.id.name });
		((ListView) findViewById(R.id.contactList)).setAdapter(adapter);

		((ListView) findViewById(R.id.contactList))
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> view, View arg1,
							int pos, long arg3) {
						// final int position = pos;
						final EditText coursename = new EditText(view
								.getContext());
						final EditText coursenum = new EditText(view
								.getContext());
						final Course course = contact.getCourses().get(pos);

						coursename.setText(course.getCoursename());
						coursenum.setText(course.getCoursenum());

						LinearLayout layout = new LinearLayout(view
								.getContext());
						layout.setOrientation(LinearLayout.VERTICAL);
						layout.addView(coursename);
						layout.addView(coursenum);

						AlertDialog.Builder alert = new AlertDialog.Builder(
								view.getContext()).setTitle("Edit Course:")
								.setView(layout);

						alert.setPositiveButton("Save",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										course.setCoursename(coursename
												.getText().toString());
										course.setCoursenum(coursenum
												.getText().toString());
										parser.editCourse(contact);
										updateCourseList();
										adapter.notifyDataSetChanged();
									}
								});
						alert.setNegativeButton("Cancel",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								});

						alert.show();
					}

				});
	}

	public void updateCourseList() {
		courselist.clear();
		contact.setCourses(parser.getContactInfo().getCourses());

		for (Course c : contact.getCourses()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("cn", c.getCoursenum());
			map.put("name", c.getCoursename());
			courselist.add(map);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contact, menu);
		return true;
	}

}