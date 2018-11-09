package brice_bastien.epicture;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

class Post {

	private String title;
	private String id;
	private String ups;
	private String downs;
	private String link;
	private List<String> images = new ArrayList<>();

	Post(String id, String title, String ups, String downs, String link) {
		this.id = id;
		this.title = title;
		this.ups = ups;
		this.downs = downs;
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public String getId() {
		return id;
	}

	public String getUps() {
		return ups;
	}

	public String getDowns() {
		return downs;
	}

	public String getLink() {
		return link;
	}

	public List<String> getImages() {
		return images;
	}

	void AddImage(String img) {
		images.add(img);
	}

	@NonNull
	@Override
	public String toString() {
		String ret = "";
		ret += "id:\t" + id + "\n";
		ret += "title:\t" + title + "\n";
		ret += "ups:\t" + ups + "\n";
		ret += "downs:\t" + downs + "\n";
		ret += "link:\t" + link + "\n";
		ret += "images:\n";
		for (String tmp : images) {
			ret += "\t" + tmp + "\n";
		}
		return ret;
	}
}
