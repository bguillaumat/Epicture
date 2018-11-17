package brice_bastien.epicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {

	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

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
