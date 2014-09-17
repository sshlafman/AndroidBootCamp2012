package com.sshlafman.yamba;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class StatusProvider extends ContentProvider {
	public static final String TAG = "StatusProvider";
	public static final String AUTHORITY = "content://com.sshlafman.yamba.provider";
	public static final Uri CONTENT_URI = Uri.parse(AUTHORITY);

	DbHelper dbHelper;
	SQLiteDatabase db;

	@Override
	public boolean onCreate() {
		dbHelper = new DbHelper(getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		if (uri.getLastPathSegment() == null) {
			return "vnd.android.cursor.item/vnd.sshlafman.yamba.status";
		} else {
			return "vnd.android.cursor.dir/vnd.sshlafman.yamba.status";
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		db = dbHelper.getWritableDatabase();
		db.insertWithOnConflict(StatusData.TABLE, null, values,
				SQLiteDatabase.CONFLICT_IGNORE);
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

}
