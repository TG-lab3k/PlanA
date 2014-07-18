/**
 * 
 */
package com.cubieline.plana.view;

import java.io.IOException;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author LeiGuoting
 *
 */
public class VideoPreview extends SurfaceView implements SurfaceHolder.Callback{
	private static final String TAG = VideoPreview.class.getSimpleName();
	
	private SurfaceHolder holder;
	
    private Camera camera;
    

	public VideoPreview(Context context) {
		super(context);
	}
	
	public VideoPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setCamera(Camera camera){
		this.camera = camera;
		holder = getHolder();
		holder.addCallback(this);
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (holder.getSurface() == null){
	          // preview surface does not exist
	          return;
	        }

	        // stop preview before making changes
	        try {
	        	camera.stopPreview();
	        } catch (Exception e){
	        	Log.e(TAG, "", e);
	          // ignore: tried to stop a non-existent preview
	        }

	        // set preview size and make any resize, rotate or
	        // reformatting changes here

	        // start preview with new settings
	        try {
	        	camera.setPreviewDisplay(holder);
	        	camera.startPreview();
	        } catch (Exception e){
	            Log.e(TAG, "Error starting camera preview: " + e.getMessage());
	        }
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			camera.setPreviewDisplay(holder);
			camera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
}
