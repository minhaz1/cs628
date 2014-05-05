package com.example.hw2_xmlparser;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.AlertDialog;

public class MainActivity extends Activity {

	Button contact_screen_launch;
	Button news_screen_launch;
	Button events_screen_launch;
	Button courses_screen_launch;
	Button help_button_launch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		contact_screen_launch = (Button) findViewById(R.id.contact_screen_button);
		news_screen_launch = (Button) findViewById(R.id.news_screen_button);
		events_screen_launch = (Button) findViewById(R.id.events_screen_button);
		courses_screen_launch = (Button) findViewById(R.id.courses_screen_button);
		help_button_launch =(Button) findViewById(R.id.main_screen_help);
		
		help_button_launch.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create(); //Read Update
		        alertDialog.setTitle("Help Message");
		        alertDialog.setMessage("Welcome! \n\nTo get started, just click any category. \n\nSee any pieces of information that need to be updated? \n\nClick on any editable boxes, and you can type right into them. Once you touch anywhere else on the screen they will be saved. \n\nAll other personal information can be edited just by tapping on it, and a popup will emerge on your screen that allows you to edit the information ");

		        alertDialog.setButton("Get started!", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int which) {
		              
		         }
		           
		          
			});
		        alertDialog.show();
			}
			
		});
		
		contact_screen_launch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ContactActivity.class);
				startActivity(intent);
			}

		});

		news_screen_launch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						NewsActivity.class);
				startActivity(intent);
			}

		});

		events_screen_launch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						EventsActivity.class);
				startActivity(intent);
			}

		});

		courses_screen_launch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						CoursesActivity.class);
				startActivity(intent);
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
