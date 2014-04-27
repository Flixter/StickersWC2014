package mk.viktor.adapter;

import mk.viktor.fragments.FriendsList;
import mk.viktor.fragments.Settings;
import mk.viktor.fragments.StickersList;
import mk.viktor.fragments.UsersList;
import mk.viktor.helper.Preferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	StickersList stickerList;
	Settings settings;
	FriendsList friendList;
	Fragment fl;

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
		stickerList = new StickersList();
		settings = new Settings();
		updateFragmentList();
	}

	@Override
	public Fragment getItem(int tabIndex) {

		switch (tabIndex) {
		case 0:
			return stickerList;
		case 1:
			return settings;
		case 2:
			return fl;
		default:
			return null;
		}

	}

	public void updateFragmentList() {
		if (Preferences.getPreferences().isUserLogged())
			fl = new UsersList();
		else
			fl = new FriendsList();
	}

	@Override
	public int getCount() {
		return 3;
	}

}
