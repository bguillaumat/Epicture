package brice_bastien.epicture.post;

import android.content.Context;
import android.content.res.Resources;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
		holder.username.setText(Html.fromHtml(res.getString(R.string.author, holder.mItem.author)));
		holder.comment.setText(holder.mItem.comment);
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


	class ViewHolder extends RecyclerView.ViewHolder {
		public final View mView;
		public CommentItem mItem;
		public final TextView comment;
		public final TextView username;

		ViewHolder(View view) {
			super(view);
			mView = view;
			comment = view.findViewById(R.id.commentary);
			username = view.findViewById(R.id.comment_username);
		}
	}

}
