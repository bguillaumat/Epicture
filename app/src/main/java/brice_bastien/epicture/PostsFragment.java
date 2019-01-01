package brice_bastien.epicture;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.post.PostItem;

public class PostsFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String ARG_COLUMN_COUNT = "column-count";
	private int mColumnCount = 2;
	private OnListFragmentInteractionListener mListener;
	private RecyclerView recyclerView;
	public MyPostsRecyclerViewAdapter adapter = null;
	public StatesRecyclerViewAdapter statesRecyclerViewAdapter = null;
	public ImgurApi imgurApi;

	public PostsFragment() {
	}

	@SuppressWarnings("unused")
	public static PostsFragment newInstance(int columnCount, ImgurApi imgurApi) {
		PostsFragment fragment = new PostsFragment();
		fragment.imgurApi = imgurApi;
		Bundle args = new Bundle();
		args.putInt(ARG_COLUMN_COUNT, columnCount);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View fragView = inflater.inflate(R.layout.fragment_posts_list, container, false);
		recyclerView = fragView.findViewById(R.id.list);
		final SwipeRefreshLayout swipeRefreshLayout = fragView.findViewById(R.id.refreshSwipe);
		final PostsFragment postsFragment = this;

		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				imgurApi.refresh_data(postsFragment);
				swipeRefreshLayout.setRefreshing(false);
			}
		});
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(container.getContext());
		Boolean grid_view = sharedPrefs.getBoolean(SettingsActivity.KEY_PREF_GRID_VIEW, false);
		Integer columnNum = sharedPrefs.getInt(SettingsActivity.KEY_PREF_GRID_COLUMN, 2);

		sharedPrefs.registerOnSharedPreferenceChangeListener(this);

		// Set the adapter
		if (recyclerView != null) {
			Context context = recyclerView.getContext();
			recyclerView.setHasFixedSize(false);
			if (!grid_view) {
				recyclerView.setLayoutManager(new LinearLayoutManager(context));
			} else {
				recyclerView.setLayoutManager(new StaggeredGridLayoutManager(columnNum, 1));
			}
			View loadingView = getLayoutInflater().inflate(R.layout.view_loading, recyclerView, false);
			View emptyView = getLayoutInflater().inflate(R.layout.view_empty, recyclerView, false);
			View errorView = getLayoutInflater().inflate(R.layout.view_error, recyclerView, false);
			adapter = new MyPostsRecyclerViewAdapter(mListener, getContext(), (MainActivity)getActivity(), imgurApi);
			statesRecyclerViewAdapter = new StatesRecyclerViewAdapter(adapter, loadingView, emptyView, errorView);
			recyclerView.setAdapter(statesRecyclerViewAdapter);
			adapter.statesRecyclerViewAdapter = statesRecyclerViewAdapter;
			statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_LOADING);
		}
		return fragView;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnListFragmentInteractionListener) {
			mListener = (OnListFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnListFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Integer columnNum = sharedPreferences.getInt(SettingsActivity.KEY_PREF_GRID_COLUMN, 2);

		switch (key) {
			case SettingsActivity.KEY_PREF_GRID_VIEW:
				if (!sharedPreferences.getBoolean(key, false)) {
					recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
				} else {
					recyclerView.setLayoutManager(new StaggeredGridLayoutManager(columnNum, 1));
				}
				recyclerView.setAdapter(statesRecyclerViewAdapter);
				break;
			case SettingsActivity.KEY_PREF_GRID_COLUMN:
				if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
					recyclerView.setLayoutManager(new StaggeredGridLayoutManager(columnNum, 1));
					recyclerView.setAdapter(statesRecyclerViewAdapter);
				}
				break;
		}
	}

	public interface OnListFragmentInteractionListener {
		void onListFragmentInteraction(PostItem item);
	}
}
