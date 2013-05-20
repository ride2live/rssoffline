package ru.fallen.rssofflinefree;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class Utils {
	private static boolean debugOn = true;
	public static boolean isImagesOn (Context context) {
		boolean cut  = context.getSharedPreferences("prefs", 0).getBoolean("images", true);
		return cut;
	}
	public static void setImagesOn (Context context, boolean isImagesOn) {
		//System.out.println(st + " to prefs");
		context.getSharedPreferences("prefs", 0).edit().putBoolean("images", isImagesOn).commit();
	}
	public static void showLogV (String className, String message)
	{
		if (debugOn)
		Log.v(className, message);
	}
	/*ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

	if (mWifi.isConnected()) {
	    // Do whatever
	}
	WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
 	wallpaperDrawable = wallpaperManager.getDrawable();
 	mImageView.setImageURI(imagePath);
	*
	*/
	
}
