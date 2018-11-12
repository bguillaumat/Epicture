package brice_bastien.epicture;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.PostsFragment.OnListFragmentInteractionListener;
import brice_bastien.epicture.dummy.PostItem;

public class MyPostsRecyclerViewAdapter extends RecyclerView.Adapter<MyPostsRecyclerViewAdapter.ViewHolder> {

	private final List<PostItem> itemList;
	private final OnListFragmentInteractionListener mListener;
	private Context context;
	private ImgurApi imgurApi;

	MyPostsRecyclerViewAdapter(OnListFragmentInteractionListener listener, Context context, ImgurApi imgurApi) {
		itemList = new ArrayList<>();
		this.context = context;
		this.imgurApi = imgurApi;
		mListener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_posts, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onViewRecycled(@NonNull ViewHolder holder) {
		super.onViewRecycled(holder);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		holder.mItem = itemList.get(position);
		if (holder.mItem.title.equals("null"))
			holder.mTitleView.setText("");
		else
			holder.mTitleView.setText(holder.mItem.title);
		VideoView videoView = holder.mVideoView;
		ImageView imageView = holder.mImageView;

		if (holder.mItem.images.get(0).endsWith(".mp4")) {
			Uri url = Uri.parse(holder.mItem.images.get(0));

			imageView.setVisibility(View.GONE);
			MediaController mediaController = new MediaController(holder.mView.getContext());
			mediaController.setAnchorView(videoView);
			videoView.setMediaController(mediaController);
			videoView.setVideoURI(url);
		} else {
			videoView.setVisibility(View.GONE);
			holder.mImageView.setImageDrawable(null);
			GlideApp.with(holder.itemView)
					.asDrawable()
					.load(holder.mItem.images.get(0))
					.override(Target.SIZE_ORIGINAL)
					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
					.centerCrop()
					.into(imageView);
		}
		holder.mFavorite.setChecked(holder.mItem.favorite);
		holder.mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
				scaleAnimation.setDuration(500);
				BounceInterpolator bounceInterpolator = new BounceInterpolator();
				scaleAnimation.setInterpolator(bounceInterpolator);
				compoundButton.startAnimation(scaleAnimation);

				imgurApi.addImgFav(holder.mItem.id);

			}});

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener) {
					mListener.onListFragmentInteraction(holder.mItem);
					Toast.makeText(v.getContext(), "You click on ", Toast.LENGTH_SHORT).show();
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
		notifyItemInserted(position);
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
		public final ToggleButton mFavorite;
		public PostItem mItem;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mTitleView = view.findViewById(R.id.post_title);
			mImageView = view.findViewById(R.id.post_img);
			mVideoView = view.findViewById(R.id.post_vid);
			mFavorite = view.findViewById(R.id.button_favorite);
		}
	}
}
