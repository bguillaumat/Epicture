package brice_bastien.epicture.post;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.lang.*;

import androidx.annotation.NonNull;

public class PostItem {
	public enum FAV_TYPE {
		ALBUM,
		PHOTO
	}

	public enum VOTE_TYPE {
		LIKE,
		DISLIKE,
		NONE
	}

	public String title;
	public String id;
	public String description;
	public String deleteHash;
	public int ups;
	public int downs;
	public int views;
	public long time;
	public String link;
	public String imageFav;
	public FAV_TYPE favType;
	public VOTE_TYPE voteType;
	public boolean favorite;
	public String ownerName;
	public int commentNumber;
	public List<String> images = new ArrayList<>();

	public PostItem(String id, String title, int ups, int downs, String link, boolean favorite) {
		this.id = id;
		this.title = title;
		this.ups = ups;
		this.downs = downs;
		this.link = link;
		this.favorite = favorite;
		this.description = "";
		this.imageFav = null;
	}

	public void AddImage(String img) {
		images.add(img);
	}

	public static Comparator<PostItem> newerComparator = new Comparator<PostItem>() {

		public int compare(PostItem s1, PostItem s2) {
			Long time1 = s1.time;
			Long time2 = s2.time;

			return time2.compareTo(time1);
		}
	};

	public static Comparator<PostItem> olderComparator = new Comparator<PostItem>() {

		public int compare(PostItem s1, PostItem s2) {
			Long time1 = s1.time;
			Long time2 = s2.time;

			return time1.compareTo(time2);
		}
	};

	public static Comparator<PostItem> mostView = new Comparator<PostItem>() {

		public int compare(PostItem s1, PostItem s2) {
			Integer view1 = s1.views;
			Integer view2 = s2.views;

			return view2.compareTo(view1);
		}
	};

	public static Comparator<PostItem> mostLike = new Comparator<PostItem>() {

		public int compare(PostItem s1, PostItem s2) {
			Integer ups1 = s1.ups;
			Integer ups2 = s2.ups;

			return ups2.compareTo(ups1);
		}
	};

	public static Comparator<PostItem> username = new Comparator<PostItem>() {

		public int compare(PostItem s1, PostItem s2) {
			String username1 = s1.ownerName.toLowerCase();
			String username2 = s2.ownerName.toLowerCase();

			return username1.compareTo(username2);
		}
	};


	@Override
	@NonNull
	public String toString() {
		String ret = "";
		ret += "id:\t" + id + "\n";
		ret += "title:\t" + title + "\n";
		ret += "ups:\t" + ups + "\n";
		ret += "downs:\t" + downs + "\n";
		ret += "link:\t" + link + "\n";
		ret += "Favorite:\t" + favorite + "\n";
		ret += "Is album:\t" + favType + "\n";
		ret += "views:\t" + views + "\n";
		ret += "Vote:\t" + voteType + "\n";
		ret += "images:\n";
		for (String tmp : images) {
			ret += "\t" + tmp + "\n";
		}
		return ret;
	}

}
