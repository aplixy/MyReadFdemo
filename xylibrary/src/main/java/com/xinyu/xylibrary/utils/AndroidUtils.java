package com.xinyu.xylibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@SuppressLint("NewApi")
public class AndroidUtils {

	/**
	 * CPU信息
	 */
	static String cpuInfoStr[] = null;
	/**
	 * Google Play包名
	 */
	public static final String GP_PACKAGE_NAME = "com.android.vending";

	static int VersionCode = 0;

	/** uid默认字符串前缀 **/
	private static final String DEFAULT_STRING = "0000";

	static String UID = null;

	/**
	 * 生成uid
	 * 
	 * @param context
	 * @return
	 */
	public static String createUid(Context context) {
		// 简单规则
		String imei = getImeiId(context);
		String id = imei != null ? imei : UUID.randomUUID().toString();
		String uid = DEFAULT_STRING + id + System.currentTimeMillis();
		return uid;
	}

	/***
	 * 获取设备的id,uuid
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		String device_id = "";
		try {

			/*device_id = getImeiId(context);

			if (TextUtils.isEmpty(device_id)) {
				device_id = getMacAddress(context);
			}*/

			if (TextUtils.isEmpty(device_id)) {
				//device_id = SharedPrefUtils.getString(context, "uid");

			}
			if (TextUtils.isEmpty(device_id)) {
				device_id = UUID.randomUUID().toString();
				//PreferencesUtils.putString(context, "uid", device_id);
			}
			return device_id;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取uid
	 * 
	 * @param context
	 * @return
	 */
	public static String getUid(Context context) {
		if (UID != null) {
			return UID;
		}
		//UID = PreferencesUtils.getString(context, "uid");
		if (UID == null) {
			UID = createUid(context);
		}
		return UID;
	}

