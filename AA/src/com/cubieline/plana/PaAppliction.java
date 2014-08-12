/**
 * 
 */
package com.cubieline.plana;

import com.cubieline.plana.utils.ExternalStorageUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

/**
 * @author LeiGuoting
 *
 */
public class PaAppliction extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		Context context = getApplicationContext();
		ExternalStorageUtils.init(context);
		
		registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks(){
			
			@Override
			public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
				
			}
			
			@Override
			public void onActivityDestroyed(Activity activity) {
				
			}
			
			@Override
			public void onActivityPaused(Activity activity) {
				
			}
			
			@Override
			public void onActivityResumed(Activity activity) {
				
			}
			
			@Override
			public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
				
			}
			
			@Override
			public void onActivityStarted(Activity activity) {
				
			}
			
			@Override
			public void onActivityStopped(Activity activity) {
				
			}
		});
	}
}
