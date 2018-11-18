package brice_bastien.epicture.ImgurApi;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import brice_bastien.epicture.PostDetails;
import brice_bastien.epicture.post.PostItem;

public class ResponsePostDetail implements Response.Listener<JSONObject> {

	private PostDetails postDetails;

	ResponsePostDetail(PostDetails postDetails) {
		this.postDetails = postDetails;
	}

	@Override
	public void onResponse(JSONObject response) {
		try {
				JSONObject obj = new JSONObject(response.getString("data"));
				Log.i("PostDetails", obj.toString(2));
				PostItem post = new PostItem(obj.getString("id"), obj.getString("title"), 0, 0, obj.getString("link"), obj.getBoolean("favorite"));

				if (obj.has("ups") && obj.has("downs") && !obj.getString("ups").equals("null") && !obj.getString("downs").equals("null")) {
					post.ups = obj.getInt("ups");
					post.downs = obj.getInt("downs");
				}
				if (obj.has("description") && !obj.getString("description").equals("null")) {
					post.description = obj.getString("description");
				} else {
					post.description = "";
				}
				if (obj.has("vote") && obj.getString("vote").equals("null")) {
					post.voteType = PostItem.VOTE_TYPE.NONE;
				} else if (obj.has("vote")) {
					if (obj.getString("vote").equals("up")) {
						post.voteType = PostItem.VOTE_TYPE.LIKE;
					} else if (obj.getString("vote").equals("down"))
						post.voteType = PostItem.VOTE_TYPE.DISLIKE;
					else
						post.voteType = PostItem.VOTE_TYPE.NONE;
				} else
					post.voteType = PostItem.VOTE_TYPE.NONE;

				if (obj.getString("title").equals("null"))
					post.title = " ";
				post.time = obj.getLong("datetime");
				if (obj.has("is_album") && obj.getBoolean("is_album")) {
					post.imageFav = obj.getString("id");
					post.favType = PostItem.FAV_TYPE.ALBUM;
				} else {
					post.imageFav = obj.getString("id");
					post.favType = PostItem.FAV_TYPE.PHOTO;
				}
				post.ownerName = obj.getString("account_url");
				if (obj.has("comment_count") && !obj.getString("comment_count").equals("null"))
					post.commentNumber = obj.getInt("comment_count");
				post.views = obj.getInt("views");
				if (obj.isNull("images")) {
					post.AddImage(obj.getString("link"));
					postDetails.setView(post);
					return;
				}
				JSONArray images = new JSONArray(obj.getString("images"));
				for (int j = 0; j < images.length(); j++) {
					JSONObject tmp_img = new JSONObject(images.getString(j));
					if (tmp_img.has("gifv")) {
						post.AddImage(tmp_img.getString("gifv"));
					} else
						post.AddImage(tmp_img.getString("link"));
				}
				postDetails.setView(post);
		} catch (Exception e) {
			Log.w("PostDetails", e.getStackTrace().toString() + e.toString());
		}
	}
}
