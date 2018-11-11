package brice_bastien.epicture.ImgurApi;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import brice_bastien.epicture.BuildConfig;
import brice_bastien.epicture.PostsFragment;
import brice_bastien.epicture.R;
import uk.me.hardill.volley.multipart.MultipartRequest;

public class ImgurApi {

	// const var
	private static final String host = "https://api.imgur.com/";

	// type definition
	private enum REQUEST_TYPE {
		NONE,
		GET_USR_IMG,
		GET_RECENT_IMG,
		GET_USR_FAVORITE
	}

	// class var
	private Context context;
	private String clientId = BuildConfig.IMGUR_API_KEY;
	private String username;
	private String token;
	private REQUEST_TYPE lastRequestType;
	private RequestQueue requestQueue;

	public ImgurApi(Context context, String username, String token) {
		this.username = username;
		this.token = token;
		this.context = context;
		requestQueue = Volley.newRequestQueue(context);
		lastRequestType = REQUEST_TYPE.NONE;
	}

	// Refresh last request
	public void refresh_data(PostsFragment fragment) {
		switch (lastRequestType) {
			case NONE:
				break;
			case GET_USR_IMG:
				getUserImg(fragment);
				break;
			case GET_RECENT_IMG:
				getRecentImg(fragment, "hot");
				break;
			case GET_USR_FAVORITE:
				getUserFavorite(fragment);
				break;
		}
	}

	// Get Favorite Image of User
	public void getUserFavorite(PostsFragment fragment) {
		String url = host + "/3/account/" + username + "/favorites/";
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponseJsonPosts(context, fragment), new ErrorListener(), clientId, token);

		requestQueue.add(request);
		lastRequestType = REQUEST_TYPE.GET_USR_FAVORITE;
	}

	// Get User Img import
	public void getUserImg(PostsFragment fragment) {
		String url = host + "/3/account/" + username + "/images";
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponseJsonPosts(context, fragment), new ErrorListener(), clientId, token);

		requestQueue.add(request);
		lastRequestType = REQUEST_TYPE.GET_USR_IMG;
	}

	// Get Recent hot/viral image in imgur
	public void getRecentImg(PostsFragment fragment, String section) {
		String url = host + "/3/gallery/" + section + "/";
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponseJsonPosts(context, fragment), new ErrorListener(), clientId, token);

		requestQueue.add(request);
		lastRequestType = REQUEST_TYPE.GET_RECENT_IMG;
	}

	public void uploadImg(Uri img) {
		String url = host + "3/image";

		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);
		MultipartRequest request = new MultipartRequest(url, headers,
				new Response.Listener<NetworkResponse>() {
					@Override
					public void onResponse(NetworkResponse response) {
						Log.w("Upload", response.toString());
						Toast.makeText(context, context.getText(R.string.upload_success), Toast.LENGTH_SHORT).show();
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Log.w("Upload", error.toString());
					}
				});
		try {
			String mimeType = context.getContentResolver().getType(img);
			InputStream iStream = context.getContentResolver().openInputStream(img);
			byte[] data = getBytes(iStream);
			request.addPart(new MultipartRequest.FilePart("image", mimeType, null, data));
			requestQueue.add(request);
		} catch (Exception e) {
			Log.w("Upload:", "failed");
		}
	}

	private byte[] getBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int len = 0;

		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		return byteBuffer.toByteArray();
	}


}
