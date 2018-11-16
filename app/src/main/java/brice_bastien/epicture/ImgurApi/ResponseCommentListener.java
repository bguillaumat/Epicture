package brice_bastien.epicture.ImgurApi;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import brice_bastien.epicture.post.CommentAdapter;
import brice_bastien.epicture.post.CommentItem;

public class ResponseCommentListener implements Response.Listener<JSONObject> {

	private CommentAdapter adapter;

	ResponseCommentListener(CommentAdapter adapter) {
		this.adapter = adapter;
	}

	@Override
	public void onResponse(JSONObject response) {
		try {
			Log.i("Comment", response.toString(2));
			JSONArray array = new JSONArray(response.getString("data"));
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = new JSONObject(array.getString(i));
				CommentItem commentItem = new CommentItem(obj.getString("author"), obj.getString("comment"));
				adapter.addItem(0, commentItem);
			}
		} catch (Exception e) {
			Log.i("Comment", e.toString());
		}
	}
}