	/**
	 * 
	 * 获取设备唯一id
	 * 
	 * @param context
	 */
	public static String getImeiId(Context context) {
		try {
			final TelephonyManager telephonyManager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			final String imei_id = telephonyManager.getDeviceId();
			return imei_id;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}

	/**
	 * 获取手机电话号码
	 * 
	 * @param context
	 * @return
	 */
	public static String getPhoneNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	/**
	 * 获取MAC地址
	 * 
	 * @return
	 */
	public static String getMacAddress(Context context) {
		String macAddress = null;
		try {
			WifiManager manager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == manager ? null : manager
					.getConnectionInfo());
			if (info != null) {
				macAddress = info.getMacAddress();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return macAddress;
	}

	/**
	 * 获取SIM卡的IMSI码
	 * 
	 * @param context
	 * @return
	 */
	public static String getImsiId(Context context) {
		TelephonyManager telManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = telManager.getSubscriberId();
		return imsi;
	}

	/**
	 * 获得当前的版本
	 * */
	public static int getVersionCode(Context context) {
		if (VersionCode != 0) {
			return VersionCode;
		}
		try {
			PackageInfo pinfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_CONFIGURATIONS);
			int version = pinfo.versionCode;
			VersionCode = version;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return VersionCode;
	}

	/**
	 * 获得当前的版本
	 * */
	public static String getVersionName(Context context) {
		String versionName = null;
		try {
			PackageInfo pinfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(),
							PackageManager.GET_CONFIGURATIONS);
			versionName = pinfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 根据sim卡网络获取国家码
	 * 
	 * @param context
	 * @return
	 */
	public static String getNetworkCountryIso(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyManager.getNetworkCountryIso();
	}

	/**
	 * 获取国家码，先根据sim卡网络获取，再根据设备的设置地区
	 * 
	 * @param context
	 * @return
	 */
	public static String getCountryId(Context context) {
		String contryId = getNetworkCountryIso(context);
		if (TextUtils.isEmpty(contryId)) {
			contryId = Locale.getDefault().getCountry();
		}
		return contryId;
	}

	/**
	 * sim卡是否可用
	 * @param 
	 * @return
     */
	public static boolean isCanUseSim() {
		//TelephonyManager manager = (TelephonyManager) Application.getApplication().getSystemService(Context.TELEPHONY_SERVICE);
		//if (manager != null) return TelephonyManager.SIM_STATE_READY == manager.getSimState();
		
		return false;
	}

	/**
	 * 解析apk文件获取版本号
	 * 
	 * @param context
	 * @param filePath
	 *            apk文件路径
	 * @return
	 */
	public static int getVersionCodeFromAPK(Context context, String filePath) {
		PackageManager pm = context.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(filePath,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			return info.versionCode;
		}
		return -1;
	}

	/**
	 * 卸载应用
	 * 
	 * @param context
	 * @param packageName
	 *            应用包名
	 */
	public static void unInstalledApk(Context context, String packageName) {
		Uri packageURI = Uri.parse("package:" + packageName);
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
		uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(uninstallIntent);
	}

	/**
	 * 打开应用
	 * 
	 * @param context
	 * @param packageName
	 *            应用包名
	 */
	public static void openApp(Context context, String packageName) {
		Intent intent = context.getPackageManager().getLaunchIntentForPackage(
				packageName);
		// 如果该程序不可启动（像系统自带的包，有很多是没有入口的）会返回NULL
		if (intent != null) {
			context.startActivity(intent);
		} else {
		}
	}

	/**
	 * 安装应用
	 * 
	 * @param context
	 * @param file
	 *            安装包地址
	 */
	public static void installedApk(Context context, File file) {

		if (file != null && file.exists()) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}
	}

	/**
	 * 查询手机内非系统应用
	 * 
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getAllNotSystemApps(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
		// 获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = paklist.get(i);
			ApplicationInfo appInfo = pak.applicationInfo;
			// 判断是否为非系统预装的应用程序
			if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) {
				// customs applications
				apps.add(pak);
			}
		}
		return apps;
	}

	/**
	 * 获取系统应用和预装应用
	 * 
	 * @param context
	 * @return
	 */
	public static List<PackageInfo> getAllSystemApps(Context context) {
		List<PackageInfo> apps = new ArrayList<PackageInfo>();
		PackageManager pManager = context.getPackageManager();
		// 获取手机内所有应用
		List<PackageInfo> paklist = pManager.getInstalledPackages(0);
		for (int i = 0; i < paklist.size(); i++) {
			PackageInfo pak = paklist.get(i);
			// 判断是否为非系统预装的应用程序
			if ((pak.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0
					&& pak.applicationInfo.uid > 10000) {
				apps.add(pak);
			}
		}
		return apps;
	}

	/**
	 * 判断某一服务是否在运行
	 * 
	 * @param context
	 * @param servicePackageName
	 * @return
	 */
	public static boolean isServiceAlive(Context context,
			String servicePackageName) {
		final ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		final ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals(servicePackageName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 分享功能
	 * 
	 * @param context
	 * @param shareMsg
	 * @param activityTitle
	 */
	public static void share(Context context, String shareMsg,
							 String activityTitle) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, activityTitle);
		intent.putExtra(Intent.EXTRA_TEXT, shareMsg);
		context.startActivity(Intent.createChooser(intent, activityTitle));
	}

	/**
	 * 获取手机品牌
	 * 
	 * @return
	 */
	public static String getPhoneBrand() {
		return Build.BRAND;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public static String getPhoneModel() {
		return Build.MODEL;
	}

	/**
	 * 获取处理器信息
	 * 
	 * @return
	 */
	public static String[] getCpuInfo() {
		if (cpuInfoStr != null) {
			return cpuInfoStr;
		}
		String str1 = "/proc/cpuinfo";
		String str2 = "";
		String[] cpuInfo = { "", "" };
		String[] arrayOfString;
		try {
			FileReader fr = new FileReader(str1);
			BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			for (int i = 2; i < arrayOfString.length; i++) {
				cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
			}
			str2 = localBufferedReader.readLine();
			arrayOfString = str2.split("\\s+");
			cpuInfo[1] += arrayOfString[2];
			localBufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cpuInfoStr = cpuInfo;
		return cpuInfo;
	}

	/**
	 * 获取WIFI热点名字
	 * 
	 * @return
	 */
	public static String getWifiSSID(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (wifi == null)
			return null;
		WifiInfo info = wifi.getConnectionInfo();
		return info.getSSID();
	}

	/**
	 * 获取手机当前可用内存大小
	 * 
	 * @param context
	 * @return
	 */
	public static String getAvailMemoryToString(Context context) {
		return Formatter.formatFileSize(context, getAvailMemory(context));
	}

	/**
	 * 获取手机总内存大小
	 * 
	 * @param context
	 * @return
	 */
	public static String getTotalMemoryToString(Context context) {
		return Formatter.formatFileSize(context, getTotalMemory(context));
	}

	/**
	 * 获取手机当前可用内存大小
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo mi = new MemoryInfo();
		am.getMemoryInfo(mi);
		return mi.availMem;
	}

	/**
	 * 获取手机总内存大小
	 * 
	 * @param context
	 * @return
	 */
	public static long getTotalMemory(Context context) {
		String path = "/proc/meminfo";
		long initial_memory = 0;
		BufferedReader localBufferedReader = null;
		try {
			FileReader localFileReader = new FileReader(path);
			localBufferedReader = new BufferedReader(localFileReader, 8192);
			String[] arrayOfString = localBufferedReader.readLine().split(
					"\\s+");
			initial_memory = Long.valueOf(arrayOfString[1]).intValue() * 1024;
			return initial_memory;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (localBufferedReader != null)
				try {
					localBufferedReader.close();
				} catch (IOException e) {
				}
		}
		return 0;
	}

	/**
	 * 获取当前应用能占用最大内存，单位为字节
	 */
	public long getRuntimeRAM(Context context) {
		return Runtime.getRuntime().maxMemory();
	}

	/**
	 * 获取当前应用能占用最大内存格式化输出
	 * 
	 * @param context
	 * @return
	 */
	public String getRuntimeRAMToString(Context context) {
		return Formatter.formatFileSize(context, Runtime.getRuntime()
				.maxMemory());
	}

	/**
	 * 获取当前应用已占用内存，单位为字节
	 * 
	 * @param context
	 * @return
	 */
	public long getAvailableRAMForApp(Context context) {
		ActivityManager activityManager = ((ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE));
		RunningAppProcessInfo processInfo = new RunningAppProcessInfo();
		ActivityManager.getMyMemoryState(processInfo);
		Debug.MemoryInfo memoryInfo[] = activityManager
				.getProcessMemoryInfo(new int[] { processInfo.pid });
		return memoryInfo[0].getTotalPrivateDirty() * 1024;
	}

	/**
	 * 获取当前应用已占用内存格式化输出
	 * 
	 * @param context
	 * @return
	 */
	public String getAvailableRAMForAppToString(Context context) {
		return Formatter
				.formatFileSize(context, getAvailableRAMForApp(context));
	}

	/**
	 * 获取系统版本
	 * 
	 * @return
	 */
	public static String getPhoneRelease() {
		return "AndroidOS" + Build.VERSION.RELEASE;
	}

	/**
	 * 根据包名检查是否安装此应用
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isInstalledByPackageName(Context context,
			String packageName) {
		try {
			Context outContext = context.createPackageContext(packageName,
					Context.CONTEXT_IGNORE_SECURITY);
			if (outContext == null) {
				return false;
			}
			return true;
		} catch (NameNotFoundException e) {

		}
		return false;
	}

	/**
	 * 是否安装Google Play
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isInstallMarketGp(Context context) {
		return isInstalledByPackageName(context, GP_PACKAGE_NAME);
	}

	/**
	 * Converts an intent into a {@link Bundle} suitable for use as fragment
	 * arguments.
	 */
	public static Bundle intentToFragmentArguments(Intent intent) {
		final Bundle arguments = new Bundle();
		if (intent == null) {
			return arguments;
		}

		final Uri data = intent.getData();
		if (data != null) {
			arguments.putParcelable("_uri", data);
		}

		final Bundle extras = intent.getExtras();
		if (extras != null) {
			arguments.putAll(intent.getExtras());
		}

		return arguments;
	}

	// 检测网络连接
	public static boolean checkConnection(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isAvailable();
		}
		return false;
	}

	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null && activeNetInfo.getTypeName().equals("WIFI")) {
			return true;
		}
		return false;
	}

//	/**
//	 * 从网上获取内容get方式
//	 * 
//	 * @param url
//	 * @return
//	 * @throws IOException
//	 * @throws ClientProtocolException
//	 */
//	public static String getStringFromUrl(String url)
//			throws ClientProtocolException, IOException {
//		HttpGet get = new HttpGet(url);
//		HttpClient client = new DefaultHttpClient();
//		HttpResponse response = client.execute(get);
//		HttpEntity entity = response.getEntity();
//		return EntityUtils.toString(entity, "UTF-8");
//	}

	/**
	 * copy文件到剪切板
	 * 
	 * @param context
	 * @param text
	 * @param successToast
	 */
	public static void copyToClipboard(Context context, String text,
									   String successToast) {
		ClipData clipData = ClipData.newPlainText("yjl_copy", text);
		android.content.ClipboardManager clipboardManager = (android.content.ClipboardManager) context
				.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setPrimaryClip(clipData);
		if (!TextUtils.isEmpty(successToast)) {
			ToastUtils.showShortToast(context, successToast);
		}
	}

	

	/**
	 * 程序是否在前台运行
	 * 
	 * @return
	 */
	public static boolean  isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (RunningAppProcessInfo appProcess : appProcesses) {
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				Logger.e("isAppOnForeground", "前台" + appProcess.processName);
				return true;
			}
		}
		Logger.e("isAppOnForeground", "后台");
		return false;
	}
	public static boolean isAppAlive(Context context, String packageName) {
		boolean isAppRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
		for (ActivityManager.RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
				isAppRunning = true;
				// find it, break
				break;
			}
		}
		return isAppRunning;
	}

	/**
	 * 跳转到系统短信发送页
	 * @param activity
	 * @param smsNumber
	 * @param smsBody
	 * @param requestCode
	 */
	public static void gotoSendSmsActivity(Activity activity, String smsNumber, String smsBody, int requestCode) {
		
		activity.startActivityForResult(getSmsIntent(smsNumber, smsBody), requestCode);
	}

	/**
	 * 跳转到系统短信发送页
	 * @param fragment
	 * @param smsNumber
	 * @param smsBody
	 * @param requestCode
	 */
	public static void gotoSendSmsActivity(Fragment fragment, String smsNumber, String smsBody, int requestCode) {

		fragment.startActivityForResult(getSmsIntent(smsNumber, smsBody), requestCode);
	}
	
	private static Intent getSmsIntent(String smsNumber, String smsBody) {
		Uri smsToUri = Uri.parse("smsto:" + smsNumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", smsBody);
		
		return intent;
	}
}