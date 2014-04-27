package mk.viktor.helper;

import android.graphics.drawable.Drawable;

public class Sticker {

	private int number;
	private String name;
	private String country;
	private Drawable image;
	private int color;
	private boolean isOwned;
	private int quantity;

	public Sticker(int number, String title,int quantity,String country) {
		this.number = number;
		this.name = title;
		this.country = country;
		this.quantity = quantity;
		updateOwned();
	}

	private void updateOwned() {
		this.isOwned = quantity > 0 ? true : false;
	}

	public int getNumber() {
		return number;
	}

	public String getCountry() {
		return country;
	}
	
	public String getName() {
		return name;
	}

	public Drawable getImage() {
		return image;
	}


	public int getColor() {
		return color;
	}

	public boolean isOwned() {
		return isOwned;
	}

	public int getQuantity() {
		return quantity;
	}

	public void increaseQuantity() {
		this.quantity++;
		updateOwned();
	}
	
	public void decreaseQuantity() {
		if(this.quantity >0)
			this.quantity--;
		updateOwned();
	
	}

}
