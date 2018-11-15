package brice_bastien.epicture.ImgurApi;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONObject;

public class EmptyResponse  implements Response.Listener<JSONObject> {
	@Override
	public void onResponse(JSONObject response) {
		Log.w("Upload", response.toString());
	}
}
