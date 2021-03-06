package com.sshlafman.yamba;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TimelineActivity extends ListActivity implements
		LoaderCallbacks<Cursor> {
	static final String TAG = "TimelineActivity";

	static final String[] FROM = { StatusProvider.C_USER,
			StatusProvider.C_TEXT, StatusProvider.C_CREATED_AT };
	static final int[] TO = { R.id.text_user, R.id.text_text,
			R.id.text_created_at };
	static final int STATUS_LOADER = 47;
	private SimpleCursorAdapter adapter;
	private TimelineReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		adapter = new SimpleCursorAdapter(this, R.layout.row, null, FROM, TO,
				0);
		adapter.setViewBinder(VIEW_BINDER);
		getLoaderManager().initLoader(STATUS_LOADER, null, this);

		setTitle(R.string.timeline);
		setListAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (receiver == null)
			receiver = new TimelineReceiver();
		registerReceiver(receiver, new IntentFilter(YambaApp.ACTION_NEW_STATUS));
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	static final ViewBinder VIEW_BINDER = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if (view.getId() != R.id.text_created_at)
				return false;

			long time = cursor.getLong(cursor
					.getColumnIndex(StatusProvider.C_CREATED_AT));
			CharSequence relativeTime = DateUtils
					.getRelativeTimeSpanString(time);

			((TextView) view).setText(relativeTime);

			return true;
		}
	};

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

		switch (item.getItemId()) {
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
			return true;

		case R.id.item_status_update:
			startActivity(new Intent(this, StatusActivity.class));
			return true;

		default:
			return false;
		}
	}

	class TimelineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			getLoaderManager().restartLoader(STATUS_LOADER, null,
					TimelineActivity.this);
			Log.d(TAG,
					"onReceive changeCursor with count: "
							+ intent.getIntExtra("count", 0));
		}

	}

	// -- LoaderManager.LoaderCallbacks
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		return new CursorLoader(this, StatusProvider.CONTENT_URI, null, null,
				null, StatusProvider.C_CREATED_AT + " DESC");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}
}
