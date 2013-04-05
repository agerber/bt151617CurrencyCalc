package edu.uchicago.cs.gerber.hw2;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefsMgr {

	
	public static void setLocaleInt(SharedPreferences shp, String strLocale, int nPos) {

		Editor editor = shp.edit();
		editor.putInt(strLocale, nPos);
		editor.commit();
	}

	public static int getLocaleInt(SharedPreferences shp, String strLocale) {

		return shp.getInt(strLocale, -99);
		
	}
	
}
