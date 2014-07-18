/**
 * 
 */
package com.cubieline.plana.activity;

import java.io.IOException;

import android.app.Activity;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cubieline.plana.R;
import com.cubieline.plana.utils.ExternalStorageUtils;
import com.cubieline.plana.view.VideoPreview;

/**
 * @author LeiGuoting
 *
 */
public class CameraActivity extends BaseActivity implements OnClickListener{
	
	private static final String TAG = CameraActivity.class.getSimpleName();
	
	private Camera camera;
	
	private VideoPreview preview;
	
	private Button actionBtn;
	
	private MediaRecorder recorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		
		setContentView(R.layout.activity_preview_for_camera);
		actionBtn = (Button)findViewById(R.id.action_btn);
		actionBtn.setOnClickListener(this);
		actionBtn.setSelected(false);
		
		preview = (VideoPreview) findViewById(R.id.camera_preview);
		int numberOfCameras = Camera.getNumberOfCameras();
		if(0 < numberOfCameras){
			if(safeCameraOpen(0)){
				preview.setCamera(camera);				
			}
		}
	}
	
	
	
	private boolean safeCameraOpen(int cameraId) {
	    boolean qOpened = false;
	  
	    try {
	        releaseCameraAndPreview();
	        camera = Camera.open(cameraId);
	        
	        if(camera != null){
	        	qOpened = true;
	        	setCameraDisplayOrientation(this, cameraId, camera);
	        }
	    } catch (Exception e) {
	        Log.e(getString(R.string.app_name), "failed to open Camera", e);
	    }

	    return qOpened;    
	}
	
	private void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
	     android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
	     android.hardware.Camera.getCameraInfo(cameraId, info);
	     int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	     int degrees = 0;
	     switch (rotation) {
	         case Surface.ROTATION_0: degrees = 0; break;
	         case Surface.ROTATION_90: degrees = 90; break;
	         case Surface.ROTATION_180: degrees = 180; break;
	         case Surface.ROTATION_270: degrees = 270; break;
	     }

	     int result;
	     if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	         result = (info.orientation + degrees) % 360;
	         result = (360 - result) % 360;  // compensate the mirror
	     } else {  // back-facing
	         result = (info.orientation - degrees + 360) % 360;
	     }
	     camera.setDisplayOrientation(result);
	 }
	
	private void releaseCameraAndPreview() {
		preview.setCamera(null);
	    if (camera != null) {
	    	camera.release();
	    	camera = null;
	    }
	}
	
	@Override
	public void onClick(View view) {
		
		switch(view.getId()){
		default : return;
		
		case R.id.action_btn:
			boolean isSelected = actionBtn.isSelected();
			if(isSelected){
				actionBtn.setSelected(false);
				actionBtn.setText(R.string.start);
				stopVideos();
			}else{
				actionBtn.setSelected(true);
				actionBtn.setText(R.string.stop);
				capturingVideos();
			}
			break;
		}
	}
	
	private void stopVideos(){
		if(null == recorder){
			return;
		}
		
		recorder.stop();
		recorder.release();
		//camera.lock();
		camera.stopPreview();
		releaseCameraAndPreview();
		
		setResult(RESULT_OK);
		finish();
	}
	
	private void capturingVideos(){
		//camera.unlock();
		recorder = new MediaRecorder();
		recorder.setCamera(camera);
		recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		
		//recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		//recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		//recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263); 
		
		
		recorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		
		String rootPath = ExternalStorageUtils.getExternalStorageDirectory(ExternalStorageUtils.DIRECTORY_CACHE).getAbsolutePath();
		String filePath = new StringBuilder(rootPath).append("/").append(System.currentTimeMillis()).append(".mp4").toString();
		recorder.setOutputFile(filePath);
		
		recorder.setOnInfoListener(new OnInfoListener(){
			@Override
			public void onInfo(MediaRecorder recorder, int what, int extrat) {
				Log.d(TAG, "@onInfo what:" + what + ", extrat:" + extrat);
			}
		});
		
		recorder.setOnErrorListener(new OnErrorListener(){
			@Override
			public void onError(MediaRecorder recorder, int what, int extrat) {
				Log.e(TAG, "@onError what:" + what + ", extrat:" + extrat);
			}
		});
		
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			Log.e(TAG, "________", e);
		} catch (IOException e) {
			Log.e(TAG, "___________", e);
		}
		
		recorder.start();
	}
	
	@Override
	protected void onStop() {
		releaseCameraAndPreview();
		super.onStop();
	}
}
