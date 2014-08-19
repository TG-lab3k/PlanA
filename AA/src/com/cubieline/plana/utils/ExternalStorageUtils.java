/**
 * 
 */
package com.cubieline.plana.utils;

import java.io.File;
import java.lang.ref.SoftReference;

import android.content.Context;
import android.os.Environment;

/**
 * @author LeiGuoting
 *
 */
public class ExternalStorageUtils {
	private static final String CORPORATION			= "cubieline";
	
	private static String packageName;
	
	public static final String DIRECTORY_CACHE		= "Cache";
	
	public static final String DIRECTORY_DOWNLOAD	= "Download";

	public static final String DIRECTORY_LOG		= "Log";
	
	private static SoftReference<File> rootRefer;

	public static void init(Context context){
		packageName = context.getPackageName();
	}
	
	public static File getRootDirectory(){
		File root = null;
		if(null != rootRefer && null != (root = rootRefer.get())){
			return root;
		}
		
		else{
			synchronized (ExternalStorageUtils.class) {
				if(null == rootRefer || null == (root = rootRefer.get())){
					File externalRoot = Environment.getExternalStorageDirectory();
					if(null == packageName){
						root = new File(externalRoot, CORPORATION);
					}else{
						root = new File(externalRoot, CORPORATION + File.separator + packageName);
					}
					
					if(!root.exists()){
						root.mkdirs();
					}
				}
			}
			
			return root;
		}
	}
	
	public static File getExternalStorageDirectory(String type){
		File root = getRootDirectory();
		File dir = new File(root, type);
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dir;
	}
}
