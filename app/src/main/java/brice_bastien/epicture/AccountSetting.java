package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.Settings.SettingItem;

public class AccountSetting extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	private String Token = "";
	private String Username = "";
	private SharedPreferences sharedPreferences;
	Switch mature;
	Switch publishMode;
	Switch messaging;
	Switch newsletter;
	TextView username;
	TextView commentCount;
	Button saveBtn;
	TextView email;
	RadioButton publicType;
	RadioButton hiddenType;
	RadioButton secretType;
	SwipeRefreshLayout refreshLayout;
	ImgurApi imgurApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false);
		if (switchPref) {
			setTheme(R.style.AppTheme_DARK);
			getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccentDarker));
		} else {
			setTheme(R.style.AppTheme);
			getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
		}
		sharedPrefs.registerOnSharedPreferenceChangeListener(this);

		setContentView(R.layout.activity_account_setting);

		mature = findViewById(R.id.switch_mature_content);
		publishMode = findViewById(R.id.publish_mode);
		messaging = findViewById(R.id.messaging);
		newsletter = findViewById(R.id.newsletter_sub);
		username = findViewById(R.id.username_settings);
		saveBtn = findViewById(R.id.save_btn);
		email = findViewById(R.id.userEmail);
		publicType = findViewById(R.id.public_type);
		hiddenType = findViewById(R.id.hidden_type);
		secretType = findViewById(R.id.secret_type);
		refreshLayout = findViewById(R.id.refreshSwipeSetting);
		commentCount = findViewById(R.id.commentsCount);

		sharedPreferences = getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
		Token = sharedPreferences.getString("User_Token", null);
		Username = sharedPreferences.getString("Username", null);

		if (Token == null || Username == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

		username.setText(Username + " \u2022 ");

		imgurApi = new ImgurApi(getApplicationContext(), Username, Token);

		imgurApi.getUsrAvatar((ImageView) findViewById(R.id.avatar_settings), Username);
		imgurApi.getCommentsCount(commentCount);
		imgurApi.getUsrSetting(this);

		final AccountSetting account = this;
		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {

				imgurApi.getUsrAvatar((ImageView) findViewById(R.id.avatar_settings), Username);
				imgurApi.getUsrSetting(account);
				refreshLayout.setRefreshing(false);
			}
		});

		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String album_privacy;
				if (publicType.isChecked())
					album_privacy = "public";
				else if (hiddenType.isChecked())
					album_privacy = "hidden";
				else
					album_privacy = "secret";
				SettingItem item = new SettingItem(mature.isChecked(), publishMode.isChecked(), messaging.isChecked(), newsletter.isChecked(), "", album_privacy);
				imgurApi.putUsrSetting(item);
			}
		});
	}

	public void initSetting(SettingItem settingItem) {
		String emailText = getString(R.string.your_email) + settingItem.getEmail();

		email.setText(emailText);
		mature.setChecked(settingItem.isMature());
		publishMode.setChecked(settingItem.isPublishType());
		messaging.setChecked(settingItem.isMessaging());
		newsletter.setChecked(settingItem.isNewsletter());
		switch (settingItem.getAlbumType()) {
			case "public":
				publicType.toggle();
				break;
			case "hidden":
				hiddenType.toggle();
				break;
			case "secret":
				secretType.toggle();
				break;
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH)) {
			if (sharedPreferences.getBoolean(SettingsActivity.KEY_PREF_EXAMPLE_SWITCH, false)) {
				setTheme(R.style.AppTheme_DARK);
				getWindow().setNavigationBarColor(getResources().getColor(R.color.colorAccentDarker));
			} else {
				setTheme(R.style.AppTheme);
				getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
			}
			recreate();
		}

	}



}
