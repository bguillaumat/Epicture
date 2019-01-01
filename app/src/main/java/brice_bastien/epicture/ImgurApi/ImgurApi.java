package brice_bastien.epicture.ImgurApi;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import androidx.preference.PreferenceManager;
import brice_bastien.epicture.AccountSetting;
import brice_bastien.epicture.BuildConfig;
import brice_bastien.epicture.PostDetails;
import brice_bastien.epicture.PostsFragment;
import brice_bastien.epicture.Settings.SettingItem;
import brice_bastien.epicture.SettingsActivity;
import brice_bastien.epicture.post.CommentAdapter;
import brice_bastien.epicture.post.PostItem;
import uk.me.hardill.volley.multipart.MultipartRequest;

import static brice_bastien.epicture.SettingsActivity.KEY_PREF_FAVORITE_SORT;
import static brice_bastien.epicture.SettingsActivity.KEY_PREF_FEED_SECTION;
import static brice_bastien.epicture.SettingsActivity.KEY_PREF_FEED_SORT;
import static brice_bastien.epicture.SettingsActivity.KEY_PREF_SEARCH_SORT;

public class ImgurApi {

	// const var
	private static final String host = "https://api.imgur.com/3/";

	// type definition
	private enum REQUEST_TYPE {
		NONE,
		GET_USR_IMG,
		GET_RECENT_IMG,
		GET_USR_FAVORITE,
		QUERY,
		COMMENTARY
	}

	// class var
	private Context context;
	private String clientId = BuildConfig.IMGUR_API_KEY;
	private String username;
	private String token;
	private String query;
	private REQUEST_TYPE lastRequestType;
	private RequestQueue requestQueue;

	public ImgurApi(Context context, String username, String token) {
		this.username = username;
		this.token = token;
		this.context = context;
		requestQueue = Volley.newRequestQueue(context);
		lastRequestType = REQUEST_TYPE.NONE;
	}

