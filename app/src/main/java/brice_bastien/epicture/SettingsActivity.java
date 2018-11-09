package brice_bastien.epicture;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_EXAMPLE_SWITCH = "example_switch";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
        if (switchPref) {
            setTheme(R.style.AppTheme_DARK);
        } else {
            setTheme(R.style.AppTheme);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
