package com.example.hw2_xmlparser;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.cs628.helper.Event;
import com.cs628.helper.XMLParser;

public class EventsActivity extends Activity {

	private XMLParser parser;
	private ArrayList<Event> events;
	private ArrayList<HashMap<String, String>> eventslist;
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_events);

		this.parser = new XMLParser();
		this.events = parser.getEvents();

		eventslist = new ArrayList<HashMap<String, String>>();
		updateEventsList();

		adapter = new SimpleAdapter(this, eventslist, R.layout.events_row,
				new String[] { "day", "note", "time", "type" }, new int[] {
						R.id.events_day, R.id.events_note, R.id.events_time,
						R.id.events_type });
		((ListView) findViewById(R.id.eventsList)).setAdapter(adapter);

		setAddButton();
		setEdit();
	}

	public void updateEventsList() {
		eventslist.clear();
		events = parser.getEvents();
		for (Event c : events) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("day", c.getDay());
			map.put("note", c.getNote());
			map.put("time", c.getTime());
			map.put("type", c.getType());
			eventslist.add(map);
		}
	}

	public void setEdit() {
		((ListView) findViewById(R.id.eventsList))
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> view, View arg1,
							int pos, long arg3) {

						final EditText type = new EditText(view.getContext());
						final EditText time = new EditText(view.getContext());
						final EditText day = new EditText(view.getContext());
						final EditText note = new EditText(view.getContext());
						final Event event = events.get(pos);

						type.setText(event.getType());
						time.setText(event.getTime());
						day.setText(event.getDay());
						note.setText(event.getNote());

						LinearLayout layout = new LinearLayout(view
								.getContext());
						layout.setOrientation(LinearLayout.VERTICAL);
						layout.addView(type);
						layout.addView(time);
						layout.addView(day);
						layout.addView(note);

						AlertDialog.Builder alert = new AlertDialog.Builder(
								view.getContext()).setTitle("Edit event:")
								.setView(layout);

						alert.setPositiveButton("Save",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										event.setType(type.getText().toString());
										event.setTime(time.getText().toString());
										event.setDay(day.getText().toString());
										event.setNote(note.getText().toString());

										parser.editEvent(events);
										updateEventsList();
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

						alert.setNegativeButton("Delete",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										events.remove(event);
										parser.deleteEvent(events);
										updateEventsList();
										adapter.notifyDataSetChanged();

									}
								});
						alert.show();
					}
				});

	}

	public void setAddButton() {
		Button add = (Button) findViewById(R.id.btnAddEvent);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final EditText type = new EditText(v.getContext());
				final EditText time = new EditText(v.getContext());
				final EditText day = new EditText(v.getContext());
				final EditText note = new EditText(v.getContext());

				// final Event event =
				type.setHint("Type");
				time.setHint("Time");
				day.setHint("Day");
				note.setHint("Note");

				LinearLayout ll = new LinearLayout(v.getContext());
				ll.setOrientation(LinearLayout.VERTICAL);
				ll.addView(type);
				ll.addView(time);
				ll.addView(day);
				ll.addView(note);

				AlertDialog.Builder alert = new AlertDialog.Builder(v
						.getContext()).setTitle("Add new event:").setView(ll);// .show()

				alert.setPositiveButton("Save",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								Event event = new Event();
								event.setType(type.getText().toString());
								event.setTime(time.getText().toString());
								event.setDay(day.getText().toString());
								event.setNote(note.getText().toString());
								events.add(event);
								parser.addEvent(event);

								updateEventsList();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}

}
