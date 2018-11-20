package brice_bastien.epicture.post;

import java.util.Comparator;

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

	public static Comparator<PostItem> newerComparator = new Comparator<PostItem>() {

		public int compare(PostItem s1, PostItem s2) {
			Long time1 = s1.time;
			Long time2 = s2.time;

			return time2.compareTo(time1);
		}
	};


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
