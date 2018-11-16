package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.post.CommentAdapter;
import brice_bastien.epicture.post.CommentItem;

public class PostComment extends Activity {

	private String Token = "";
	private String Username = "";
	private SharedPreferences sharedPreferences;
	ImgurApi imgurApi;
	String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_comment);


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

		RecyclerView recyclerView = findViewById(R.id.commentList);

		Intent intent = getIntent();
		id = intent.getStringExtra("POST_ID");


		imgurApi = new ImgurApi(getApplicationContext(), Username, Token);

		Context context = recyclerView.getContext();
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		CommentAdapter adapter = new CommentAdapter(getApplicationContext(), imgurApi);
		recyclerView.setAdapter(adapter);

		imgurApi.getComment(id, adapter);

	}

}
