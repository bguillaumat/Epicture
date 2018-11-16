package brice_bastien.epicture.ImgurApi;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONObject;

public class ResponseCommentListener implements Response.Listener<JSONObject> {

	@Override
	public void onResponse(JSONObject response) {
		try {
			Log.i("Comment", response.toString(2));
		} catch (Exception e) {
			Log.i("Comment", e.toString());
		}
	}
}
