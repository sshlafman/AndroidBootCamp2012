package com.sshlafman.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class StatusActivity extends Activity {
	static final String TAG = "StatusActivity";
	EditText editStatus;
	
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        
//        Debug.startMethodTracing("Yamba.trace");
        
        Log.d(TAG, "onCreate with Bundle: " + bundle);
        
        setContentView(R.layout.status);
        
        editStatus = (EditText)findViewById(R.id.edit_status);
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

		@Override
		protected String doInBackground(String... params) {
			try {
				Twitter twitter = new Twitter("student", "password");
				twitter.setAPIRootUrl("http://yamba.marakana.com/api");
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
			
			Toast.makeText(StatusActivity.this, 
					result, 
					Toast.LENGTH_LONG).show();
		}
		
	}
	
	// Menu Stuff
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intentUpdater = new Intent(this, UpdaterService.class);
		Intent intentRefresh = new Intent(this, RefreshService.class);
		
		switch(item.getItemId()) {
			case R.id.item_start_service:
				startService(intentUpdater);
				return true;

			case R.id.item_stop_service:
				stopService(intentUpdater);
				return true;

			case R.id.item_refresh:
				startService(intentRefresh);
				return true;
				
			case R.id.item_prefs:
				startActivity(new Intent(this, PrefsActivity.class));
				
			default:
				return false;
		}
	}
	
}
