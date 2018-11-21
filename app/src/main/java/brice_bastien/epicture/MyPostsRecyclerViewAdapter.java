package brice_bastien.epicture;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.flexbox.AlignSelf;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.rockerhieu.rvadapter.states.StatesRecyclerViewAdapter;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import net.openid.appauth.AuthorizationRequest;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.ImgurPicture.ImgurPicture;
import brice_bastien.epicture.PostsFragment.OnListFragmentInteractionListener;
import brice_bastien.epicture.post.ElapsedTime;
import brice_bastien.epicture.post.PostItem;

public class MyPostsRecyclerViewAdapter extends RecyclerView.Adapter<MyPostsRecyclerViewAdapter.ViewHolder> {

	private final List<PostItem> itemList;
	private final OnListFragmentInteractionListener mListener;
	public StatesRecyclerViewAdapter statesRecyclerViewAdapter;
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

	private void onGridLayout(ViewHolder holder, int position) {
		holder.seeMoreComments.setVisibility(View.GONE);
		holder.editButton.setVisibility(View.GONE);
		holder.deleteButton.setVisibility(View.GONE);
		holder.mFavorite.setVisibility(View.GONE);
		holder.mLike.setVisibility(View.GONE);
		holder.mDislike.setVisibility(View.GONE);
		holder.carouselView.setVisibility(View.GONE);
		holder.timePost.setVisibility(View.GONE);
		holder.mTitleView.setVisibility(View.GONE);
		holder.numberOfView.setVisibility(View.GONE);
		holder.mImageView.setVisibility(View.VISIBLE);
		holder.mImageView.setImageDrawable(null);
		List<String> images = holder.mItem.images;

		ImageView imageView = holder.mImageView;
		holder.mImageView.setImageDrawable(null);
		ImgurPicture imgurPicture = new ImgurPicture(images.get(0));

		GlideApp.with(holder.itemView)
				.load(imgurPicture.getBigSquare())
				.transition(DrawableTransitionOptions.withCrossFade())
				.thumbnail(GlideApp.with(holder.itemView)
						.load(imgurPicture.getSmallSquare())
						.transition(DrawableTransitionOptions.withCrossFade()))
				.error(new ColorDrawable(Color.RED))
				.override(Target.SIZE_ORIGINAL)
				.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
				.into(imageView);
	}

	private void onLinearLayout(final ViewHolder holder, final int position) {
		final List<String> images = holder.mItem.images;
		Resources res = holder.mView.getResources();

		holder.seeMoreComments.setVisibility(View.VISIBLE);
		holder.editButton.setVisibility(View.VISIBLE);
		holder.deleteButton.setVisibility(View.VISIBLE);
		holder.mFavorite.setVisibility(View.VISIBLE);
		holder.mLike.setVisibility(View.VISIBLE);
		holder.mDislike.setVisibility(View.VISIBLE);
		holder.carouselView.setVisibility(View.VISIBLE);
		holder.timePost.setVisibility(View.VISIBLE);
		holder.mTitleView.setVisibility(View.VISIBLE);
		holder.numberOfView.setVisibility(View.VISIBLE);
		holder.mImageView.setVisibility(View.VISIBLE);

		holder.mFavorite.setOnCheckedChangeListener(null);
		holder.mLike.setOnCheckedChangeListener(null);
		holder.mDislike.setOnCheckedChangeListener(null);

		if (holder.mItem.id.equals("null") || isMe(holder.mItem.ownerName)) {
			holder.seeMoreComments.setVisibility(View.GONE);
		} else
			holder.seeMoreComments.setVisibility(View.VISIBLE);
		if (isMe(holder.mItem.ownerName)) {
			holder.mDislike.setVisibility(View.GONE);
			holder.mLike.setVisibility(View.GONE);
			holder.mFavorite.setVisibility(View.GONE);
			holder.deleteButton.setVisibility(View.VISIBLE);
			holder.editButton.setVisibility(View.VISIBLE);
		} else {
			holder.mDislike.setVisibility(View.VISIBLE);
			holder.mLike.setVisibility(View.VISIBLE);
			holder.mFavorite.setVisibility(View.VISIBLE);
			holder.deleteButton.setVisibility(View.GONE);
			holder.editButton.setVisibility(View.GONE);
		}

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

		final String views = formatValue(holder.mItem.views, DECIMAL_FORMAT);
		holder.numberOfView.setText(res.getQuantityString(R.plurals.numberOfView, holder.mItem.views, views));

		ElapsedTime elapsedTime = new ElapsedTime(holder.mItem.time);
		holder.timePost.setText(elapsedTime.getTimeString(res).toUpperCase());

		if (images.size() <= 1) {
			holder.carouselView.setVisibility(View.GONE);
			holder.mImageView.setVisibility(View.VISIBLE);
			ImageView imageView = holder.mImageView;
			holder.mImageView.setImageDrawable(null);
			ImgurPicture imgurPicture = new ImgurPicture(images.get(0));

			GlideApp.with(holder.itemView)
					.load(imgurPicture.getUrl(holder.itemView.getContext()))
					.transition(DrawableTransitionOptions.withCrossFade())
					.thumbnail(GlideApp.with(holder.itemView)
							.load(imgurPicture.getSmall())
							.transition(DrawableTransitionOptions.withCrossFade()))
					.error(new ColorDrawable(Color.RED))
					.override(Target.SIZE_ORIGINAL)
					.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
					.into(imageView);
		} else {
			CarouselView carouselView = holder.carouselView;
			carouselView.setVisibility(View.VISIBLE);
			holder.mImageView.setVisibility(View.GONE);
			carouselView.setPageCount(images.size());

			carouselView.setImageClickListener(new ImageClickListener() {
				@Override
				public void onClick(int position) {
					if (null != mListener)
						mListener.onListFragmentInteraction(holder.mItem);
				}
			});
			carouselView.setImageListener(new ImageListener() {
				@Override
				public void setImageForPosition(int position, ImageView imageView) {
					imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
					imageView.setBackground(new ColorDrawable(Color.BLACK));
					ImgurPicture imgurPicture = new ImgurPicture(images.get(position));

					GlideApp.with(holder.itemView)
							.load(imgurPicture.getUrl(holder.itemView.getContext()))
							.transition(DrawableTransitionOptions.withCrossFade())
							.thumbnail(GlideApp.with(holder.itemView)
									.load(imgurPicture.getSmall())
									.transition(DrawableTransitionOptions.withCrossFade()))
							.error(new ColorDrawable(Color.RED))
							.override(Target.SIZE_ORIGINAL)
							.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
							.into(imageView);
				}
			});
		}
	}

