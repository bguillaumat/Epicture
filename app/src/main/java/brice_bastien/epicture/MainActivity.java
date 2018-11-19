package brice_bastien.epicture;

import android.Manifest;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.post.PostItem;

public class MainActivity extends AppCompatActivity implements PostsFragment.OnListFragmentInteractionListener {

	private boolean upload_dialog = false;
	private Uri imagePath;
	private String Token = "";
	private String Username = "";
	private SharedPreferences sharedPreferences;
	private PostsFragment postsFragment;
	private FloatingActionButton fab;
	private BottomAppBar bar;
	private FragmentManager fragmentManager = getFragmentManager();
	private ImgurApi imgurApi;
	private static final int REQUEST_CODE_CAMERA = 24;
	private static final int REQUEST_CODE_FILE_EXPLORER = 42;
	String[] PERMISSIONS = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.CAMERA
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
		postsFragment = PostsFragment.newInstance(2, imgurApi);
		fragmentManager.beginTransaction().replace(R.id.include, postsFragment).commit();
		fab = findViewById(R.id.fab);
		final AppCompatActivity main = this;
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Dialog dialog = new Dialog(main);
				dialog.setTitle(R.string.modal_upload_choose);
				dialog.setCancelable(true);
				dialog.setContentView(R.layout.dialog_image);
				dialog.show();

				Button camera = dialog.findViewById(R.id.camera_action);
				Button fileExplorer = dialog.findViewById(R.id.file_explorer_action);

				camera.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (ContextCompat.checkSelfPermission(main, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
							Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							if (takePicture.resolveActivity(getPackageManager()) != null) {
								startActivityForResult(takePicture, REQUEST_CODE_CAMERA);
							}
						} else {
							ActivityCompat.requestPermissions(main, PERMISSIONS, 1);
						}
						dialog.dismiss();
					}
				});

				fileExplorer.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
						intent.addCategory(Intent.CATEGORY_OPENABLE);
						intent.setType("image/*");
						startActivityForResult(intent, REQUEST_CODE_FILE_EXPLORER);
						dialog.dismiss();
					}
				});

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
		Intent intent = new Intent(this, PostDetails.class);
		intent.putExtra("ID", item.id);
		intent.putExtra("IS_ALBUM", item.favType == PostItem.FAV_TYPE.ALBUM);
		intent.putExtra("USERNAME", item.ownerName);
		intent.putExtra("COMMENTS", item.commentNumber);
		startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE_FILE_EXPLORER:
					if (data != null) {
						imagePath = data.getData();
						upload_dialog = true;
					}
					break;
				case REQUEST_CODE_CAMERA:
					if (data != null) {
						Bitmap img = (Bitmap)data.getExtras().get("data");
						if (img != null) {
							imagePath = getImageUri(getApplicationContext(), img);
							upload_dialog = true;
						}
					}
					break;
			}
		}
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		if (upload_dialog) {
			upload_dialog = false;
			showUploadDialog(imagePath);
			imagePath = null;
		}
	}

	private Uri getImageUri(Context context, Bitmap inImage) {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
		return Uri.parse(path);
	}

	private void showUploadDialog(Uri img) {
		DialogUpload editNameDialogFragment = DialogUpload.newInstance(postsFragment.adapter, img, imgurApi);
		editNameDialogFragment.show(getSupportFragmentManager(), "fragment_upload_picture");
	}

	private void showEditDialog() {
		DialogSort editNameDialogFragment = DialogSort.newInstance(postsFragment.adapter);
		editNameDialogFragment.show(getSupportFragmentManager(), "fragment_edit_name");
	}


}
