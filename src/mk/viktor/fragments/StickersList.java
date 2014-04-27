package mk.viktor.fragments;

import java.util.List;

import mk.viktor.adapter.GridViewAdapter;
import mk.viktor.helper.Sticker;
import mk.viktor.model.Database;
import mk.viktor.stickers.MainActivity;
import mk.viktor.stickers.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class StickersList extends Fragment implements OnItemClickListener,
		OnClickListener, DialogInterface.OnClickListener {

	private View myView;
	GridView stickersGridView;
	public static List<Sticker> stickers;
	GridViewAdapter adapter;
	Sticker currentSticker;

	// HeaderView
	TextView nameTV;
	TextView quantityTv;
	Button increaseBtn;
	Button decreaseBtn;
	Database db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		myView = inflater.inflate(R.layout.sticker_list, container, false);
		

		initialize(inflater);
		return myView;
	}

	private void initialize(LayoutInflater inflater) {
		db = new Database(getActivity().getApplicationContext());
		stickers = MainActivity.stickers;
		currentSticker = stickers.get(0);
		adapter = new GridViewAdapter(inflater, getActivity());
		stickersGridView = (GridView) myView
				.findViewById(R.id.stickersGridView);
		stickersGridView.setAdapter(adapter);
		stickersGridView.setOnItemClickListener(this);

		// Set up View Controllers
		nameTV = (TextView) myView.findViewById(R.id.stickerTitleTV);
		quantityTv = (TextView) myView.findViewById(R.id.quantityTV);
		increaseBtn = (Button) myView.findViewById(R.id.increaseQuantityBtn);
		decreaseBtn = (Button) myView.findViewById(R.id.decreaseQuantityBtn);
		increaseBtn.setOnClickListener(this);
		decreaseBtn.setOnClickListener(this);
		update();
	}

	private void increaseQuantity() {
		currentSticker.increaseQuantity();
		update();
	}

	private void decreaseQuantity() {
		currentSticker.decreaseQuantity();
		update();
	}

	private void stickerClicked(int position) {
		currentSticker = adapter.filteredStickers.get(position);
		
		String positive = "Yes"; 
		String negative = "No";
		if(currentSticker.isOwned()){
			positive = "Add duplicate";
			if(currentSticker.getQuantity() > 1)
				negative = "Remove duplicate";
			else
				negative = "Remove sticker";
		}
		showPopup(positive, negative);
		
	}

	private void showPopup(String positive, String negative) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(
				"Are you sure you want to add \"" + currentSticker.getName()
						+ "\" ( " + currentSticker.getNumber() + " ) ?")
				.setPositiveButton(positive, this)
				.setNegativeButton(negative, this).show();

	}

	public void updateFilter(){
		adapter.notifyDataSetChanged();
	}
	
	public void update() {
		adapter.notifyDataSetChanged();
		db.updateQuantity(currentSticker.getNumber(),
				currentSticker.getQuantity());
		nameTV.setText(currentSticker.getName());
		quantityTv.setText(currentSticker.getQuantity() + "");
		
		Settings.update();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		ScaleAnimation scale = new ScaleAnimation((float) 1.0, (float) 1.5,
				(float) 1.0, (float) 1.5, Animation.RELATIVE_TO_SELF,
				(float) 0.5, Animation.RELATIVE_TO_SELF, (float) 0.5);
		scale.setFillAfter(false);
		scale.setDuration(220);
		view.setAnimation(scale);
		stickerClicked(position);
		increaseQuantity();
		update();
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.increaseQuantityBtn:
			this.increaseQuantity();
			break;
		case R.id.decreaseQuantityBtn:
			this.decreaseQuantity();
			break;
		}

	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:
			increaseQuantity();
			update();
			break;
		case DialogInterface.BUTTON_NEGATIVE:
			decreaseQuantity();
			update();
			break;
		}

	}

}
