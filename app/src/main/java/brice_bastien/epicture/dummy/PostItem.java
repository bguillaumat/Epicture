package brice_bastien.epicture.dummy;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class PostItem {
	public String title;
	public String id;
	public String ups;
	public String downs;
	public String link;
	public boolean favorite;
	public List<String> images = new ArrayList<>();

	public PostItem(String id, String title, String ups, String downs, String link, boolean favorite) {
		this.id = id;
		this.title = title;
		this.ups = ups;
		this.downs = downs;
		this.link = link;
		this.favorite = favorite;
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
		ret += "images:\n";
		for (String tmp : images) {
			ret += "\t" + tmp + "\n";
		}
		return ret;
	}
}
