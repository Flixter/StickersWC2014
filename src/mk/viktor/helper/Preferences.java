package mk.viktor.helper;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.preference.PreferenceManager;

public class Preferences {

	private static Preferences preferences = null;
	private static final String USER_PREFERENCE_KEY = "user_preferences";
	SharedPreferences sharedPreferences;
	LocationManager locationManager;
	Location location;
	Geocoder geoCoder;

	public Preferences(Context context) {
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		location = locationManager
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		geoCoder = new Geocoder(context, Locale.getDefault());

	}

	public static Preferences getPreferences() {
		return preferences;
	}

	public static Preferences getInstance(Context context) {
		if (preferences == null) {
			preferences = new Preferences(context);
		}
		return preferences;
	}

	public void logInUser(JSONObject userObject) throws JSONException {
		String userId = userObject.getString(Constants.USER_TABLE_ID_PARAM);
		String username = userObject
				.getString(Constants.USER_TABLE_USERNAME_PARAM);
		String city = userObject.getString(Constants.USER_TABLE_CITY_PARAM);
		long longtitude = userObject
				.getLong(Constants.USER_TABLE_LONGTITUDE_PARAM);
		long latitude = userObject.getLong(Constants.USER_TABLE_LATITUDE_PARAM);
		List<Sticker> stickers = null;
		User.instantiateUser(username, longtitude, latitude, city, stickers);
		this.sharedPreferences.edit().putString(USER_PREFERENCE_KEY, userId)
				.commit();

	}

	public boolean isUserLogged() {
		String defaultValueString = "$%noUser%$";
		String username = this.sharedPreferences.getString(USER_PREFERENCE_KEY,
				defaultValueString);
		return username.equals(defaultValueString) ? false : true;
	}

	public double getLongtitude() {
		if (location == null)
			return 0;
		return location.getLongitude();
	}

	public double getLatitude() {
		if (location == null)
			return 0;
		return location.getLatitude();
	}

	public String getCity() {
		try {
			List<Address> adresses = geoCoder.getFromLocation(getLatitude(),
					getLongtitude(), 1);

			if (adresses.size() > 0)
				return adresses.get(0).getLocality();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "Skopje";
	}
}
