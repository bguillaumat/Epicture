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
import brice_bastien.epicture.post.PostItem;

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
	public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
		holder.mItem = itemList.get(position);

		Resources res = holder.mView.getResources();
		Log.i("Item", holder.mItem.toString());
		holder.mFavorite.setOnCheckedChangeListener(null);
		holder.mFavorite.setChecked(holder.mItem.favorite);
		holder.mTitleView.setText(Html.fromHtml(res.getString(R.string.title_post, holder.mItem.ownerName, holder.mItem.title)));
		holder.seeMoreComments.setText(res.getQuantityString(R.plurals.numberOfComments, holder.mItem.commentNumber, holder.mItem.commentNumber));

		if (holder.mItem.commentNumber == 0) {
			holder.seeMoreComments.setVisibility(View.GONE);
		}

		String views = formatValue(holder.mItem.views, DECIMAL_FORMAT);


		holder.numberOfView.setText(res.getQuantityString(R.plurals.numberOfView, holder.mItem.views, views));

		Timestamp stamp = new Timestamp(System.currentTimeMillis());

		Date endDate = new Date(stamp.getTime());
		Date startDate = new Date(Long.parseLong(Long.toString(holder.mItem.time)) * 1000);


		long different = endDate.getTime() - startDate.getTime();

		System.out.println("startDate : " + startDate);
		System.out.println("endDate : " + endDate);
		System.out.println("different : " + different);

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		if (elapsedMinutes == 0 && elapsedHours == 0 && elapsedDays == 0) {
			holder.timePost.setText(res.getString(R.string.seconds_elapsed, elapsedSeconds).toUpperCase());
		} else if (elapsedHours == 0 && elapsedDays == 0) {
			holder.timePost.setText(res.getString(R.string.minutes_elapsed, elapsedMinutes).toUpperCase());
		} else if (elapsedDays == 0) {
			holder.timePost.setText(res.getString(R.string.hours_elapsed, elapsedHours).toUpperCase());
		} else
			holder.timePost.setText(res.getString(R.string.days_elapsed, elapsedDays).toUpperCase());


		ImageView imageView = holder.mImageView;
		holder.mImageView.setImageDrawable(null);
		String url = holder.mItem.images.get(0);

		if (url.endsWith(".gifv"))
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

		holder.seeMoreComments.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(context, PostComment.class);
				intent.putExtra("POST_ID", holder.mItem.id);
				context.startActivity(intent);
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
		}
	}
}
