package brice_bastien.epicture;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
		if (switchPref) {
			setTheme(R.style.AppTheme_DARK_NoActionBar);
			getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccentDarker));
		} else {
			setTheme(R.style.AppTheme_NoActionBar);
			getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
		}

		setContentView(R.layout.activity_splash_screen);

		ConstraintLayout layout = findViewById(R.id.splash_background);
		if (switchPref) {
			layout.setBackground(new ColorDrawable(Color.BLACK));
		}

		sharedPreferences = getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);

		int SPLASH_TIME_OUT = 2000;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent;

				if (sharedPreferences.contains("User_Token") && sharedPreferences.contains("Username")) {
					intent = new Intent(getApplicationContext(), MainActivity.class);
				} else {
					intent = new Intent(getApplicationContext(), LoginActivity.class);
				}
				startActivity(intent);
				finish();
			}
		}, SPLASH_TIME_OUT);

	}
}
