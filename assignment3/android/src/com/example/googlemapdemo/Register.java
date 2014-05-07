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
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Register extends Activity {

	private Handler myHandler;
	private EditText username;
	private EditText password;
	private Button register; 
	private Context context; 
	private String reg_id;
	
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    static final String TAG = "GCMDemo";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		myHandler = new Handler();
		this.context = this;
	
		register = (Button) findViewById(R.id.register_click);
		username = (EditText) findViewById(R.id.register_username_);
		password = (EditText) findViewById(R.id.register_password);
		reg_id = getRegistrationId(context);
		
		
		register.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				
			new RegisterTask().execute(username.getText().toString(),password.getText().toString(),reg_id);
			}
			
		}
		);
	}

	public class RegisterTask extends AsyncTask<String, String, String>
	{
		
		@Override
		protected String doInBackground(String... registerInfos) {
			// TODO Auto-generated method stub
			//checkLogin(loginInfo[0],loginInfo[1]);
			if(registerInfos[0] != null && registerInfos[1] != null && registerInfos[2] != null)
			{
				try{
					String registerResult = checkRegister(registerInfos[0],registerInfos[1], registerInfos[2]);
					if(!registerResult.equalsIgnoreCase("ERROR\n"))
					{
						myHandler.post(new Runnable()
						{

							@Override
							public void run() {
								Intent launchApp = new Intent(context, Login_Screen.class);
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
	
	public String checkRegister(String username , String password, String gcm_id) throws IOException
	{	
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    String resp = "" ;
	    HttpPost httppost = new HttpPost("http://minhazm.com/mobile/register.php");

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("username", username));
	        nameValuePairs.add(new BasicNameValuePair("password", password));
	        nameValuePairs.add(new BasicNameValuePair("regid", gcm_id));
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
		getMenuInflater().inflate(R.menu.register, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);
			return rootView;
		}
	}
	
	private String getRegistrationId(Context context) {
	    final SharedPreferences prefs = getGCMPreferences(context);
	    String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	    if (registrationId.isEmpty()) {
	        Log.i(TAG, "Registration not found.");
	        return "";
	    }
	    // Check if app was updated; if so, it must clear the registration ID
	    // since the existing regID is not guaranteed to work with the new
	    // app version.
	    int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	    int currentVersion = getAppVersion(context);
	    if (registeredVersion != currentVersion) {
	        Log.i(TAG, "App version changed.");
	        return "";
	    }
	    return registrationId;
	}

	
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}
	
	
	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
	    // This sample app persists the registration ID in shared preferences, but
	    // how you store the regID in your app is up to you.
	    return getSharedPreferences(Splash.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	}
}
