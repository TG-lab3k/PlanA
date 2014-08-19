/**
 * 
 */
package com.cubieline.plana.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author LeiGuoting
 *
 */
public class GameDisplay extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback{
	
	public GameDisplay(Context context) {
		super(context);
	}
	
	public GameDisplay(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPreviewFrame(byte[] arg0, Camera arg1) {
		// TODO Auto-generated method stub
		
	}
}
