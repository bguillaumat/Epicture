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
			JSONArray array = new JSONArray(response.getString("data"));
			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = new JSONObject(array.getString(i));
				Log.i("Comment", obj.toString(2));
				if (obj.getBoolean("deleted"))
					continue;
				CommentItem commentItem = new CommentItem(obj.getString("author"), obj.getString("comment"), obj.getLong("datetime"));
				adapter.addItem(0, commentItem);
				getChildren(obj, adapter);
			}
		} catch (Exception e) {
			Log.i("Comment", e.toString());
		}
	}

	private void getChildren(JSONObject object, CommentAdapter adapter) {
		try {
			JSONArray children = new JSONArray(object.getString("children"));
			for (int j = 0; j < children.length(); j++) {
				JSONObject childrenObj = new JSONObject(children.getString(j));
				Log.i("Comment", childrenObj.toString(2));
				if (childrenObj.getBoolean("deleted"))
					continue;
				CommentItem childrenCommentItem = new CommentItem(childrenObj.getString("author"), childrenObj.getString("comment"), childrenObj.getLong("datetime"));
				adapter.addItem(0, childrenCommentItem);
				getChildren(childrenObj, adapter);
			}
		} catch (Exception e) {
			Log.i("CommentChildren", e.toString());
		}
	}

}
