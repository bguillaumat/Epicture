package brice_bastien.epicture.post;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class PostItem {
	public enum FAV_TYPE {
		ALBUM,
		PHOTO
	}
	public String title;
	public String id;
	public int ups;
	public int downs;
	public int views;
	public long time;
	public String link;
	public String imageFav;
	public FAV_TYPE favType;
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
		this.imageFav = null;
	}

	public void AddImage(String img) {
		images.add(img);
	}

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
		ret += "images:\n";
		for (String tmp : images) {
			ret += "\t" + tmp + "\n";
		}
		return ret;
	}
}
