package com.sshlafman.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	static final String TAG = "Updater Service";
	static final String DELAY = "30"; // in seconds
	Twitter twitter;
	boolean running = false;

	@Override
	public void onCreate() {
		super.onCreate();
		
		Log.d(TAG, "onCreated");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		running = true;
		final int delay = Integer
				.parseInt(((YambaApp)getApplication()).prefs.getString("delay", DELAY));
		new Thread() {
			public void run() {
				try {
					while(running) {
						List<Status> timeline = 
								((YambaApp)getApplication()).getTwitter().getPublicTimeline();

						for (Status status : timeline) {
							((YambaApp)getApplication()).statusData.insert(status);
							Log.d(TAG, String.format("%s: %s", status.user.name,
									status.text));
						}
						Thread.sleep(delay * 1000);
					}
				} catch (TwitterException e) {
					Log.e(TAG, "Failed because of network error");
				} catch (InterruptedException e) {
					Log.d(TAG, "Updater interrupted", e);
				}
			}
		}.start();
		
		Log.d(TAG, "onStarted");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		running = false;
		Log.d(TAG, "onDestroyed");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
}
