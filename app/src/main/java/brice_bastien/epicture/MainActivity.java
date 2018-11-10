package brice_bastien.epicture;

import android.Manifest;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import brice_bastien.epicture.dummy.PostItem;

public class MainActivity extends AppCompatActivity implements PostsFragment.OnListFragmentInteractionListener {

	private String Token = "";
	private String Username = "";
	private SharedPreferences sharedPreferences;
	private PostsFragment postsFragment = PostsFragment.newInstance(1);
	private FragmentManager fragmentManager = getFragmentManager();
	private ApiCall apiCall;
	String[] PERMISSIONS = {
			android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
			android.Manifest.permission.READ_EXTERNAL_STORAGE
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		BottomAppBar bar = findViewById(R.id.bottom_app_bar);
		setSupportActionBar(bar);

		ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

		sharedPreferences = getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
		Token = sharedPreferences.getString("User_Token", null);
		Username = sharedPreferences.getString("Username", null);

		if (Token == null || Username == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			finish();
			startActivity(intent);
		}

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
		if (switchPref) {
			setTheme(R.style.AppTheme_DARK);
		} else {
			setTheme(R.style.AppTheme);
		}

		fragmentManager.beginTransaction().replace(R.id.include, postsFragment).commit();
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, 42);
			}
		});

		apiCall = new ApiCall(Username, "8c94575ba123f37", Token);
		apiCall.getRecentImg(getApplicationContext(), "hot", postsFragment);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
		if (switchPref) {
			setTheme(R.style.AppTheme_DARK);
		} else {
			setTheme(R.style.AppTheme);
		}
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
				apiCall.getFavorites(getApplicationContext(), postsFragment);
				break;
			case (R.id.app_bar_search):
				break;
			case (android.R.id.home):
				BottomNavigationDrawerFragment bottomNavDrawerFragment = new BottomNavigationDrawerFragment();
				bottomNavDrawerFragment.postsFragment = postsFragment;
				bottomNavDrawerFragment.show(getSupportFragmentManager(), bottomNavDrawerFragment.getTag());
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListFragmentInteraction(PostItem item) {

	}
}
