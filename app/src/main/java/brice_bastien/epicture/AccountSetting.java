package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import brice_bastien.epicture.ImgurApi.ImgurApi;

public class AccountSetting extends AppCompatActivity {

	private String Token = "";
	private String Username = "";
	private SharedPreferences sharedPreferences;
	ImgurApi imgurApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_setting);

		sharedPreferences = getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
		Token = sharedPreferences.getString("User_Token", null);
		Username = sharedPreferences.getString("Username", null);

		if (Token == null || Username == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			finish();
			startActivity(intent);
		}

		TextView username = findViewById(R.id.username_settings);
		username.setText(Username);

		imgurApi = new ImgurApi(getApplicationContext(), Username, Token);

		imgurApi.getUsrAvatar((ImageView)findViewById(R.id.avatar_settings));
		imgurApi.getUsrSetting();
	}

}
