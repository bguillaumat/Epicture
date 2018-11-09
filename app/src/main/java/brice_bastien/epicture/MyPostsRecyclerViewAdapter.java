package brice_bastien.epicture;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import brice_bastien.epicture.PostsFragment.OnListFragmentInteractionListener;
import brice_bastien.epicture.dummy.PostItem;

/**
 * {@link RecyclerView.Adapter} that can display a {@link PostItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyPostsRecyclerViewAdapter extends RecyclerView.Adapter<MyPostsRecyclerViewAdapter.ViewHolder> {

	private final List<PostItem> itemList;
	private final OnListFragmentInteractionListener mListener;

	MyPostsRecyclerViewAdapter(OnListFragmentInteractionListener listener) {
		itemList = new ArrayList<>();
		mListener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_posts, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

		holder.mItem = itemList.get(position);
		holder.mTitleView.setText(itemList.get(position).title);
		VideoView videoView = holder.mVideoView;
		ImageView imageView = holder.mImageView;

		if (itemList.get(position).images.get(0).endsWith(".mp4")) {
			imageView.setVisibility(View.GONE);
			MediaController mediaController = new MediaController(holder.mView.getContext());
			mediaController.setAnchorView(videoView);
			videoView.setMediaController(mediaController);
			videoView.setVideoURI(Uri.parse(itemList.get(position).images.get(0)));
		} else {
			videoView.setVisibility(View.GONE);
			GlideApp.with(holder.mView)
					.load(itemList.get(position).images.get(0))
					.diskCacheStrategy(DiskCacheStrategy.ALL)
					.into(imageView);
		}
		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener) {
					mListener.onListFragmentInteraction(holder.mItem);
				}
			}
		});

	}

	public void updateData(List<PostItem> posts) {
		itemList.clear();
		itemList.addAll(posts);
		notifyDataSetChanged();
	}

	public void removeAll() {
		itemList.clear();
		notifyDataSetChanged();
	}

	public void addItem(int position, PostItem post) {
		if (position == 0) {
			itemList.add(post);
		} else {
			itemList.add(position, post);
		}
		notifyItemChanged(position);
	}

	public void removeItem(int position) {
		if (itemList.isEmpty()) {
			return;
		}
		itemList.remove(position);
		notifyItemRemoved(position);
	}

	@Override
	public int getItemCount() {
		return itemList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View mView;
		public final TextView mTitleView;
		public final ImageView mImageView;
		public final VideoView mVideoView;
		public PostItem mItem;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mTitleView = view.findViewById(R.id.post_title);
			mImageView = view.findViewById(R.id.post_img);
			mVideoView = view.findViewById(R.id.post_vid);
		}
	}
}
