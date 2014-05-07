package com.example.googlemapdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DisplayFriends extends Activity implements OnMapClickListener,
		OnMarkerClickListener, LocationListener {

	// Constant to know if we have a cached location
	boolean work = true;
	
	String DEVICEID = "FriendX123";
	Context context;
	Map<String, Marker> MarkerList = new HashMap<String, Marker>();
	
	// Handler for multi threading
	private Handler myHandler = new Handler();

	// Reference to sensor manager and accelerometer sensor to deregister
	// onPause, and re-register onResume
	SensorManager sensorManager;
	Sensor accelerometerSensor;

	// Reference to compass sensor to deregister for updates onPause, and
	// re-register onResume
	Sensor compass;
	
	BroadcastReceiver receiver;
	
	private static final float UPDATE_MAP_INTERVAL = 1.0f;

	// Google Maps Variables
	private GoogleMap theMap;
	public static int totaldegrees;
	public static Timer updatesAccl;
	public boolean hasLocation = false;
	Marker locationMarker;
	LatLng currLocation = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Default onCreate code
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if(savedInstanceState != null)
		{
		
		String message = savedInstanceState.get("msg").toString();
		}
		context = this;
		theMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// Set the marker on the map
		theMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		theMap.setMyLocationEnabled(true);
		
		// Register GPS Listener for updates
		LocationManager location = (LocationManager) getSystemService(LOCATION_SERVICE);
		
		location.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 180000,
				0, this);
		
	}
	
	public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        // Explicitly specify that GcmIntentService will handle the intent.
	        ComponentName comp = new ComponentName(context.getPackageName(),
	                GcmIntentService.class.getName());
	        // Start the service, keeping the device awake while it is launching.
	        startWakefulService(context, (intent.setComponent(comp)));
	        setResultCode(Activity.RESULT_OK);
	    }
	}


	public class postTask extends AsyncTask<Float,Float,Float>{

		@Override
		protected Float doInBackground(Float... arg0) {
			// TODO Auto-generated method stub
			try {
				updateDatabase(arg0[0], arg0[1]);
				JSONArray[] s = readDatabase(arg0[0],arg0[1]);
				myHandler.post(new connect(s));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	
	}
	
public class connect implements Runnable
{
	JSONArray[] info;
	
	connect(JSONArray[] s)
	{
		info = s;
	}
	
	@Override
	public void run()
	{
		try 
		{
		drawMarkers(info[0],info[1],info[2]);
		} catch(Exception e)
		{
			String t = e.getMessage();
		}
	}
}	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		// Register GPS Listener for updates
				LocationManager location = (LocationManager) getSystemService(LOCATION_SERVICE);
				
				location.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 180000,
						0, this);
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.example.googlemapdemo.PUSH_NOTIFICATION");

		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
    //do something based on the intent's action
	  new postTask().execute(Float.parseFloat(Double.toString(currLocation.longitude)), Float.parseFloat(Double.toString(currLocation.latitude)));
  }};

   registerReceiver(receiver, filter);
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		 LocationManager l = (LocationManager)this.getSystemService(LOCATION_SERVICE);
		l.removeUpdates(this);
		try
		{
		unregisterReceiver(receiver);
		} catch (IllegalArgumentException e)
		{
			
		}
	}

	@Override
	protected void onStop() {
	
		super.onStop();
		LocationManager l = (LocationManager)this.getSystemService(LOCATION_SERVICE);
		l.removeUpdates(this);
		try
		{
		unregisterReceiver(receiver);
		} catch (IllegalArgumentException e)
		{
			
		}
		finish();
		
	}
	
	public void setInitialLocation(LatLng startloc) {

	}


	public static LatLng lastloc;

	private class LocationWork implements Runnable {

		private LatLng e;

		public LocationWork(LatLng latlng) {
			this.e = latlng;
		}

		@Override
		public void run() {
			// Draw the circle on the map to update location
			CircleOptions circleOptions = new CircleOptions()
					.center(currLocation).radius(1).strokeColor(Color.RED)
					.fillColor(Color.RED);
			theMap.addCircle(circleOptions);

			// Zoom in on new location
			CameraPosition currentPlace = new CameraPosition.Builder()
					.target(currLocation).bearing(totaldegrees).zoom(18f).build();
			theMap.moveCamera(CameraUpdateFactory.newCameraPosition(currentPlace));
		}
	}

	@Override
	public void onMapClick(LatLng arg0) {

	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		return false;
	}

	@Override
	public void onLocationChanged(Location location) {

		new postTask().execute(Float.parseFloat(Double.toString(location.getLongitude())), Float.parseFloat(Double.toString(location.getLatitude())));
		currLocation = new LatLng(location.getLatitude(), location.getLongitude());
		
		/*
		locationMarker = theMap.addMarker(new MarkerOptions().position(currLocation)
				.title("MY LOCATION"));
		theMap.animateCamera(CameraUpdateFactory.newLatLng(currLocation));
		*/
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	public String getTime()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
	
	public JSONArray[] readDatabase(float lat, float longitude) throws Exception{
		    // Create a new HttpClient and Post Header
		    HttpClient httpclient = new DefaultHttpClient();
		    String resp = "" ;
		    HttpPost httppost = new HttpPost("http://minhazm.com/mobile/get.php");

		    try {
		        // Add your data
		        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		        nameValuePairs.add(new BasicNameValuePair("latitude", Float.toString(lat)));
		        nameValuePairs.add(new BasicNameValuePair("longitude", Float.toString(longitude)));
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
		    
        
        JSONObject jObject = new JSONObject(resp);
        JSONArray idArray = jObject.getJSONArray("id");
        JSONArray latArray = jObject.getJSONArray("lat");
        JSONArray longArray = jObject.getJSONArray("long");
        
        //drawMarkers(idArray,latArray,longArray);
        
        JSONArray ret[] = {idArray, latArray, longArray};
        
        return ret;
		
	}
	
	public void drawMarkers(JSONArray id, JSONArray lng, JSONArray lat)
	{
		for(int i = 0; i<id.length(); i++)
		{
			try {
				if(id.get(i) != null && lng.getString(i) != null && lat.getString(i) != null )
				{
					if(MarkerList.containsKey(id.getString(i)))
							{
						MarkerList.get(id.getString(i)).remove();
						MarkerList.remove(id.getString(i));
						Marker m = theMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(lat.getString(i)), Float.parseFloat(lng.getString(i))))
								.title(id.getString(i)));
						this.MarkerList.put(id.getString(i), m);
							}
					else 
					{
						Marker m = theMap.addMarker(new MarkerOptions().position(new LatLng(Float.parseFloat(lat.getString(i)), Float.parseFloat(lng.getString(i))))
								.title(id.getString(i)));
					this.MarkerList.put(id.getString(i), m);
					}
				
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				
			}
		}
	}
	
	public void updateDatabase(float lat, float longitude) throws Exception{
		// Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    String resp = "" ;
	    
	    final SharedPreferences prefs = getSharedPreferences(Login_Screen.class.getSimpleName(),
	            Context.MODE_PRIVATE);
	    String username = prefs.getString("username", "none");
	  
	    HttpPost httppost = new HttpPost("http://minhazm.com/mobile/addlocation.php");

	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	    nameValuePairs.add(new BasicNameValuePair("username", username));
	    nameValuePairs.add(new BasicNameValuePair("lat", Float.toString(lat)));
	    nameValuePairs.add(new BasicNameValuePair("long", Float.toString(longitude)));
	    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        
	        
	    
	}
	
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	


    
}
