package mk.viktor.fragments;

import mk.viktor.helper.Sticker;
import mk.viktor.stickers.MainActivity;
import mk.viktor.stickers.R;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Settings extends Fragment implements OnClickListener {

	private View myView;
	LinearLayout filterAll;
	LinearLayout filterOwned;
	LinearLayout filterMissing;
	LinearLayout filterDuplicate;

	static TextView ownedStickers;
	static TextView allStickers;
	static TextView missingStickers;
	static TextView duplicateStickers;
	private TextView sendStickers;

	static boolean isInitialized = false;
	static String missingStickersSMSText;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.settings, container, false);
		initializeComponenets();
		return myView;
	}

	private void initializeComponenets() {
		filterAll = (LinearLayout) myView.findViewById(R.id.filterTotal);
		filterOwned = (LinearLayout) myView.findViewById(R.id.filterOwned);
		filterMissing = (LinearLayout) myView.findViewById(R.id.filterMissing);
		filterDuplicate = (LinearLayout) myView
				.findViewById(R.id.filterDuplicate);

		ownedStickers = (TextView) myView.findViewById(R.id.ownedStickersTv);
		missingStickers = (TextView) myView
				.findViewById(R.id.missingStickersTv);
		duplicateStickers = (TextView) myView
				.findViewById(R.id.duplicateStickersTv);
		sendStickers = (TextView) myView
				.findViewById(R.id.tv_send_stickers_withsms);

		// Set Click Listeners
		filterAll.setOnClickListener(this);
		filterOwned.setOnClickListener(this);
		filterMissing.setOnClickListener(this);
		filterDuplicate.setOnClickListener(this);
		sendStickers.setOnClickListener(this);
		isInitialized = true;
		update();
	}

	public void updateFilter(int filter) {
		MainActivity a = (MainActivity) getActivity();
		a.goToFirstTabWithFilter(filter);
	}

	public static void update() {
		if (!isInitialized)
			return;
		int ownedStickersCount = 0;
		int missingStickersCount = 0;
		int duplicateStickersCount = 0;
		StringBuilder sb = new StringBuilder("Missing Stickers: \n");

		for (int i = 0; i < MainActivity.stickers.size(); i++) {
			Sticker s = MainActivity.stickers.get(i);

			if (s.isOwned()) {
				ownedStickersCount++;
				duplicateStickersCount += s.getQuantity() - 1;
			} else {
				missingStickersCount++;
				sb.append(s.getNumber() + ", ");
			}
		}

		ownedStickers.setText(" ( " + ownedStickersCount + " ) ");
		missingStickers.setText(" ( " + missingStickersCount + " ) ");
		duplicateStickers.setText(" ( " + duplicateStickersCount + " ) ");
		missingStickersSMSText = sb.toString();
	}

	private void sendSms() {
		String smsSender = "078323924";
		PendingIntent pi = PendingIntent.getActivity(getActivity()
				.getApplicationContext(), 0, new Intent(getActivity()
				.getApplicationContext(), MainActivity.class), 0);
		SmsManager smsManager = SmsManager.getDefault();

		for (int i = 0; i < missingStickersSMSText.length(); i+=150){
			int stickerTo = i+150;
			if(i+150 > missingStickersSMSText.length())
				stickerTo = missingStickersSMSText.length();
			
			smsManager.sendTextMessage(
					smsSender,
					null,
					missingStickersSMSText.substring(i,stickerTo
							), pi, null);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.filterTotal:
			updateFilter(0);
			break;
		case R.id.filterOwned:
			updateFilter(1);
			break;
		case R.id.filterMissing:
			updateFilter(2);
			break;
		case R.id.filterDuplicate:
			updateFilter(3);
			break;
		case R.id.tv_send_stickers_withsms:
			sendSms();
			break;
		}

	}
}
