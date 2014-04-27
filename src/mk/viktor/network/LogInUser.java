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

import android.os.AsyncTask;

public class LogInUser extends AsyncTask<Void, Boolean, JSONObject> {

	List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	String[] paramsName = new String[] { Constants.USER_TABLE_USERNAME_PARAM,
			Constants.USER_TABLE_PASSWORD_PARAM };

	public LogInUser(String username, String password) {
		nameValuePair.add(new BasicNameValuePair(paramsName[0], username));
		nameValuePair.add(new BasicNameValuePair(paramsName[1], password));
	}

	@Override
	protected JSONObject doInBackground(Void... params) {
		HttpClient httpClient = new DefaultHttpClient();
		// Create the HTTP post
		HttpPost httpPost = new HttpPost(Constants.MAIN_URL
				+ Constants.LOGIN_USER_EXTENSION);
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
		try {
			Preferences.getPreferences().logInUser(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
