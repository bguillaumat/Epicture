package brice_bastien.epicture.ImgurApi;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import brice_bastien.epicture.PostsFragment;

public class ErrorListener implements Response.ErrorListener {

	private PostsFragment postsFragment = null;
	private StatesRecyclerViewAdapter statesRecyclerViewAdapter = null;

	ErrorListener() {

	}

	ErrorListener(StatesRecyclerViewAdapter statesRecyclerViewAdapter) {
		this.statesRecyclerViewAdapter = statesRecyclerViewAdapter;
	}

	ErrorListener(PostsFragment postsFragment) {
		this.postsFragment = postsFragment;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		if (postsFragment != null)
			postsFragment.statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
		if (statesRecyclerViewAdapter != null)
			statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
		Log.w("ErrorListener", error.toString());
	}
}
