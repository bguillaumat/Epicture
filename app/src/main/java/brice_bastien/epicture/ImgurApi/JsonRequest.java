package brice_bastien.epicture.ImgurApi;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;

public class JsonRequest extends JsonObjectRequest {

	private String clientId;
	private String usrToken;

	JsonRequest(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener, String clientId, String token) {
		super(method, url, jsonRequest, listener, errorListener);
		this.clientId = clientId;
		this.usrToken = token;
	}

	@Override
	public Map<String, String> getHeaders() {
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json; charset=UTF-8");
		String credentials = "Client-ID " + clientId;
		headers.put("Authorization", credentials);
		String token = "Bearer " + usrToken;
		headers.put("Authorization", token);
		return headers;
	}
}
