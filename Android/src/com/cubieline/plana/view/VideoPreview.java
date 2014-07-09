/**
 * 
 */
package com.cubieline.plana.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author LeiGuoting
 *
 */
public class VideoPreview extends SurfaceView implements SurfaceHolder.Callback{

	public VideoPreview(Context context) {
		super(context);
	}
	
	public VideoPreview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
}
