package com.example.googlemapdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;




public class Login_Screen extends Activity {

	private Handler myHandler;
	private Context context;
	private Button login;
	private EditText username;
	private EditText password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login__screen);
		
		myHandler = new Handler();
		this.context = this;
		
		
		
		
		login = (Button) findViewById(R.id.splash_login);
		username = (EditText) findViewById(R.id.splash_enter_login);
		password = (EditText) findViewById(R.id.splash_enter_password);
		
		login.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				
			new LoginTask().execute(username.getText().toString(),password.getText().toString());
			}
			
		}
		);
		
	}

	public class LoginTask extends AsyncTask<String, String, String>
	{
		
		@Override
		protected String doInBackground(String... loginInfos) {
			// TODO Auto-generated method stub
			//checkLogin(loginInfo[0],loginInfo[1]);
			if(loginInfos[0] != null && loginInfos[1] != null)
			{
				try{
					String loginResult = checkLogin(loginInfos[0],loginInfos[1]);
					if(loginResult.equalsIgnoreCase("1OKAY"))
					{
						myHandler.post(new Runnable()
						{

							@Override
							public void run() {
								final SharedPreferences prefs = getSharedPreferences(Login_Screen.class.getSimpleName(),
							            Context.MODE_PRIVATE);
							    SharedPreferences.Editor editor = prefs.edit();
							    editor.putString("username", username.getText().toString());
							    editor.commit();
								Intent launchApp = new Intent(context, DisplayFriends.class);
								launchApp.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(launchApp);
							}
						}
						);
					}
				} catch (Exception e)
				{
				}
			}
			return "";
		}
		
	}
	
	public String checkLogin(String username , String password) throws IOException
	{
		
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    String resp = "" ;
	    HttpPost httppost = new HttpPost("http://minhazm.com/mobile/login.php");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("username", username));
	        nameValuePairs.add(new BasicNameValuePair("password", password));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        InputStream inputStream = response.getEntity().getContent();
	        
	        if (inputStream != null) {
	            Writer writer = new StringWriter();

	            char[] buffer = new char[1024];
	            try {
	                Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),1024);
	                int n;
	                while ((n = reader.read(buffer)) != -1) {
	                    writer.write(buffer, 0, n);
	                }
	            } finally {
	                inputStream.close();
	            }
	            resp = writer.toString();
	            
	        } else {
	            resp = "";
	        }
	    } catch (ClientProtocolException e) {
	    } catch (IOException e) {
	    	// TODO Auto-generated catch block
	    }
	    
        
        return resp;

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login__screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
