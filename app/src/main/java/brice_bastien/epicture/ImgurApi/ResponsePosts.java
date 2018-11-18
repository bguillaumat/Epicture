package brice_bastien.epicture.ImgurApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONObject;

public class ResponsePosts implements Response.Listener<JSONObject> {

	private Context context;

	ResponsePosts(Context context) {
		this.context = context;
	}

	@Override
	public void onResponse(JSONObject response) {
		try {
			Log.i("GetData", response.toString(2));
		} catch (Exception e) {
			Log.i("ResponseJsonPosts", e.toString());
			Toast.makeText(context, "Check our internet connection", Toast.LENGTH_SHORT).show();
		}
	}
}
