package com.example.hw2_xmlparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.cs628.helper.Event;
import com.cs628.helper.News;
import com.cs628.helper.XMLParser;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NewsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		 ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		  
		  XMLParser tmp = new XMLParser();
		  ArrayList<News> news = tmp.getNews();
		  for(News n:news)
		  {
			  HashMap<String,String> map = new HashMap<String,String>();
			  map.put("highlights", n.getHighlights());
			  map.put("keywords", n.getKeyword());
			  map.put("title", n.getTitle());
			  list.add(map);
		  }
		  
		SimpleAdapter adapter = new SimpleAdapter(this,
				  list,
				  R.layout.news_row,
				  new String[] {"highlights","keywords","title"},
				  new int[] {R.id.news_highlights,R.id.news_keywords,R.id.news_title});
				  ((ListView)findViewById(R.id.newsList)).setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
		return true;
	}

}
