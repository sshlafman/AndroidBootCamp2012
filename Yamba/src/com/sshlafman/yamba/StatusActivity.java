package com.sshlafman.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class StatusActivity extends ActionBarActivity {
	static final String TAG = "StatusActivity";
	EditText editStatus;
	
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.status);
        
        Log.d(TAG, "onCreate with Bundle: " + bundle);
        
        editStatus = (EditText)findViewById(R.id.edit_status);
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.status, menu);
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
}
