package mk.viktor.helper;

import java.util.List;

public class User {

	private static User user;
	private String username;
	private long longtitude;
	private long latitude;
	private String city;
	private List<Sticker> stickers;

	private User(String username, long longtitude, long latitude, String city,
			List<Sticker> stickers) {
		super();
		this.username = username;
		this.longtitude = longtitude;
		this.latitude = latitude;
		this.city = city;
		this.stickers = stickers;
	}

	public static User getUser() {
		return user;
	}

	public static User instantiateUser(String username, long longtitude,
			long latitude, String city, List<Sticker> stickers) {

		if (user == null) {
			user = new User(username, longtitude, latitude, city, stickers);
		}
		return user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getLongtitude() {
		return longtitude;
	}

	public void setLongtitude(long longtitude) {
		this.longtitude = longtitude;
	}

	public long getLatitude() {
		return latitude;
	}

	public void setLatitude(long latitude) {
		this.latitude = latitude;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<Sticker> getStickers() {
		return stickers;
	}

	public void setStickers(List<Sticker> stickers) {
		this.stickers = stickers;
	}

}
