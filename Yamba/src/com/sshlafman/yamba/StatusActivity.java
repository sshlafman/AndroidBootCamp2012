  package com.sshlafman.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class StatusActivity extends Activity implements LocationListener {
	static final String TAG = "StatusActivity";
	static final String PROVIDER = LocationManager.GPS_PROVIDER;
	EditText editStatus;
	LocationManager locationManager;
	Location location;
	
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
//        Debug.startMethodTracing("Yamba.trace");
        
        Log.d(TAG, "onCreate with Bundle: " + bundle);
        
        setContentView(R.layout.status);
        
        editStatus = (EditText)findViewById(R.id.edit_status);
        
        locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        location = locationManager.getLastKnownLocation(PROVIDER);
    }

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(PROVIDER, 30000, 1000, this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
//		Debug.stopMethodTracing();
	}


	public void onClick(View v) {
		final String statusText = editStatus.getText().toString();
		
		new PostToTwitter().execute(statusText);
		
		Log.d(TAG, "onClicked with text: " + statusText);
	}
	
	class PostToTwitter extends AsyncTask<String, Void, String> {
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			dialog = new ProgressDialog(StatusActivity.this);
			dialog.setMessage("Please wait while updating...");
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				Twitter twitter = ((YambaApp)getApplication()).getTwitter();
				twitter.setStatus(params[0]);
				Log.d(TAG, "Successfully posted: " + params[0]);
				return "Successfully posted: " + params[0];
			} catch (TwitterException e) {
				Log.e(TAG, "Died", e);
				e.printStackTrace();
				return  "Failed posting: " + params[0]; 
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			dialog.cancel();			
			Toast.makeText(StatusActivity.this, 
					result, 
					Toast.LENGTH_LONG).show();
		}
		
	}
	
	//--- LocationListener callbacks

	@Override
	public void onLocationChanged(Location location) {
		this.location = location;
		Log.d(TAG, "onLocationChanged: " + location.toString());
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}
	
}
