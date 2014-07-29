package com.cubieline.plana.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cubieline.plana.R;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment implements OnClickListener{
		
		private TextView resultTxtv;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			rootView.findViewById(R.id.start_btn).setOnClickListener(this);
			resultTxtv = (TextView)rootView.findViewById(R.id.result_txtv);
			return rootView;
		}
		
		@Override
		public void onClick(View view) {
			
			switch(view.getId()){
			default : return;
			
			case R.id.start_btn :
				Activity activity = getActivity();
				Intent intent = new Intent(activity, CameraActivity.class);
				activity.startActivityForResult(intent, REQUEST_CAPTURE_VIDEO);
				break;
				
			case R.id.result_txtv :
				activity = getActivity();
				intent = new Intent(activity, CameraActivity.class);
				String path = resultTxtv.getText().toString();
				Uri uri = Uri.parse(path);
				intent.setData(uri);
				activity.startActivityForResult(intent, REQUEST_CAPTURE_VIDEO);
				break;
			}
			
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			
			switch(requestCode){
			default : super.onActivityResult(requestCode, resultCode, data); 
				break;
				
			case REQUEST_CAPTURE_VIDEO :
				if(RESULT_OK == resultCode){
					Uri path = data.getData();
					resultTxtv.setText(path.toString());
				}
				break;
			}
			
		}
	}

}
