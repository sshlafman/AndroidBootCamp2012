package com.sshlafman.yamba;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class TimelineActivity extends Activity {
	static final String[] FROM = { StatusData.C_USER, StatusData.C_TEXT, StatusData.C_CREATED_AT };
	static final int[] TO = {R.id.text_user, R.id.text_text, R.id.text_created_at };
	ListView list;
	Cursor cursor;
	SimpleCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timeline);
		
		list = (ListView) findViewById(R.id.list);
		
		cursor = ((YambaApp)getApplication()).statusData.query();
		
		adapter = new SimpleCursorAdapter(this, R.layout.row,
                                           cursor, FROM, TO, 0);
		adapter.setViewBinder(VIEW_BINDER);
		
		list.setAdapter(adapter);
		
	}
	
	static final ViewBinder VIEW_BINDER = new ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if( view.getId() != R.id.text_created_at ) return false;
			
			long time = cursor.getLong( cursor.getColumnIndex(StatusData.C_CREATED_AT));
			CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(time);
			
			((TextView)view).setText(relativeTime);
			
			return true;
		}
	};
}
