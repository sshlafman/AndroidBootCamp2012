package com.sshlafman.yamba;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class TimelineActivity extends Activity {
	TextView textOut;
	Cursor cursor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		textOut = (TextView) findViewById(R.id.text_out);
		
		cursor = ((YambaApp)getApplication()).statusData.query();
		
		while (cursor.moveToNext()) {
			String user = cursor.getString( cursor.getColumnIndex(StatusData.C_USER) );
			String text = cursor.getString( cursor.getColumnIndex(StatusData.C_TEXT) );
			textOut.append( String.format("\n%s: %s", user, text) );
		}
	}

}
