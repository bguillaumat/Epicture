package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.post.CommentAdapter;
import brice_bastien.epicture.post.CommentItem;

public class PostComment extends AppCompatActivity {

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

		RecyclerView recyclerView = findViewById(R.id.commentList);

		Intent intent = getIntent();
		id = intent.getStringExtra("POST_ID");


		imgurApi = new ImgurApi(getApplicationContext(), Username, Token);

		final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.refresh_commentary);

		Context context = recyclerView.getContext();
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		final CommentAdapter adapter = new CommentAdapter(getApplicationContext(), imgurApi);
		recyclerView.setAdapter(adapter);

		imgurApi.getComment(id, adapter);

		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				imgurApi.getComment(id, adapter);
				swipeRefreshLayout.setRefreshing(false);
			}
		});


		ImageView userPic = findViewById(R.id.user_add_comment);
		imgurApi.getUsrAvatar(userPic, Username);


		final EditText commentText = findViewById(R.id.editMessage);
		ImageButton sendButton = findViewById(R.id.buttonSend);
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				imgurApi.postComment(commentText.getText().toString(), id);
				commentText.setText(null);
			}
		});

	}

}
