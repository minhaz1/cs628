package com.example.hw2_xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Xml;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button contact_screen_launch; 
	Button news_screen_launch;
	Button events_screen_launch;
	Button courses_screen_launch; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         contact_screen_launch= (Button)findViewById(R.id.contact_screen_button);
         news_screen_launch= (Button)findViewById(R.id.news_screen_button);
         events_screen_launch= (Button)findViewById(R.id.events_screen_button);
         courses_screen_launch= (Button)findViewById(R.id.courses_screen_button);
         
         
         contact_screen_launch.setOnClickListener(new OnClickListener(){
        	
        	@Override
        	public void onClick(View v) 
            {   
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                    startActivity(intent);      
            }
        	
        	
        });
         
         news_screen_launch.setOnClickListener(new OnClickListener(){
         	
        	@Override
        	public void onClick(View v) 
            {   
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                    startActivity(intent);      
            }
        	
        	
        });
         
         events_screen_launch.setOnClickListener(new OnClickListener(){
         	
        	@Override
        	public void onClick(View v) 
            {   
                Intent intent = new Intent(MainActivity.this, EventsActivity.class);
                    startActivity(intent);      
            }
        	
        	
        });
         
         courses_screen_launch.setOnClickListener(new OnClickListener(){
         	
        	@Override
        	public void onClick(View v) 
            {   
                Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
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



