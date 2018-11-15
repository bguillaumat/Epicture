package brice_bastien.epicture.Settings;

public class SettingItem {

	private boolean mature;
	private boolean publishType;
	private boolean messaging;
	private boolean newsletter;
	private String email;
	private String albumType;

	public SettingItem(boolean mature, boolean publishType, boolean messaging, boolean newsletter, String email, String albumType) {
		this.mature = mature;
		this.publishType = publishType;
		this.messaging = messaging;
		this.newsletter = newsletter;
		this.email = email;
		this.albumType = albumType;

	}

	public boolean isMature() {
		return mature;
	}

	public String getEmail() {
		return email;
	}

	public boolean isPublishType() {
		return publishType;
	}

	public boolean isMessaging() {
		return messaging;
	}

	public boolean isNewsletter() {
		return newsletter;
	}

	public String getAlbumType() {
		return albumType;
	}
}
