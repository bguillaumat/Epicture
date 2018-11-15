package brice_bastien.epicture.ImgurApi;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONObject;

import brice_bastien.epicture.GlideApp;

public class ResponseAvatarListener implements Response.Listener<JSONObject> {

	private Context context;
	private ImageView img;

	ResponseAvatarListener(Context context, ImageView img) {
		this.context = context;
		this.img = img;
	}

	@Override
	public void onResponse(JSONObject response) {
		try {

			JSONObject tmp = response.getJSONObject("data");
			Log.i("ResponseJsonPosts", tmp.getString("avatar"));
			GlideApp.with(context)
					.load(tmp.getString("avatar"))
					.into(img);

		} catch (Exception e) {
			Log.i("ResponseJsonPosts", e.toString());
			Toast.makeText(context, "Check our internet connection", Toast.LENGTH_SHORT).show();
		}
	}
}