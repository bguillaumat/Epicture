package brice_bastien.epicture;

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
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import brice_bastien.epicture.dummy.PostItem;
import uk.me.hardill.volley.multipart.MultipartRequest;

class ApiCall {

	private String username;
	private String client_id;
	private String token;

	ApiCall(String username, String client_id, String token) {
		this.client_id = client_id;
		this.username = username;
		this.token = token;
	}

	void uploadImg() {
		//https://api.imgur.com/3/image
	}

	void getUserImg(final Context context, final PostsFragment fragment) {
		String url = "https://api.imgur.com/3/account/" + username + "/images";

		RequestQueue queue = Volley.newRequestQueue(context);

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
				null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					fragment.adapter.removeAll();
					Log.i("GetData", response.toString(2));
					JSONArray array = new JSONArray(response.getString("data"));
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = new JSONObject(array.getString(i));
						PostItem post = new PostItem(obj.getString("id"), obj.getString("title"), "0", "0", obj.getString("link"));
						if (obj.isNull("images")) {
							post.AddImage(obj.getString("link"));
							fragment.adapter.addItem(0, post);
							continue;
						}
						JSONArray images = new JSONArray(obj.getString("images"));
						for (int j = 0; j < images.length(); j++) {
							JSONObject tmp_img = new JSONObject(images.getString(j));
							post.AddImage(tmp_img.getString("link"));
						}
						fragment.adapter.addItem(0, post);
					}
				} catch (Exception e) {
					Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<>();
				headers.put("Content-Type", "application/json; charset=UTF-8");
				String credentials = "Bearer " + token;
				headers.put("Authorization", credentials);
				return headers;
			}
		};

		queue.add(req);

	}

	void getRecentImg(final Context context, String section, final PostsFragment fragment) {
		String url = "https://api.imgur.com/3/gallery/" + section + "/";

		RequestQueue queue = Volley.newRequestQueue(context);

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
				null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					fragment.adapter.removeAll();
					JSONArray array = new JSONArray(response.getString("data"));
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = new JSONObject(array.getString(i));
						PostItem post = new PostItem(obj.getString("id"), obj.getString("title"), obj.getString("ups"), obj.getString("downs"), obj.getString("link"));
						if (obj.isNull("images")) {
							post.AddImage(obj.getString("link"));
							fragment.adapter.addItem(0, post);
							continue;
						}
						JSONArray images = new JSONArray(obj.getString("images"));
						for (int j = 0; j < images.length(); j++) {
							JSONObject tmp_img = new JSONObject(images.getString(j));
							post.AddImage(tmp_img.getString("link"));
						}
						fragment.adapter.addItem(0, post);
					}
				} catch (Exception e) {
					Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<>();
				headers.put("Content-Type", "application/json; charset=UTF-8");
				String credentials = "Client-ID " + client_id;
				headers.put("Authorization", credentials);
				return headers;
			}
		};
		queue.add(req);
	}

	void searchImg() {

	}

	void getFavorites(final Context context, final PostsFragment fragment) {
		String url = "https://api.imgur.com/3/account/" + username + "/favorites/";

		RequestQueue queue = Volley.newRequestQueue(context);

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
				null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONArray array = new JSONArray(response.getString("data"));
					fragment.adapter.removeAll();
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = new JSONObject(array.getString(i));
						PostItem post = new PostItem(obj.getString("id"), obj.getString("title"), "0", "0", obj.getString("link"));
						if (obj.isNull("images")) {
							post.AddImage(obj.getString("link"));
							fragment.adapter.addItem(0, post);
							continue;
						}
						JSONArray images = new JSONArray(obj.getString("images"));
						for (int j = 0; j < images.length(); j++) {
							JSONObject tmp_img = new JSONObject(images.getString(j));
							post.AddImage(tmp_img.getString("link"));
						}
						fragment.adapter.addItem(0, post);
					}
				} catch (Exception e) {
					Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<>();
				headers.put("Content-Type", "application/json; charset=UTF-8");
				String credentials = "Bearer " + token;
				headers.put("Authorization", credentials);
				return headers;
			}
		};
		queue.add(req);
	}

	void getUserImgProfil(final Context context) {
		String url = "https://api.imgur.com/3/account/" + username + "/avatar";

		RequestQueue queue = Volley.newRequestQueue(context);

		JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
				null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {

				try {
					Log.i("UsrImg", response.toString(2));
					JSONArray array = new JSONArray(response.getString("data"));
					//avatar link in avatar field
					// avatar name in avatar_name field
				} catch (Exception e) {
					Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
				}

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() {
				HashMap<String, String> headers = new HashMap<>();
				headers.put("Content-Type", "application/json; charset=UTF-8");
				String credentials = "Bearer " + token;
				headers.put("Authorization", credentials);
				return headers;
			}
		};
		queue.add(req);
	}

	void UploadImg(final Context context, Uri img) {
		String url = "https://api.imgur.com/3/image";

		RequestQueue queue = Volley.newRequestQueue(context);

		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);
		MultipartRequest request = new MultipartRequest(url, headers,
				new Response.Listener<NetworkResponse>() {
					@Override
					public void onResponse(NetworkResponse response) {
						Log.w("Upload", response.toString());
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

			queue.add(request);

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