	public String getUsername() {
		return this.username;
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
			case QUERY:
				getQuery(query, fragment);
				break;
		}
	}

	public void delUserImg(boolean isAlbum, String deleteHash) {
		String url = host + (isAlbum ? "album/" : "image/") + deleteHash;
		JsonObjectRequest request = new JsonRequest(Request.Method.DELETE, url, null, new ResponsePosts(context), new ErrorListener(), clientId, token);

		requestQueue.add(request);
	}

	public void getDetails(boolean isAlbum, String id, PostDetails postDetails) {
		String url = host + (isAlbum ? "album/" : "image/") + id;
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponsePostDetail(postDetails), new ErrorListener(), clientId, token);

		requestQueue.add(request);
	}

	public void postComment(String comment, String id) {
		String url = host + "gallery/" + id + "/comment";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);

		MultipartRequest request = new MultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
			@Override
			public void onResponse(NetworkResponse response) {
				Log.w("post a comment", Integer.toString(response.statusCode));
			}
		}, new ErrorListener());

		try {
			request.addPart(new MultipartRequest.FormPart("comment", comment));
			Log.i("post a comment", new String(request.getBody()));
			requestQueue.add(request);
		}catch (Exception e) {
			Log.w("post a comment:", "failed");
		}
	}

	public void getComment(String id, CommentAdapter adapter) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		Boolean switchPref = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_COMMENTARY_NEW, false);

		String url = host + "gallery/" + id + "/comments/" + (switchPref ? "new" : "best" );
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponseCommentListener(adapter), new ErrorListener(adapter.statesRecyclerViewAdapter), clientId, token);

		requestQueue.add(request);
		lastRequestType = REQUEST_TYPE.COMMENTARY;
	}

	public void getQuery(String query, PostsFragment postsFragment) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String sort = sharedPrefs.getString(KEY_PREF_SEARCH_SORT, "viral");
		String url = host + "gallery/search/" + sort + "/?q=" + query;
		JsonObjectRequest requets = new JsonRequest(Request.Method.GET, url, null, new ResponseJsonPosts(context, postsFragment), new ErrorListener(), clientId, token);

		requestQueue.add(requets);
		this.query = query;
		lastRequestType = REQUEST_TYPE.QUERY;
	}

	public void addVote(String id, String vote) {
		String url = host + "gallery/" + id + "/vote/" + vote;
		JsonObjectRequest request = new JsonRequest(Request.Method.POST, url, null, new ResponsePosts(context), new ErrorListener(), clientId, token);

		requestQueue.add(request);
	}

	// Fav an image
	public void addImgFav(String pictureId, PostItem.FAV_TYPE type) {
		String url = host + (PostItem.FAV_TYPE.ALBUM == type ? "album" : "image") + "/" + pictureId + "/favorite";
		JsonObjectRequest request = new JsonRequest(Request.Method.POST, url, null, new ResponsePosts(context), new ErrorListener(), clientId, token);

		requestQueue.add(request);
	}

	// Get Favorite Image of User
	public void getUserFavorite(PostsFragment fragment) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		String sort = sharedPrefs.getString(KEY_PREF_FAVORITE_SORT, "newest");

		String url = host + "account/" + username + "/favorites/" + sort;
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponseJsonPosts(context, fragment), new ErrorListener(fragment), clientId, token);

		requestQueue.add(request);
		lastRequestType = REQUEST_TYPE.GET_USR_FAVORITE;
	}

	// Get User Img import
	public void getUserImg(PostsFragment fragment) {
		String url = host + "account/" + username + "/images";
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponseJsonPosts(context, fragment), new ErrorListener(fragment), clientId, token);

		requestQueue.add(request);
		lastRequestType = REQUEST_TYPE.GET_USR_IMG;
	}

	// Get Recent hot/viral image in imgur
	public void getRecentImg(PostsFragment fragment, String section) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		section = sharedPrefs.getString(KEY_PREF_FEED_SECTION, "hot");
		String sort = sharedPrefs.getString(KEY_PREF_FEED_SORT, "viral");

		String url = host + "gallery/" + section + "/" + sort;
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponseJsonPosts(context, fragment), new ErrorListener(fragment), clientId, token);

		requestQueue.add(request);
		lastRequestType = REQUEST_TYPE.GET_RECENT_IMG;
	}

	public void getUsrAvatar(ImageView img, String username) {
		String url = host + "account/" + username + "/avatar";
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponseAvatarListener(context, img) , new ErrorListener(), clientId, token);

		requestQueue.add(request);
	}

	public void getUsrSetting(AccountSetting view) {
		String url = host + "account/me/settings";
		JsonObjectRequest request = new JsonRequest(Request.Method.GET, url, null, new ResponseSettingsListener(context, view), new ErrorListener(), clientId, token);

		requestQueue.add(request);
	}

	public void putUsrSetting(SettingItem settingItem) {
		String url = host + "account/" + username +"/settings";
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);
		MultipartRequest request = new MultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
			@Override
			public void onResponse(NetworkResponse response) {
				Log.w("saveSetting", Integer.toString(response.statusCode));
			}
		}, new ErrorListener());

		try {
			request.addPart(new MultipartRequest.FormPart("show_mature", Boolean.toString(settingItem.isMature())));
			request.addPart(new MultipartRequest.FormPart("album_privacy", settingItem.getAlbumType()));
			request.addPart(new MultipartRequest.FormPart("public_images", Boolean.toString(settingItem.isPublishType())));
			request.addPart(new MultipartRequest.FormPart("newsletter_subscribed", Boolean.toString(settingItem.isNewsletter())));
			Log.i("saveSetting", new String(request.getBody()));
			requestQueue.add(request);
		}catch (Exception e) {
			Log.w("saveSetting:", e.toString());
		}
	}

	public void editImage(boolean isAlbum, String id, String title, String description) {
		String url = host +  (isAlbum ? "album/" : "image/") + id;
		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);

		MultipartRequest request = new MultipartRequest(Request.Method.POST, url, headers, new Response.Listener<NetworkResponse>() {
			@Override
			public void onResponse(NetworkResponse response) {
				Log.i("edit", Integer.toString(response.statusCode));
			}
		}, new ErrorListener());

		try {
			request.addPart(new MultipartRequest.FormPart("title", title));
			request.addPart(new MultipartRequest.FormPart("description", description));
			requestQueue.add(request);

		} catch (Exception e) {
			Log.w("saveSetting:", e.toString());
		}


	}

	public void uploadImg(Uri img, String title, String desc) {
		UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

		try {
			String uploadId =
					new MultipartUploadRequest(context, host + "image")
							.addFileToUpload(img.getPath(), "image")
							.addHeader("Authorization", "Client-ID " + clientId)
							.addHeader("Authorization", "Bearer " + token)
							.addParameter("type", context.getContentResolver().getType(img))
							.addParameter("title", title)
							.addParameter("description", desc)
							.setNotificationConfig(new UploadNotificationConfig())
							.setMaxRetries(2)
							.startUpload();
		} catch (Exception e) {
			e.printStackTrace();
		}
/*

		String url = host + "image";

		HashMap<String, String> headers = new HashMap<>();
		headers.put("Authorization", "Bearer " + token);
		MultipartRequest request = new MultipartRequest(url, headers,
				new Response.Listener<NetworkResponse>() {
					@Override
					public void onResponse(NetworkResponse response) {
						Log.w("UploadResponse", response.toString());
						Toast.makeText(context, context.getText(R.string.upload_success), Toast.LENGTH_SHORT).show();
					}
				}, new ErrorListener());
		try {
			String mimeType = context.getContentResolver().getType(img);
			InputStream iStream = context.getContentResolver().openInputStream(img);
			byte[] data = getBytes(iStream);
			request.addPart(new MultipartRequest.FilePart("image", mimeType, null, data));
			request.addPart(new MultipartRequest.FormPart("title", title));
			request.addPart(new MultipartRequest.FormPart("description", desc));
			request.setRetryPolicy(new DefaultRetryPolicy(2500, 0, 1.0f));
			requestQueue.add(request);
		} catch (Exception e) {
			e.printStackTrace();
			Log.w("Upload", e.toString());
		}*/
	}

	private byte[] getBytes(InputStream inputStream) throws IOException {
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		int len;

		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}
		return byteBuffer.toByteArray();
	}


}
