package brice_bastien.epicture;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;


public class SettingsFragment extends PreferenceFragmentCompat {

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.preferences, rootKey);
		PreferenceManager.setDefaultValues(getContext(), R.xml.preferences, false);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
		Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
		if (switchPref) {
			getActivity().setTheme(R.style.AppTheme_DARK);
			getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDarker));
		} else {
			getActivity().setTheme(R.style.AppTheme);
			getActivity().getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
		}
	}

}
