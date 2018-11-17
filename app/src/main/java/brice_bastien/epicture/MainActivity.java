package brice_bastien.epicture;

import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.post.PostItem;

public class MainActivity extends AppCompatActivity implements PostsFragment.OnListFragmentInteractionListener {

	private String Token = "";
	private String Username = "";
	private SharedPreferences sharedPreferences;
	private PostsFragment postsFragment;
	private FloatingActionButton fab;
	private BottomAppBar bar;
	private FragmentManager fragmentManager = getFragmentManager();
	private ImgurApi imgurApi;
	private static final int REQUEST_CODE = 42;
	String[] PERMISSIONS = {
			android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
			android.Manifest.permission.READ_EXTERNAL_STORAGE
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bar = findViewById(R.id.bottom_app_bar);
		setSupportActionBar(bar);

		ActivityCompat.requestPermissions(this, PERMISSIONS, 1);

		sharedPreferences = getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
		Token = sharedPreferences.getString("User_Token", null);
		Username = sharedPreferences.getString("Username", null);

		if (Token == null || Username == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
		if (switchPref) {
			setTheme(R.style.AppTheme_DARK);
		} else {
			setTheme(R.style.AppTheme);
		}

		imgurApi = new ImgurApi(getApplicationContext(), Username, Token);
		postsFragment = PostsFragment.newInstance(1, imgurApi);
		fragmentManager.beginTransaction().replace(R.id.include, postsFragment).commit();
		fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/*");
				startActivityForResult(intent, REQUEST_CODE);
			}
		});

		imgurApi.getRecentImg(postsFragment, "hot");
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

		SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

		SearchView search = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();

		search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
		search.setOnSearchClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bar.setCradleVerticalOffset(100);
			}
		});
		search.setOnCloseListener(new SearchView.OnCloseListener() {
			@Override
			public boolean onClose() {
				bar.setCradleVerticalOffset(0);
				return false;
			}
		});

		search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				Log.i("QuerySub", query);
				if (query.isEmpty()) {
					return true;
				}
				imgurApi.getQuery(query, postsFragment);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String query) {
				Log.i("QueryChange", query);
				if (query.isEmpty()) {
					return true;
				}
				imgurApi.getQuery(query, postsFragment);
				return true;
			}

		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case (R.id.action_settings):
				Intent intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				return true;
			case (R.id.app_bar_home):
				imgurApi.getRecentImg(postsFragment, "hot");
				break;
			case (R.id.app_bar_search):
				break;

			case (R.id.action_sort):
				showEditDialog();
				break;
			case (android.R.id.home):
				BottomNavigationDrawerFragment bottomNavDrawerFragment = new BottomNavigationDrawerFragment();
				bottomNavDrawerFragment.postsFragment = postsFragment;
				bottomNavDrawerFragment.imgurApi = imgurApi;
				bottomNavDrawerFragment.show(getSupportFragmentManager(), bottomNavDrawerFragment.getTag());
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListFragmentInteraction(PostItem item) {
		// TODO display one item
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE:
					if (data != null) {
						imgurApi.uploadImg(data.getData());
					}
			}
		}

	}

	private void showEditDialog() {
		DialogSort editNameDialogFragment = DialogSort.newInstance(postsFragment.adapter);
		editNameDialogFragment.show(getSupportFragmentManager(), "fragment_edit_name");
	}


}
