package brice_bastien.epicture;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	public static final String KEY_PREF_EXAMPLE_SWITCH = "example_switch";
	public static final String KEY_PREF_COMMENTARY_NEW = "commentary_sort";
	public static final String KEY_PREF_FEED_SECTION = "feed_section";
	public static final String KEY_PREF_FEED_SORT = "feed_sort";
	public static final String KEY_PREF_SEARCH_SORT = "search_sort";
	public static final String KEY_PREF_LOW_DATA = "low_data";
	public static final String KEY_PREF_FAVORITE_SORT = "favorite_sort";
	public static final String KEY_PREF_GRID_VIEW = "grid_view";
	public static final String KEY_PREF_GRID_COLUMN = "grid_column";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
		if (switchPref) {
			setTheme(R.style.AppTheme_DARK);
			getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccentDarker));
		} else {
			setTheme(R.style.AppTheme);
			getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
		}

		sharedPrefs.registerOnSharedPreferenceChangeListener(this);

		getSupportFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment())
				.commit();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH)) {
			if (sharedPreferences.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false)) {
				setTheme(R.style.AppTheme_DARK);
				getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccentDarker));
			} else {
				setTheme(R.style.AppTheme);
				getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
			}
			recreate();
		}

	}
}
