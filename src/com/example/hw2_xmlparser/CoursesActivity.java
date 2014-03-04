package com.example.hw2_xmlparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.cs628.helper.Course;
import com.cs628.helper.CourseDetail;
import com.cs628.helper.Event;
import com.cs628.helper.XMLParser;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class CoursesActivity extends Activity {
	private XMLParser parser;
	private ArrayList<CourseDetail> courses;
	private ArrayList<HashMap<String, String>> courselist;
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses);

		parser = new XMLParser();

		courselist = new ArrayList<HashMap<String, String>>();
		updateCourseList();

		adapter = new SimpleAdapter(this, courselist, R.layout.courses_row,
				new String[] { "cn", "title", "credit", "time", "days" },
				new int[] { R.id.courses_cn, R.id.courses_course_title,
						R.id.courses_credit_hours, R.id.courses_time,
						R.id.courses_days });
		((ListView) findViewById(R.id.coursesList)).setAdapter(adapter);

		setEdit();

	}

	public void setEdit() {

		((ListView) findViewById(R.id.coursesList))
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> view, View arg1,
							int pos, long arg3) {

						final EditText CN = new EditText(view.getContext());
						final EditText title = new EditText(view.getContext());
						final EditText credits = new EditText(view.getContext());
						final EditText days = new EditText(view.getContext());
						final EditText time = new EditText(view.getContext());
						final CourseDetail course = courses.get(pos);

						CN.setText(course.getCoursenum());
						title.setText(course.getTitle());
						credits.setText(course.getCredits());
						days.setText(course.getDays());
						time.setText(course.getTime());

						LinearLayout layout = new LinearLayout(view
								.getContext());
						layout.setOrientation(LinearLayout.VERTICAL);
						layout.addView(CN);
						layout.addView(title);
						layout.addView(credits);
						layout.addView(days);
						layout.addView(time);

						AlertDialog.Builder alert = new AlertDialog.Builder(
								view.getContext()).setTitle("Edit the Course:")
								.setView(layout);

						alert.setPositiveButton("Save",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										course.setCoursenum(CN.getText()
												.toString());
										course.setTitle(title.getText()
												.toString());
										course.setCredits(credits.getText()
												.toString());
										course.setDays(days.getText()
												.toString());
										course.setTime(time.getText()
												.toString());

										parser.editCourseDetail(courses);
										updateCourseList();
										adapter.notifyDataSetChanged();
									}
								});
						alert.setNeutralButton("Cancel",
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
		courses = parser.getCourses();

		for (CourseDetail c : courses) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("cn", c.getCoursenum());
			map.put("title", c.getTitle());
			map.put("credit", c.getCredits());
			map.put("time", c.getTime());
			map.put("days", c.getDays());
			courselist.add(map);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.courses, menu);
		return true;
	}

}
