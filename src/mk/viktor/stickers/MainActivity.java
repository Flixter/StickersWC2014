package mk.viktor.stickers;

import java.util.List;

import mk.viktor.adapter.TabsPagerAdapter;
import mk.viktor.fragments.StickersList;
import mk.viktor.helper.Preferences;
import mk.viktor.helper.Sticker;
import mk.viktor.model.Database;
import mk.viktor.stickers.R;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuInflater;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	// Variable Names
	private ViewPager viewPager;
	private TabsPagerAdapter tabsAdapter;
	private int[] tabImages = new int[] { R.drawable.stickerslist,
			R.drawable.statistic, R.drawable.listfriends };

	public static List<Sticker> stickers;
	public static int filter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		initialize();

		// get the data
		Database db = new Database(getApplicationContext());

		stickers = db.getStickers();
	}

	public TabsPagerAdapter getAdapter(){
		return tabsAdapter;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_menu, menu);
		return true;
	}

	public void goToFirstTabWithFilter(int filter) {
		viewPager.setCurrentItem(0);
		getActionBar().setSelectedNavigationItem(0);
		StickersList stickerList = (StickersList) tabsAdapter.getItem(0);
		MainActivity.filter = filter;
		stickerList.updateFilter();

	}

	private void initialize() {
		//Initialize the preference singleton!
		Preferences.getInstance(getApplicationContext());
		viewPager = (ViewPager) findViewById(R.id.viewPager);

		tabsAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(tabsAdapter);

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



		for (int i = 0; i < 3; i++) {
			Drawable tabIcon = getTabIcon(i);
			actionBar.addTab(actionBar.newTab().setIcon(tabIcon)
					.setTabListener(this));
		}

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				getActionBar().setSelectedNavigationItem(index);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	public Drawable getTabIcon(int position) {
		return getResources().getDrawable(tabImages[position]);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(tab.getPosition());
		getActionBar().setSelectedNavigationItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

}
