package brice_bastien.epicture.ImgurPicture;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import brice_bastien.epicture.SettingsActivity;

public class ImgurPicture {

	private static final String small_square = "s";
	private static final String big_square = "b";
	private static final String small = "t";
	private static final String medium = "m";
	private static final String big = "l";
	private static final String huge_picture = "h";

	private String original_url;

	public ImgurPicture(String original_url) {
		this.original_url = original_url;
	}

	public String getSmall() {
		return getImgLink(addType(original_url, small));
	}

	public String getMedium() {
		return getImgLink(addType(original_url, medium));
	}

	public String getBig() {
		return getImgLink(addType(original_url, big));
	}

	public String getSmallSquare() { return getImgLink(addType(original_url, small_square)); }

	public String getBigSquare() { return getImgLink(addType(original_url, big_square)); }

	public String getUrl(Context context) {
		String url;

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Boolean lowData = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_LOW_DATA, false);

		if (lowData) {
			return getMedium();
		}
		if (original_url.endsWith(".mp4")) {
			url = addType(original_url, huge_picture);
		} else if (original_url.endsWith(".gifv")) {
			url = addType(original_url, huge_picture);
		} else
			url = original_url;
		return getImgLink(url);
	}

	private String addType(String url, String type) {
		if (url.endsWith(".gif")) {
			return url;
		}
		StringBuilder str = new StringBuilder(url);
		int last = url.lastIndexOf('.');
		str.insert(last, type);

		return str.toString();
	}

	private String getImgLink(String url) {
		if (url.endsWith(".mp4")) {
			url = url.replace(".mp4", ".jpg");
		} else if (url.endsWith(".gifv")) {
			url = url.replace(".gifv", ".jpg");
		}
		return url;
	}

}
