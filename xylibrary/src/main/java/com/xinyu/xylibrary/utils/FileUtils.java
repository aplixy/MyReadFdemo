package com.xinyu.xylibrary.utils;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixinyu on 2017/5/9.
 */

public class FileUtils {
	
	public static final String APP_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "MyReadFDemo" + File.separator;

	public static void copyAssetsToDst(Context context, String srcPath, String dstPath) {
		InputStream is = null;
		FileOutputStream fos = null;
		try {
			String fileNames[] = context.getAssets().list(srcPath);
			if (fileNames.length > 0) {
				File file = new File(Environment.getExternalStorageDirectory(), dstPath);
				if (!file.exists()) file.mkdirs();
				for (String fileName : fileNames) {
					if (!srcPath.equals("")) { // assets 文件夹下的目录
						copyAssetsToDst(context, srcPath + File.separator + fileName, dstPath + File.separator + fileName);
					} else { // assets 文件夹
						copyAssetsToDst(context, fileName, dstPath + File.separator + fileName);
					}
				}
			} else {
				File outFile = new File(Environment.getExternalStorageDirectory(), dstPath);
				is = context.getAssets().open(srcPath);
				fos = new FileOutputStream(outFile);
				byte[] buffer = new byte[1024];
				int byteCount;
				while ((byteCount = is.read(buffer)) != -1) {
					fos.write(buffer, 0, byteCount);
				}
				fos.flush();
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != is) is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (null != fos) fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void copySingleAssetsToDst(Context context,String srcFileName, String dstFileName) {
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {

			File file = new File(dstFileName);
			File parentFile = file.getParentFile();
			if (!parentFile.exists()) parentFile.mkdirs();
			
			outputStream = new FileOutputStream(dstFileName);
			inputStream = context.getAssets().open(srcFileName);
			
			byte[] buffer = new byte[1024];
			int length = inputStream.read(buffer);
			while (length > 0) {
				outputStream.write(buffer, 0, length);
				length = inputStream.read(buffer);
			}

			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != inputStream) inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				if (null != outputStream) outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	

	public static List<File> getSuffixFile(String filePath, String suffix) {

		List<File> files = null;

		File f = new File(filePath);

		if (!f.exists()) {
			return null;
		}

		File[] subFiles = f.listFiles();
		for (File subFile : subFiles) {
			if(subFile.isFile() && subFile.getName().endsWith(suffix)){
				if (null == files) files = new ArrayList<>();
				files.add(subFile);
			} else if(subFile.isDirectory()){
				List<File> recursionFiles = getSuffixFile(subFile.getAbsolutePath(), suffix);
				if (null != recursionFiles && null == files) {
					files = new ArrayList<>();
				}

				if (null != recursionFiles) files.addAll(recursionFiles);
			} else{
				//非指定目录文件 不做处理
			}

		}

		return files;
	}
	
	public static List<File> getSuffixesFile(String filePath, String[] suffixes) {
		if (null == filePath) return null;
		
		if (null == suffixes) suffixes = new String[1];

		List<File> files = null;

		File f = new File(filePath);

		if (!f.exists()) {
			return null;
		}

		File[] subFiles = f.listFiles();
		for (File subFile : subFiles) {
			if(subFile.isFile()){
				boolean isSuffix = false;
				for (String suffix : suffixes) {
					if (subFile.getName().endsWith(suffix)) {
						isSuffix = true;
						break;
					}
				}
				
				if (isSuffix) {
					if (null == files) files = new ArrayList<>();
					files.add(subFile);
				}
			} else {
				List<File> recursionFiles = getSuffixesFile(subFile.getAbsolutePath(), suffixes);
				if (null != recursionFiles) {
					if (null == files) files = new ArrayList<>();
					files.addAll(recursionFiles);
				}
			}

		}

		return files;
	}
	
	public static void getSuffixesFile(final String filePath, final String[] suffixes, final FileResultListener listener) {
		if (null == listener) return;
		
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				listener.onResult((List)msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<File> files = getSuffixesFile(filePath, suffixes);
				Message msg = handler.obtainMessage();
				msg.obj = files;
				handler.sendMessage(msg);
			}
		}).start();
	}
	public static void getSuffixesFile(final String[] suffixes, final FileResultListener listener) {
		getSuffixesFile(Environment.getExternalStorageDirectory().getAbsolutePath(), suffixes, listener);
	}

	public interface FileResultListener {
		void onResult(List<File> files);
	}


	public static void saveFile(String destFileName, String str) {
		if (destFileName == null || destFileName.length() == 0) {
			destFileName = "temp.txt";
		}
		
		String filePath = destFileName;
//		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//		if (hasSDCard) { // SD卡根目录的hello.text  
//			filePath = Environment.getExternalStorageDirectory().toString() + File.separator + destFileName;
//		} else { // 系统下载缓存根目录的hello.text  
//			filePath = Environment.getDownloadCacheDirectory().toString() + File.separator + destFileName;
//		}
		File file = new File(filePath);
		FileOutputStream outStream = null;
		try {
			if (!file.exists()) {
				File dir = new File(file.getParent());
				dir.mkdirs();
				//Logger.d("file.mkdirs()--->" + file.mkdirs());
				if (!file.createNewFile()) {
					file.delete();
					file.createNewFile();
				}
			}

			outStream = new FileOutputStream(file);
			outStream.write(str.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != outStream) outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String readFileByLines(String fileName) {
		FileInputStream file = null;
		BufferedReader reader = null;
		InputStreamReader inputFileReader = null;
		StringBuilder content = new StringBuilder();
		String tempString = null;
		try {
			file = new FileInputStream(fileName);
			inputFileReader = new InputStreamReader(file, "utf-8");
			reader = new BufferedReader(inputFileReader);
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				content.append(tempString);
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (inputFileReader != null) {
				try {
					inputFileReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return content.toString();
	}

}
