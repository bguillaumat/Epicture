package brice_bastien.epicture.ImgurApi;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import brice_bastien.epicture.PostsFragment;
import brice_bastien.epicture.post.PostItem;

public class ResponseJsonPosts implements Response.Listener<JSONObject> {

	private PostsFragment postsFragment;
	private Context context;

	ResponseJsonPosts(Context context, PostsFragment postsFragment) {
		this.postsFragment = postsFragment;
		this.context = context;
	}

	@Override
	public void onResponse(JSONObject response) {
		try {
			postsFragment.adapter.removeAll();
			JSONArray array = new JSONArray(response.getString("data"));
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = new JSONObject(array.getString(i));
				Log.i("GetData", obj.toString(2));
				PostItem post = new PostItem(obj.getString("id"), obj.getString("title"), "0", "0", obj.getString("link"), obj.getBoolean("favorite"));
				if (obj.has("cover")) {
					post.imageFav = obj.getString("cover");
				}
				if (obj.isNull("images")) {
					post.AddImage(obj.getString("link"));
					postsFragment.adapter.addItem(0, post);
					continue;
				}
				JSONArray images = new JSONArray(obj.getString("images"));
				for (int j = 0; j < images.length(); j++) {
					JSONObject tmp_img = new JSONObject(images.getString(j));
					if (tmp_img.has("gifv")) {
						post.AddImage(tmp_img.getString("gifv"));
					} else
						post.AddImage(tmp_img.getString("link"));
				}
				postsFragment.adapter.addItem(0, post);
			}
		} catch (Exception e) {
			Log.i("ResponseJsonPosts", e.toString());
			Toast.makeText(context, "Check our internet connection", Toast.LENGTH_SHORT).show();
		}
	}
}
