package mk.viktor.fragments;

import java.util.concurrent.ExecutionException;

import mk.viktor.adapter.TabsPagerAdapter;
import mk.viktor.helper.Preferences;
import mk.viktor.network.CreateUser;
import mk.viktor.network.LogInUser;
import mk.viktor.stickers.MainActivity;
import mk.viktor.stickers.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FriendsList extends Fragment implements OnClickListener {

	private View myView;

	// Views
	Button btnSignUp;
	Button btnLogIn;
	EditText etUsername;
	EditText etPassword;
	TextView errormessage;

	String username;
	String password;
	Preferences preferences;

	LayoutInflater inflater;
	ViewGroup container;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO check if the user is logged!
		this.inflater = inflater;
		this.container = container;
		myView = inflater.inflate(R.layout.user_login_screen, container, false);
		initialize();
		return myView;
	}

	private void initialize() {
		btnSignUp = (Button) myView.findViewById(R.id.btn_SignUp);
		btnLogIn = (Button) myView.findViewById(R.id.btn_SignIn);
		btnSignUp.setOnClickListener(this);
		btnLogIn.setOnClickListener(this);
		etUsername = (EditText) myView.findViewById(R.id.et_userName);
		etPassword = (EditText) myView.findViewById(R.id.et_userPassword);
		errormessage = (TextView) myView.findViewById(R.id.tv_error_message);
		preferences = Preferences.getPreferences();
		if (checkIfUserIsLoggedIn())
			goToUserList();
	}

	private boolean checkIfUserIsLoggedIn() {
		return preferences.isUserLogged();
	}

	private void goToUserList() {
		myView = null;
		MainActivity activity = (MainActivity) getActivity();
		TabsPagerAdapter adapter = activity.getAdapter();
		adapter.updateFragmentList();
		adapter.notifyDataSetChanged();
	}

	private void userSignUp() {
		if (checkIfUserIsLoggedIn()) {
			errormessage.setText("GOSPOZHA LOGIRAN SUM!");
			preferences.getCity();
			return;
		}
		try {
			new CreateUser(errormessage, username, password,
					preferences.getLatitude() + "", preferences.getLongtitude()
							+ "", preferences.getCity()).execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.goToUserList();

	}

	private void userSignIn() {
		new LogInUser(username, password).execute();
		this.goToUserList();
	}

	@Override
	public void onClick(View v) {
		username = etUsername.getText().toString();
		password = etPassword.getText().toString();
		switch (v.getId()) {
		case R.id.btn_SignUp:
			userSignUp();
			break;
		case R.id.btn_SignIn:
			userSignIn();
		}

	}
}