	@Override
	public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
		holder.mItem = itemList.get(position);
		CardView card = holder.mView.findViewById(R.id.card_view);

		Display display = mainActivity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		float density = holder.mView.getResources()
				.getDisplayMetrics()
				.density;
		ViewGroup.LayoutParams lp = card.getLayoutParams();
		if (lp instanceof GridLayoutManager.LayoutParams) {
			card.setLayoutParams(lp);
			onGridLayout(holder, position);
		} else {
			card.setLayoutParams(lp);
			onLinearLayout(holder, position);
		}

		holder.editButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(context);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_edit);
				dialog.show();

				Button save = dialog.findViewById(R.id.save_btn);
				Button cancel = dialog.findViewById(R.id.cancel_action);

				final EditText title = dialog.findViewById(R.id.uploadPhotoTitle);
				final EditText description = dialog.findViewById(R.id.uploadPhotoDescription);

				title.setText(holder.mItem.title);
				if (!holder.mItem.description.isEmpty())
					description.setText(holder.mItem.description);

				save.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (title.getText().toString().isEmpty())
							Toast.makeText(v.getContext(), "Your photo need a title", Toast.LENGTH_SHORT).show();
						else {
							imgurApi.editImage(holder.mItem.favType == PostItem.FAV_TYPE.ALBUM, holder.mItem.id, title.getText().toString(), description.getText().toString());
							dialog.dismiss();
						}
					}
				});
				cancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

			}
		});

		holder.deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				alert.setTitle(R.string.delete_action);
				alert.setMessage(R.string.delete_warning);
				alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						imgurApi.delUserImg(holder.mItem.favType == PostItem.FAV_TYPE.ALBUM, holder.mItem.deleteHash);
						removeItem(position);
					}
				});
				alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				alert.show();
			}
		});

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

	private boolean isMe(String user) {
		return imgurApi.getUsername().equals(user);
	}

	public void updateData(List<PostItem> posts) {
		itemList.clear();
		itemList.addAll(posts);
		notifyDataSetChanged();
	}

	public void removeAll() {
		itemList.clear();
		statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
		notifyDataSetChanged();
	}

	public void addItem(int position, PostItem post) {
		if (position == 0) {
			itemList.add(post);
		} else {
			itemList.add(position, post);
		}
		statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
		notifyItemInserted(position);
	}

	public void removeItem(int position) {
		if (itemList.isEmpty()) {
			return;
		}
		itemList.remove(position);
		if (itemList.isEmpty()) {
			statesRecyclerViewAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
		}
		notifyItemRemoved(position);
	}

	public void orderByNewest() {
		Collections.sort(itemList, PostItem.newerComparator);
		notifyDataSetChanged();
	}

	public void orderByOldest() {
		Collections.sort(itemList, PostItem.olderComparator);
		notifyDataSetChanged();
	}

	public void orderByMostView() {
		Collections.sort(itemList, PostItem.mostView);
		notifyDataSetChanged();
	}

	public void orderByUps() {
		Collections.sort(itemList, PostItem.mostLike);
		notifyDataSetChanged();
	}

	public void orderByName() {
		Collections.sort(itemList, PostItem.username);
		notifyDataSetChanged();
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
		public final CarouselView carouselView;
		public final ImageButton deleteButton;
		public final ImageButton editButton;
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
			carouselView = view.findViewById(R.id.carousel_img);
			mDislike = view.findViewById(R.id.button_dislike);
			deleteButton = view.findViewById(R.id.delete_button);
			editButton = view.findViewById(R.id.edit_button);
		}
	}
}
