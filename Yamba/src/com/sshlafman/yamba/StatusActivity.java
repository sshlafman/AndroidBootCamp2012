package com.sshlafman.yamba;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


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
		String statusText = editStatus.getText().toString();
		Log.d(TAG, "onClicked with text: " + statusText);
	}
}
