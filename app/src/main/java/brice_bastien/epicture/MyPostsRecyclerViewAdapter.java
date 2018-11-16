package brice_bastien.epicture;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.PostsFragment.OnListFragmentInteractionListener;
import brice_bastien.epicture.post.ElapsedTime;
import brice_bastien.epicture.post.PostItem;

public class MyPostsRecyclerViewAdapter extends RecyclerView.Adapter<MyPostsRecyclerViewAdapter.ViewHolder> {

	private final List<PostItem> itemList;
	private final OnListFragmentInteractionListener mListener;
	private Context context;
	private MainActivity mainActivity;
	private ImgurApi imgurApi;

	MyPostsRecyclerViewAdapter(OnListFragmentInteractionListener listener, Context context, MainActivity mainActivity, ImgurApi imgurApi) {
		itemList = new ArrayList<>();
		this.context = context;
		this.imgurApi = imgurApi;
		this.mainActivity = mainActivity;
		mListener = listener;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_posts, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		holder.mItem = itemList.get(position);

		Resources res = holder.mView.getResources();
		Log.i("Item", holder.mItem.toString());

		holder.mFavorite.setOnCheckedChangeListener(null);
		holder.mLike.setOnCheckedChangeListener(null);
		holder.mDislike.setOnCheckedChangeListener(null);

		if (holder.mItem.voteType == PostItem.VOTE_TYPE.LIKE) {
			holder.mLike.setChecked(true);
		} else if (holder.mItem.voteType == PostItem.VOTE_TYPE.DISLIKE) {
			holder.mDislike.setChecked(true);
		} else {
			holder.mLike.setChecked(false);
			holder.mDislike.setChecked(false);
		}
		holder.mFavorite.setChecked(holder.mItem.favorite);
		holder.mTitleView.setText(Html.fromHtml(res.getString(R.string.title_post, holder.mItem.ownerName, holder.mItem.title)));
		holder.seeMoreComments.setText(res.getQuantityString(R.plurals.numberOfComments, holder.mItem.commentNumber, holder.mItem.commentNumber));
		if (holder.mItem.commentNumber == 0) {
			holder.seeMoreComments.setVisibility(View.GONE);
		}

		final String views = formatValue(holder.mItem.views, DECIMAL_FORMAT);
		holder.numberOfView.setText(res.getQuantityString(R.plurals.numberOfView, holder.mItem.views, views));

		ElapsedTime elapsedTime = new ElapsedTime(holder.mItem.time);
		holder.timePost.setText(elapsedTime.getTimeString(res).toUpperCase());


		ImageView imageView = holder.mImageView;
		holder.mImageView.setImageDrawable(null);
		String url = holder.mItem.images.get(0);

		if (url.endsWith(".mp4"))
			url = url.replace(".mp4", "h.jpg");
		else if (url.endsWith(".gifv"))
			url = url.replace(".gifv", "h.jpg");

		GlideApp.with(holder.itemView)
				.load(url)
				.override(Target.SIZE_ORIGINAL)
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
				.into(imageView);

		holder.mFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
				final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 0.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
				scaleAnimation.setDuration(500);
				BounceInterpolator bounceInterpolator = new BounceInterpolator();
				scaleAnimation.setInterpolator(bounceInterpolator);
				compoundButton.startAnimation(scaleAnimation);

				imgurApi.addImgFav(holder.mItem.imageFav, holder.mItem.favType);
			}
		});

		holder.mLike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				holder.mDislike.setOnCheckedChangeListener(null);
				holder.mDislike.setChecked(false);
				holder.mDislike.setOnCheckedChangeListener(this);

				if (!isChecked) {
					imgurApi.addVote(holder.mItem.imageFav, "veto");
				} else
					imgurApi.addVote(holder.mItem.imageFav, "up");
			}
		});

		holder.mDislike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				holder.mLike.setOnCheckedChangeListener(null);
				holder.mLike.setChecked(false);
				holder.mLike.setOnCheckedChangeListener(this);

				if (!isChecked) {
					imgurApi.addVote(holder.mItem.imageFav, "veto");
				} else
					imgurApi.addVote(holder.mItem.imageFav, "down");
			}
		});

		holder.seeMoreComments.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(mainActivity, PostComment.class);
				intent.putExtra("POST_ID", holder.mItem.id);
				mainActivity.startActivity(intent);
			}
		});

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != mListener)
					mListener.onListFragmentInteraction(holder.mItem);
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

	private static final String DECIMAL_FORMAT = "###,###.#";

	private String formatValue(Number value, String formatString) {
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		formatSymbols.setDecimalSeparator('.');
		formatSymbols.setGroupingSeparator(' ');
		DecimalFormat formatter = new DecimalFormat(formatString, formatSymbols);
		return formatter.format(value);
	}

	@Override
	public int getItemCount() {
		return itemList.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder {
		public final View mView;
		public final TextView mTitleView;
		public final ImageView mImageView;
		public final TextView numberOfView;
		public final TextView timePost;
		public final TextView seeMoreComments;
		public final ToggleButton mFavorite;
		public final ToggleButton mLike;
		public final ToggleButton mDislike;
		public PostItem mItem;

		ViewHolder(View view) {
			super(view);
			mView = view;
			mTitleView = view.findViewById(R.id.post_title);
			mImageView = view.findViewById(R.id.post_img);
			seeMoreComments = view.findViewById(R.id.see_more_comments);
			timePost = view.findViewById(R.id.timePost);
			numberOfView = view.findViewById(R.id.numberOfView);
			mFavorite = view.findViewById(R.id.button_favorite);
			mLike = view.findViewById(R.id.button_like);
			mDislike = view.findViewById(R.id.button_dislike);
		}
	}
}
