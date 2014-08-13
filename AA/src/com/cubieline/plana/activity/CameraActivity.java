/**
 * 
 */
package com.cubieline.plana.activity;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cubieline.plana.R;
import com.cubieline.plana.common.CameraHelper;
import com.cubieline.plana.utils.ExternalStorageUtils;

/**
 * @author LeiGuoting
 *
 */
public class CameraActivity extends BaseActivity implements OnClickListener, TextureView.SurfaceTextureListener{
	
	private static final String TAG = CameraActivity.class.getSimpleName();
	
	private Camera camera;
	
	//private VideoPreview preview;
	
	private TextureView preview;
	
	private Button actionBtn;
	
	private MediaRecorder recorder;
	
	private boolean isRecording = false;
	
	private boolean isRecorded;
	
	private String filePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		
		setContentView(R.layout.activity_preview_for_camera);
		actionBtn = (Button)findViewById(R.id.action_btn);
		actionBtn.setOnClickListener(this);
		isRecording = false;
		
		preview = (TextureView) findViewById(R.id.camera_preview);
		preview.setSurfaceTextureListener(this);
		/*
		int numberOfCameras = Camera.getNumberOfCameras();
		if(0 < numberOfCameras){
			if(safeCameraOpen(0)){
				preview.setCamera(camera);				
			}
		}
		*/
		
		/*
		camera = CameraHelper.getDefaultCameraInstance();
		int cameraId = 0;
		setCameraDisplayOrientation(this, cameraId, camera);

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalPreviewSize(mSupportedPreviewSizes,preview.getWidth(), preview.getHeight());

        // Use the same size for recording profile.
        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        camera.setParameters(parameters);
        try {
                // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
                // with {@link SurfaceView}
        	camera.setPreviewTexture(preview.getSurfaceTexture());
        	camera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
        }
        // END_INCLUDE (configure_preview)
        
        */
	}
	
	private void setCaptureButtonText(String title) {
		actionBtn.setText(title);
    }
	
	@SuppressWarnings("unused")
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
		//preview.setCamera(null);
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
			/*
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
			*/
			if (isRecording) {
				// stop recording and release camera
				recorder.stop();  // stop the recording
	            releaseMediaRecorder(); // release the MediaRecorder object
	            camera.lock();         // take camera access back from MediaRecorder

	            // inform the user that recording has stopped
	            setCaptureButtonText("Capture");
	            isRecording = false;
	            releaseCamera();
	            isRecorded = true;
			}
			
			else{
				AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){
					@Override
					protected Void doInBackground(Void... params) {
						// BEGIN_INCLUDE (configure_media_recorder)
						recorder = new MediaRecorder();

				        // Step 1: Unlock and set camera to MediaRecorder
						camera.unlock();
				        recorder.setCamera(camera);

				        // Step 2: Set sources
				        recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
				        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
						
						//recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
						//recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
						//recorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264); 

				        /*
				        Camera.Parameters parameters = camera.getParameters();
				        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
				        Camera.Size optimalSize = CameraHelper.getOptimalPreviewSize(mSupportedPreviewSizes,preview.getWidth(), preview.getHeight());

				        // Use the same size for recording profile.
				        CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
				        profile.videoFrameWidth = optimalSize.width;
				        profile.videoFrameHeight = optimalSize.height;
				        */
				        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
				        if(null != profile){
				        	recorder.setProfile(profile);	
				        }
				        
				        // Step 4: Set output file
				        String rootPath = ExternalStorageUtils.getExternalStorageDirectory(ExternalStorageUtils.DIRECTORY_CACHE).getAbsolutePath();
						filePath = new StringBuilder(rootPath).append("/").append(System.currentTimeMillis()).append(".mp4").toString();
						recorder.setOutputFile(filePath);
						//recorder.setOutputFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO).toString());
				        // END_INCLUDE (configure_media_recorder)

				        // Step 5: Prepare configured MediaRecorder
				        try {
				        	recorder.prepare();
				        } catch (IllegalStateException e) {
				            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
				            releaseMediaRecorder();
				            return null;
				        } catch (IOException e) {
				            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
				            releaseMediaRecorder();
				            return null;
				        }
				        
				        recorder.start();
				        isRecording = true;
						return null;
					}
					
					@Override
					protected void onPostExecute(Void result) {
						setCaptureButtonText("Stop");
					}
				};
				task.execute();
			}
			break;
		}
	}
	
	 private void releaseMediaRecorder(){
	        if (recorder != null) {
	            // clear recorder configuration
	        	recorder.reset();
	            // release the recorder object
	        	recorder.release();
	        	recorder = null;
	            // Lock camera for later use i.e taking it back from MediaRecorder.
	            // MediaRecorder doesn't need it anymore and we will release it if the activity pauses.
	        	camera.lock();
	        }
	    }

	    private void releaseCamera(){
	        if (camera != null){
	            // release the camera for other applications
	        	camera.release();
	        	camera = null;
	        }
	    }
	
	    
	private CamcorderProfile profile;
	
	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
		camera = CameraHelper.getDefaultCameraInstance();
		int cameraId = 0;
		setCameraDisplayOrientation(this, cameraId, camera);

        // We need to make sure that our preview and recording video size are supported by the
        // camera. Query camera to find all the sizes and choose the optimal size given the
        // dimensions of our preview surface.
        Camera.Parameters parameters = camera.getParameters();
        List<Camera.Size> mSupportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Camera.Size optimalSize = CameraHelper.getOptimalPreviewSize(mSupportedPreviewSizes,preview.getWidth(), preview.getHeight());

        // Use the same size for recording profile.
        profile = CamcorderProfile.get(CamcorderProfile.QUALITY_720P);
        profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
        profile.audioCodec = MediaRecorder.AudioEncoder.AMR_NB;
        profile.videoCodec = MediaRecorder.VideoEncoder.H264;
        profile.videoFrameWidth = optimalSize.width;
        profile.videoFrameHeight = optimalSize.height;

        
        // likewise for the camera object itself.
        parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
        camera.setParameters(parameters);
        try {
                // Requires API level 11+, For backward compatibility use {@link setPreviewDisplay}
                // with {@link SurfaceView}
        	camera.setPreviewTexture(surface);
        	camera.startPreview();
        } catch (IOException e) {
            Log.e(TAG, "Surface texture is unavailable or unsuitable" + e.getMessage());
        }
        // END_INCLUDE (configure_preview)
	}
	
	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// TODO Auto-generated method stub
		
	}
	
	@SuppressWarnings("unused")
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
	
	
	@SuppressWarnings("unused")
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
	
	@Override
	public void onBackPressed() {
		if(isRecorded){
			Intent intent = new Intent();
			intent.setData(Uri.parse(filePath));
			setResult(RESULT_OK, intent);
			finish();
		}
		
		else{
			super.onBackPressed();
		}
	}
}
