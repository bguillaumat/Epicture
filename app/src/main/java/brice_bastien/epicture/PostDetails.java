package brice_bastien.epicture;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.ImgurPicture.ImgurPicture;
import brice_bastien.epicture.post.ElapsedTime;
import brice_bastien.epicture.post.PostItem;

public class PostDetails extends AppCompatActivity {

	String id;
	Boolean isAlbum;
	String ownerName;
	int commentNumber;
	PostDetails view;
	CircularImageView userAvatar;
	CarouselView carouselView;
	MaterialButton seeMoreComments;
	Resources res;
	TextView postTitle;
	TextView numberOfView;
	TextView description;
	TextView timePost;
	ImgurApi imgurApi;
	SharedPreferences sharedPreferences;
	String Token;
	String Username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_details);

		sharedPreferences = getSharedPreferences(getString(R.string.user_info_pref), Context.MODE_PRIVATE);
		Token = sharedPreferences.getString("User_Token", null);
		Username = sharedPreferences.getString("Username", null);

		if (Token == null || Username == null) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			finish();
		}

		Intent intent = getIntent();
		id = intent.getStringExtra("ID");
		isAlbum = intent.getBooleanExtra("IS_ALBUM", false);
		ownerName = intent.getStringExtra("USERNAME");
		commentNumber = intent.getIntExtra("COMMENTS", 0);
		carouselView = findViewById(R.id.carousel_detail_img);
		userAvatar = findViewById(R.id.user_avatar);
		postTitle = findViewById(R.id.post_title);
		numberOfView = findViewById(R.id.numberOfView);
		description = findViewById(R.id.description);
		seeMoreComments = findViewById(R.id.see_more_comments);
		timePost = findViewById(R.id.timePost);
		res = getResources();
		view = this;

		imgurApi = new ImgurApi(getApplicationContext(), Username, Token);
		imgurApi.getDetails(isAlbum, id, this);
	}

	public void setView(final PostItem postItem) {
		Log.i("setView", postItem.toString());

		setTitle(postItem.title);

		final List<String> images = postItem.images;

		carouselView.setVisibility(View.VISIBLE);
		carouselView.setImageListener(new ImageListener() {
			@Override
			public void setImageForPosition(int position, ImageView imageView) {
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setBackground(new ColorDrawable(Color.BLACK));
				ImgurPicture imgurPicture = new ImgurPicture(images.get(position));

				GlideApp.with(view)
						.load(imgurPicture.getUrl())
						.transition(DrawableTransitionOptions.withCrossFade())
						.thumbnail(GlideApp.with(view)
								.load(imgurPicture.getSmall())
								.transition(DrawableTransitionOptions.withCrossFade()))
						.error(new ColorDrawable(Color.RED))
						.override(Target.SIZE_ORIGINAL)
						.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
						.into(imageView);
			}
		});
		carouselView.setPageCount(images.size());
		if (images.size() > 10) {
			carouselView.setIndicatorVisibility(View.GONE);
			carouselView.playCarousel();
		}

		final String views = formatValue(postItem.views, DECIMAL_FORMAT);
		numberOfView.setText(res.getQuantityString(R.plurals.numberOfView, postItem.views, views));

		imgurApi.getUsrAvatar(userAvatar, ownerName);
		postTitle.setText(Html.fromHtml(res.getString(R.string.title_post, ownerName, postItem.title)));

		if (!postItem.description.isEmpty()) {
			description.setVisibility(View.VISIBLE);
			description.setText(postItem.description);
		}

		seeMoreComments.setText(res.getQuantityString(R.plurals.numberOfComments, commentNumber, commentNumber));
		if (postItem.id.equals("null")) {
			seeMoreComments.setVisibility(View.GONE);
		}

		final AppCompatActivity activity = this;
		seeMoreComments.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(activity, PostComment.class);
				intent.putExtra("POST_ID", postItem.id);
				activity.startActivity(intent);
			}
		});

		ElapsedTime elapsedTime = new ElapsedTime(postItem.time);
		timePost.setText(elapsedTime.getTimeString(res).toUpperCase());
	}

	private static final String DECIMAL_FORMAT = "###,###.#";

	private String formatValue(Number value, String formatString) {
		DecimalFormatSymbols formatSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		formatSymbols.setDecimalSeparator('.');
		formatSymbols.setGroupingSeparator(' ');
		DecimalFormat formatter = new DecimalFormat(formatString, formatSymbols);
		return formatter.format(value);
	}


}
