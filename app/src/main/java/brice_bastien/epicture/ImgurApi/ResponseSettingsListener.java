package brice_bastien.epicture.ImgurApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONObject;

import brice_bastien.epicture.AccountSetting;
import brice_bastien.epicture.Settings.SettingItem;

public class ResponseSettingsListener implements Response.Listener<JSONObject> {

	private Context context;
	private AccountSetting view;

	ResponseSettingsListener(Context context, AccountSetting view) {
		this.context = context;
		this.view = view;
	}

	@Override
	public void onResponse(JSONObject response) {
		try {

			JSONObject tmp = response.getJSONObject("data");
			Log.i("ResponseSetting", tmp.toString(2));
			SettingItem setting = new SettingItem(tmp.getBoolean("show_mature"), tmp.getBoolean("public_images"), tmp.getBoolean("messaging_enabled"), tmp.getBoolean("newsletter_subscribed"), tmp.getString("email"), tmp.getString("album_privacy"));
			view.initSetting(setting);
		} catch (Exception e) {
			Log.i("ResponseJsonPosts", e.toString());
			Toast.makeText(context, "Check our internet connection", Toast.LENGTH_SHORT).show();
		}

	}
}
