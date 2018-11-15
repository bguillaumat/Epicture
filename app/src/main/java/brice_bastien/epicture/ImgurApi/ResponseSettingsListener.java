package brice_bastien.epicture.ImgurApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONObject;

public class ResponseSettingsListener implements Response.Listener<JSONObject> {

	private Context context;

	ResponseSettingsListener(Context context) {
		this.context = context;
	}

	@Override
	public void onResponse(JSONObject response) {
		try {

			JSONObject tmp = response.getJSONObject("data");
			Log.i("ResponseSetting", tmp.toString(2));
			/*
			* "account_url": "AsianPw",
      "email": "bricelangnguyen1@gmail.com",
      "avatar": null,
      "cover": null,
      "public_images": false,
      "album_privacy": "public",
      "pro_expiration": false,
      "accepted_gallery_terms": false,
      "active_emails": [],
      "messaging_enabled": true,
      "comment_replies": true,
      "blocked_users": [],
      "show_mature": false,
      "newsletter_subscribed": false,
      "first_party": true
			* */

		} catch (Exception e) {
			Log.i("ResponseJsonPosts", e.toString());
			Toast.makeText(context, "Check our internet connection", Toast.LENGTH_SHORT).show();
		}

	}
}
