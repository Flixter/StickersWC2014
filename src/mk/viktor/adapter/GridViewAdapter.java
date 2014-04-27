package mk.viktor.adapter;

import java.util.ArrayList;
import java.util.List;

import mk.viktor.fragments.StickersList;
import mk.viktor.helper.Sticker;
import mk.viktor.stickers.MainActivity;
import mk.viktor.stickers.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	private int filter;
	private static LayoutInflater layoutInflater;
	private Context context;
	public List<Sticker> filteredStickers;

	public GridViewAdapter(LayoutInflater inflater, Context context) {
		layoutInflater = inflater;
		this.context = context;
		filteredStickers = new ArrayList<Sticker>();
		filteredStickers = StickersList.stickers;
	}

	@Override
	public int getCount() {
		update();
		return filteredStickers.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Log.v("Data", "GET VIEW !");
		View view = convertView;

		if (convertView == null)
			view = layoutInflater.inflate(R.layout.sticker, null);

		ImageView stickerImage = (ImageView) view
				.findViewById(R.id.stickerImage);
		TextView stickerNumber = (TextView) view
				.findViewById(R.id.stickerNumber);

		Sticker sticker = filteredStickers.get(position);

		stickerNumber.setText(sticker.getNumber() + " ");
		stickerImage.setImageDrawable(getImageDrawable(sticker));
		if (sticker.isOwned())
			view.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.border_selected));
		else
			view.setBackgroundDrawable(context.getResources().getDrawable(
					R.drawable.border));
		return view;
	}

	private Drawable getImageDrawable(Sticker sticker) {
		if (sticker.isOwned())
			return context.getResources().getDrawable(
					R.drawable.brasilcolourflag);
		else
			return context.getResources()
					.getDrawable(R.drawable.brasilgrayflag);
	}

	public void update() {
		filter = MainActivity.filter;
		switch (filter) {
		case 0:
			filteredStickers = StickersList.stickers;
			break;
		case 1:
			filteredStickers = getOwned();
			break;
		case 2:
			filteredStickers = getMissing();
			break;
		case 3:
			filteredStickers = getDuplicates();
			break;

		}

	}

	private List<Sticker> getMissing() {
		List<Sticker> returnList = new ArrayList<Sticker>();
		for (int i = 0; i < StickersList.stickers.size(); i++) {
			Sticker s = StickersList.stickers.get(i);
			if(!s.isOwned())
				returnList.add(s);
		}
	return returnList;
	}

	private List<Sticker> getDuplicates() {
		List<Sticker> returnList = new ArrayList<Sticker>();
		for (int i = 0; i < StickersList.stickers.size(); i++) {
			Sticker s = StickersList.stickers.get(i);
			if(s.getQuantity() > 1)
				returnList.add(s);
		}
	return returnList;
	}
	
	private List<Sticker> getOwned() {
		List<Sticker> returnList = new ArrayList<Sticker>();
		for (int i = 0; i < StickersList.stickers.size(); i++) {
			Sticker s = StickersList.stickers.get(i);
			if(s.isOwned())
				returnList.add(s);
		}
	return returnList;
	}

}
