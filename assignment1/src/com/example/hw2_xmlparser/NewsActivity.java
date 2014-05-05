package com.example.hw2_xmlparser;

import java.util.ArrayList;
import java.util.HashMap;

import com.cs628.helper.CourseDetail;
import com.cs628.helper.Event;
import com.cs628.helper.News;
import com.cs628.helper.XMLParser;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class NewsActivity extends Activity {
	private XMLParser parser;
	private ArrayList<News> allnews;
	private ArrayList<HashMap<String, String>> newslist;
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);

		newslist = new ArrayList<HashMap<String, String>>();

		parser = new XMLParser();
		updateNewsList();

		 adapter = new SimpleAdapter(this, newslist,
				R.layout.news_row, new String[] { "highlights", "keywords",
						"title" }, new int[] { R.id.news_highlights,
						R.id.news_keywords, R.id.news_title });
		((ListView) findViewById(R.id.newsList)).setAdapter(adapter);

		setEdit();
	}

	public void updateNewsList() {
		newslist.clear();
		allnews = parser.getNews();

		for (News n : allnews) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("highlights", n.getHighlights());
			map.put("keywords", n.getKeyword());
			map.put("title", n.getTitle());
			newslist.add(map);
		}
	}

	public void setEdit() {

		((ListView) findViewById(R.id.newsList))
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> view, View arg1,
							int pos, long arg3) {

						final EditText title = new EditText(view.getContext());
						final EditText keyword = new EditText(view.getContext());
						final EditText highlights = new EditText(view
								.getContext());
						final News news = allnews.get(pos);

						title.setText(news.getTitle());
						keyword.setText(news.getKeyword());
						highlights.setText(news.getHighlights());

						LinearLayout layout = new LinearLayout(view
								.getContext());
						layout.setOrientation(LinearLayout.VERTICAL);
						layout.addView(title);
						layout.addView(keyword);
						layout.addView(highlights);

						AlertDialog.Builder alert = new AlertDialog.Builder(
								view.getContext()).setTitle("Edit News:")
								.setView(layout);

						alert.setPositiveButton("Save",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

										news.setTitle(title.getText()
												.toString());
										news.setKeyword(keyword.getText()
												.toString());
										news.setHighlights(highlights.getText()
												.toString());

										parser.editNews(allnews);
										updateNewsList();
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news, menu);
		return true;
	}

}
