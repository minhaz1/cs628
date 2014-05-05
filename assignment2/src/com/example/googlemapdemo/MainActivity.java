package com.example.googlemapdemo;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnMapClickListener,
		OnMarkerClickListener, LocationListener {

	// Constant to know if we have a cached location
	boolean work = true;

	// Handler for multi threading
	private Handler myHandler = new Handler();

	// Reference to sensor manager and accelerometer sensor to deregister
	// onPause, and re-register onResume
	SensorManager sensorManager;
	Sensor accelerometerSensor;

	// Reference to compass sensor to deregister for updates onPause, and
	// re-register onResume
	Sensor compass;

	// Listeners we want to register and deregister
	AcclListener accelListener;
	CompassList compassListener;														// in
																		// milliseconds
	private static final float UPDATE_MAP_INTERVAL = 1.0f;

	// Google Maps Variables
	private GoogleMap theMap;
	public static int totaldegrees;
	public static Timer updatesAccl;
	public boolean hasLocation = false;
	Marker locationMarker;
	LatLng currLocation;

	// Variables to help compute linear acceleration
	public float[] acclvals;
	private float lastVX = 0;
	private float lastVY = 0;
	private float lastAX;
	private float lastAY;
	public static ArrayList<Float> accelvalues = new ArrayList<Float>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Default onCreate code
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Make accelerometer Listener and Register for accelerometer updates
		accelListener = new AcclListener();
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		accelerometerSensor = sensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(accelListener, accelerometerSensor,
				SensorManager.SENSOR_DELAY_NORMAL);

		// Register compass listener for updates
		compassListener = new CompassList();
		compass = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		sensorManager.registerListener(compassListener, compass,
				SensorManager.SENSOR_DELAY_GAME);

		theMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		// Set the marker on the map
		theMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		theMap.setMyLocationEnabled(true);
		
		// Register GPS Listener for updates
		LocationManager location = (LocationManager) getSystemService(LOCATION_SERVICE);
		location.requestLocationUpdates(LocationManager.GPS_PROVIDER, 180000,
				0, this);
		
		// Updates location with accel values every 10 milliseconds
		updatesAccl = new Timer();
		updatesAccl.scheduleAtFixedRate(new UpdateLinAccel(), 0, 10);

	}

	@Override
	protected void onResume() {
super.onResume();
		SharedPreferences pref = getSharedPreferences("locationdetails", 0);

		boolean tmp = pref.getBoolean("hasLocation", false);

		// If we have a location value, save it so its remembered
		if (tmp) {

			long tmpLong = pref.getLong("locationLong", 0);
			long tmpLat = pref.getLong("locationLat", 0);
			currLocation = new LatLng(Double.longBitsToDouble(tmpLat),
					Double.longBitsToDouble(tmpLong));

			setInitialLocation(currLocation);
			
			// Flag that we have a cached location
			hasLocation = tmp;

		}
		
		// Re-register our listeners
		sensorManager.registerListener(accelListener, accelerometerSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(compassListener, compass,
				SensorManager.SENSOR_DELAY_NORMAL);
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		// De-register listeners
		this.sensorManager.unregisterListener(accelListener);
		this.sensorManager.unregisterListener(compassListener);

		// If we have a location value, save it so its remembered
		if (hasLocation) {
			// Save last location value
			SharedPreferences pref = getSharedPreferences("locationdetails", 0);
			Editor prefEditor = pref.edit();
			prefEditor.putBoolean("hasLocation", hasLocation);
			prefEditor.putLong("locationLat",
					Double.valueOf(this.currLocation.latitude).longValue());
			prefEditor.putLong("locationLong",
					Double.valueOf(this.currLocation.latitude).longValue());
			prefEditor.commit();
		}
	}

	public void setInitialLocation(LatLng startloc) {

		locationMarker = theMap.addMarker(new MarkerOptions().position(startloc).title(
				"HAMBURG"));

	}

	public class UpdateLinAccel extends TimerTask {
		@Override
		public void run() {
			if (acclvals == null || hasLocation == false)
				return;

			float temp = (float) Math.pow(acclvals[0], 2);
			temp += (float) Math.pow(acclvals[1], 2);
			temp = (float) Math.sqrt(temp);
			Float val = Float.valueOf(temp);
			accelvalues.add(val);

			if (accelvalues.size() >= 100) {
				updateValues(UtilityFunctions.floatAverage(accelvalues));
				accelvalues.clear();
			}

		}

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

	private class CompassWork implements Runnable {

		private SensorEvent event;

		public CompassWork(SensorEvent event) {
			this.event = event;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			totaldegrees = Math.round(event.values[0]);
		}

	}

	private class CompassList implements SensorEventListener {

		@Override
		public void onSensorChanged(SensorEvent event) {
			CompassWork task = new CompassWork(event);
			myHandler.post(task);
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
		}

	}

	private class AcclListener implements SensorEventListener {

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
		}

		@Override
		public void onSensorChanged(SensorEvent arg0) {
			MainActivity.this.acclvals = arg0.values;
		}

	}

	void updateValues(float averageAcceleration) {

		//
		if (averageAcceleration >= 1) {
			updateAccelerationValues(averageAcceleration, totaldegrees);
			lastVX = lastAX * UPDATE_MAP_INTERVAL;
			lastVY = lastAY * UPDATE_MAP_INTERVAL;
		} else {
			updateAccelerationValues(averageAcceleration, totaldegrees);

		}

		float dx = lastVX * UPDATE_MAP_INTERVAL;
		float dy = lastVY * UPDATE_MAP_INTERVAL;

		updateLocation(dx, dy);
	}

	private void updateAccelerationValues(float acceleration, int degrees) {

		double accelerationX = Math.sin(Math.toRadians(degrees)) * acceleration;
		double accelerationY = Math.cos(Math.toRadians(degrees)) * acceleration;

		if (Math.abs(accelerationX) < .00001) {
			accelerationX = 0;
		}
		if (Math.abs(accelerationY) < .00001) {
			accelerationY = 0;
		}

		this.lastAY = (float) accelerationY;
		this.lastAX = (float) accelerationX;

	}

	// source code from 
	// http://gis.stackexchange.com/questions/2951/algorithm-for-offsetting-a-latitude-longitude-by-some-amount-of-meters
	public void updateLocation(float dx, float dy) {
		double lat = currLocation.latitude;
		double lng = currLocation.longitude;

		// radius of the earth in meters
		int Radius = 6378137;

		double dLat = dy / Radius;
		double dLon = dx / (Radius * Math.cos(Math.PI * lat / 180));

		double newLat = lat + dLat * 180 / Math.PI;
		double newLng = lng + dLon * 180 / Math.PI;
		currLocation = new LatLng(newLat, newLng);
		myHandler.post(new LocationWork(currLocation));

		this.lastVX = 0;
		this.lastVY = 0;
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

		currLocation = new LatLng(location.getLatitude(), location.getLongitude());
		this.hasLocation = true;
		locationMarker = theMap.addMarker(new MarkerOptions().position(currLocation)
				.title("MY LOCATION"));
		theMap.animateCamera(CameraUpdateFactory.newLatLng(currLocation));
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

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
