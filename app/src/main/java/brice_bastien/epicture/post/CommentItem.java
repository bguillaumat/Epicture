package brice_bastien.epicture.post;

import androidx.annotation.NonNull;

public class CommentItem {

	public String author;
	public String comment;

	public CommentItem(String author, String comment) {
		this.author = author;
		this.comment = comment;
	}

	@NonNull
	@Override
	public String toString() {

		String ret = "";

		ret += "Author:\t" + author +"\n";
		ret += "Comment:\t" + comment + "\n";

		return ret;
	}
}
