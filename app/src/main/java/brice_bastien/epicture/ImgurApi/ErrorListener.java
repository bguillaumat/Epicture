package brice_bastien.epicture.ImgurApi;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class ErrorListener implements Response.ErrorListener {
	@Override
	public void onErrorResponse(VolleyError error) {
		Log.w("ErrorListener", error.toString());
	}
}
