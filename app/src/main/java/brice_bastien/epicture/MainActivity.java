package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

	Storage store = new Storage();

	private String Token = "";
	private String Username = "";
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		BottomAppBar bar = findViewById(R.id.bottom_app_bar);
		setSupportActionBar(bar);

		sharedPreferences = getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
		Token = sharedPreferences.getString("User_Token", null);
		Username = sharedPreferences.getString("Username", null);

		if (Token == null || Username == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			finish();
			startActivity(intent);
		}

		Toast.makeText(this, Token, Toast.LENGTH_LONG).show();

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				BottomAppBar bar = findViewById(R.id.bottom_app_bar);
				ApiCall apiCall = new ApiCall(Username, "8c94575ba123f37", Token);
				apiCall.getUserImg(getApplicationContext());
				if (bar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_END) {
					bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
				} else {
					bar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
				}
			}
		});
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
	}

	@Override
	protected void onPause() {
		//store.closeData(this, Token, Username, Etc);
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case (R.id.action_settings):
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			case (R.id.app_bar_fav):
				break;
			case (R.id.app_bar_search):
				break;
			case (android.R.id.home):
				BottomNavigationDrawerFragment bottomNavDrawerFragment = new BottomNavigationDrawerFragment();
				bottomNavDrawerFragment.show(getSupportFragmentManager(), bottomNavDrawerFragment.getTag());
				break;
		}
		return super.onOptionsItemSelected(item);
	}

}
