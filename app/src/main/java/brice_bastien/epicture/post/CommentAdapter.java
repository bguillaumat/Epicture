package brice_bastien.epicture.post;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import brice_bastien.epicture.GlideApp;
import brice_bastien.epicture.ImgurApi.ImgurApi;
import brice_bastien.epicture.R;

public class CommentAdapter  extends RecyclerView.Adapter<CommentAdapter.ViewHolder>  {

	private final List<CommentItem> itemList;
	Context context;
	ImgurApi imgurApi;

	public CommentAdapter(Context context, ImgurApi imgurApi) {
		itemList = new ArrayList<>();
		this.context = context;
		this.imgurApi = imgurApi;
	}

	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_comments, parent, false);
		return new ViewHolder(view);

	}

	@Override
	public int getItemCount() {
		return itemList.size();
	}

	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.mItem = itemList.get(position);
		Resources res = holder.mView.getResources();

		ElapsedTime elapsedTime = new ElapsedTime(holder.mItem.time);
		holder.time.setText(elapsedTime.getTimeString(res).toUpperCase());

		ImageView imageView = holder.commentPic;
		imageView.setImageDrawable(null);
		String commentary = holder.mItem.comment;

		imgurApi.getUsrAvatar(holder.userAvatar, holder.mItem.author);

		ArrayList<String> list = pullLinks(holder.mItem.comment);
		if (list.size() > 0) {
			String pic = list.get(0);

			if (pic.endsWith(".gifv") || pic.endsWith(".mp4"))
				pic = pic.replace(".gifv", "h.jpg");

			String extension = pic.substring(pic.lastIndexOf(".") + 1);

			if (!extension.isEmpty() && extension.length() <= 4) {
				imageView.setVisibility(View.VISIBLE);
				GlideApp.with(context)
						.load(pic)
						.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
						.into(imageView);
				commentary = commentary.replace(list.get(0), "");
			}
		}
		holder.comment.setText(Html.fromHtml(res.getString(R.string.title_post, holder.mItem.author, commentary)));
		list.clear();
	}

	public void updateData(List<CommentItem> posts) {
		itemList.clear();
		itemList.addAll(posts);
		notifyDataSetChanged();
	}

	public void removeAll() {
		itemList.clear();
		notifyDataSetChanged();
	}

	public void addItem(int position, CommentItem post) {
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

	private ArrayList<String> pullLinks(String text) {
		ArrayList<String> links = new ArrayList<>();

		String regex = "\\(?\\b(http://|https://|www[.])[-A-Za-z0-9+&amp;@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&amp;@#/%=~_()|]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		while(m.find()) {
			String urlStr = m.group();
			if (urlStr.startsWith("(") && urlStr.endsWith(")")) {
				urlStr = urlStr.substring(1, urlStr.length() - 1);
			}
			links.add(urlStr);
		}
		return links;
	}


	class ViewHolder extends RecyclerView.ViewHolder {
		public final View mView;
		public CommentItem mItem;
		public final TextView time;
		public final TextView comment;
		public final ImageView commentPic;
		public final ImageView userAvatar;

		ViewHolder(View view) {
			super(view);
			mView = view;
			time = view.findViewById(R.id.elapsed_commentary);
			comment = view.findViewById(R.id.commentary);
			commentPic = view.findViewById(R.id.commentary_img);
			userAvatar = view.findViewById(R.id.user_avatar);
		}
	}

}
