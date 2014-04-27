package mk.viktor.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mk.viktor.helper.Constants;
import mk.viktor.helper.Preferences;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class CreateUser extends AsyncTask<Void, Void, JSONObject> {

	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	String[] paramsName = new String[] { Constants.USER_TABLE_USERNAME_PARAM,
			Constants.USER_TABLE_PASSWORD_PARAM,
			Constants.USER_TABLE_LATITUDE_PARAM,
			Constants.USER_TABLE_LONGTITUDE_PARAM,
			Constants.USER_TABLE_CITY_PARAM,
			Constants.USER_TABLE_STICKERS_PARAM };
	TextView errorMessage;

	public CreateUser(TextView errorMessage, String... parametars) {
		for (int i = 0; i < parametars.length; i++)
			nameValuePair.add(new BasicNameValuePair(paramsName[i],
					parametars[i]));
		this.errorMessage = errorMessage;
	}

	@Override
	protected JSONObject doInBackground(Void... params) {
		HttpClient httpClient = new DefaultHttpClient();
		// Create the HTTP post
		HttpPost httpPost = new HttpPost(Constants.MAIN_URL
				+ Constants.CREATE_USER_EXTENSION);
		HttpResponse response = null;
		JSONObject responseJsonObject = null;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
			response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream;
				inputStream = entity.getContent();

				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream));
				StringBuilder builder = new StringBuilder();
				for (String line = null; (line = bufferedReader.readLine()) != null;) {
					builder.append(line).append("\n");
				}
				responseJsonObject = new JSONObject(builder.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();

		}
		return responseJsonObject;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		String userSucessfullyLoggedIn = "User Sucessfully Signed Up!";
		Log.v(Constants.RESPONSE_OBJECT_LOG_TAG, result.toString());
		try {
			if (result.isNull("errors")) {
				errorMessage.setTextColor(Color.GREEN);
				errorMessage.setText(userSucessfullyLoggedIn);
				Log.v(Constants.RESPONSE_OBJECT_LOG_TAG, result.toString());
			} else {
				String errors = result.getString("errors");
				// Format error message
				char c = '"';
				errors = errors.replace(c, ' ');
				errors = errors.replaceAll("[{:}]", " ");
				errors = errors.replaceAll("\\,", "and");
				Log.v(Constants.RESPONSE_OBJECT_LOG_TAG, errors);
				errorMessage.setTextColor(Color.RED);
				errorMessage.setText(errors.trim());
			}
			// Preferences.getPreferences().logInUser(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			Preferences.getPreferences().logInUser(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
