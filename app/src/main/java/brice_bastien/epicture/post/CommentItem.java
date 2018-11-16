package brice_bastien.epicture.post;

import androidx.annotation.NonNull;

public class CommentItem {

	public String author;
	public String comment;
	public long time;

	public CommentItem(String author, String comment, long time) {
		this.author = author;
		this.comment = comment;
		this.time = time;
	}

	@NonNull
	@Override
	public String toString() {

		String ret = "";

		ret += "Author:\t" + author +"\n";
		ret += "Comment:\t" + comment + "\n";
		ret += "time:\t" + time + "\n";

		return ret;
	}
}
