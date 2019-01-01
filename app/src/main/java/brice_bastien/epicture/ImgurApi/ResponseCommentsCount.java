package brice_bastien.epicture.ImgurApi;

import android.util.Log;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONObject;

import brice_bastien.epicture.R;

public class ResponseCommentsCount implements Response.Listener<JSONObject> {

	private TextView commentCountText;

	ResponseCommentsCount(TextView commentCountText) {
		this.commentCountText = commentCountText;
	}

	@Override
	public void onResponse(JSONObject response) {
		try {
			int commentCount = response.getInt("data");
			this.commentCountText.setText(commentCountText.getContext().getApplicationContext().getResources().getQuantityString(R.plurals.commentCount, commentCount, commentCount));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
