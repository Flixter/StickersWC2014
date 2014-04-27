package mk.viktor.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import mk.viktor.helper.Sticker;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {

	StickersDatabaseHelper dbHelper;
	Context c;
	private static final int stickersNumber = 640;
	 
	
	public Database(Context c) {
		dbHelper = new StickersDatabaseHelper(c);
		this.c = c;
	}

	private boolean checkIfDbExist() {
		File database = c.getDatabasePath(StickersDatabaseHelper.DATABASE_NAME);
		return database.exists();
	}
	
	
	public void updateQuantity(int position,int quantity){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(StickersDatabaseHelper.STICKERS_QUANTITY, quantity);
		String whereClause = StickersDatabaseHelper.STICKERS_NUMBER + " = " + position;
		database.update(StickersDatabaseHelper.STICKERS_TABLE, contentValues, whereClause, null);
		database.close();
		
	}

	private  List<Sticker> insertInDatabase() {
		List<Sticker> stickers = new ArrayList<Sticker>(stickersNumber);
		int resId = mk.viktor.stickers.R.raw.stickers;
		
		InputStream inputStream = c.getResources().openRawResource(resId);
		
		InputStreamReader inputReader = new InputStreamReader(inputStream);
		BufferedReader bufferReader = new BufferedReader(inputReader);
		

		
		for (int i = 0; i < stickersNumber; i++) {
			int number = i;
			String name = "";
			try {
				name = bufferReader.readLine();
			} catch (IOException e) {
				System.out.println("Failed to read Buffered Reader with exception : " + e.toString());
			}
			String country = "Brasil";
			int quantity = 0;
			Sticker sticker =  new Sticker(number, name, quantity, country);
			stickers.add(sticker);
			insertSticker(sticker);
			
		}
		
		return stickers;
	}

	private void insertSticker(Sticker sticker){
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(StickersDatabaseHelper.STICKERS_NUMBER, sticker.getNumber());
		contentValues.put(StickersDatabaseHelper.STICKERS_NAME, sticker.getName());
		contentValues.put(StickersDatabaseHelper.STICKERS_COUNTRY, sticker.getCountry());
		contentValues.put(StickersDatabaseHelper.STICKERS_QUANTITY, sticker.getQuantity());
		
		database.insert(StickersDatabaseHelper.STICKERS_TABLE, null, contentValues);
		database.close();
		
	}
	
	public List<Sticker> getStickers() {
		if(!checkIfDbExist()){
			return insertInDatabase();
		}else{
			return getData();
		}
	};
	
	private List<Sticker> getData() {
		List<Sticker> stickers = new ArrayList<Sticker>(stickersNumber);

		// Open the Database
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		String[] columns = { StickersDatabaseHelper.STICKERS_NUMBER,
				StickersDatabaseHelper.STICKERS_NAME,
				StickersDatabaseHelper.STICKERS_COUNTRY,
				StickersDatabaseHelper.STICKERS_QUANTITY };
		
		String orderBy = String.format("%s %s", StickersDatabaseHelper.STICKERS_NUMBER, "ASC");
		
		Cursor cursor = db.query(StickersDatabaseHelper.STICKERS_TABLE, columns, null, null, null, null, orderBy);
		
		while(cursor.moveToNext()){
			
			int number = cursor.getInt(0);
			String name = cursor.getString(1);
			String country = cursor.getString(2);
			int quantity = cursor.getInt(3);
			
			stickers.add(new Sticker(number, name, quantity, country));
		}
		db.close();
		
		return stickers;
	}

	public static class StickersDatabaseHelper extends SQLiteOpenHelper {

		// Databse information
		private static final String DATABASE_NAME = "stickers.db";
		private static final int DATABASE_VERSION = 1;

		// <---- TABLES ----->
		// First Table
		private static final String STICKERS_TABLE = "stickers";

		public static final String STICKERS_NUMBER = "stickers_number";
		public static final String STICKERS_NAME = "stickers_name";
		public static final String STICKERS_QUANTITY = "stickers_quantity";
		public static final String STICKERS_COUNTRY = "stickers_country";

		// Query za kreiranje na baza
		private static final String CREATE_STICKERS_TABLE = createStickersTableQuery();

		public StickersDatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_STICKERS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + STICKERS_TABLE);
			onCreate(db);
		}

		private static String createStickersTableQuery() {
			StringBuilder sb = new StringBuilder();

			sb.append("create table ");
			sb.append(STICKERS_TABLE);
			sb.append(" (");
			sb.append(STICKERS_NUMBER);
			sb.append(" integer primary key, ");
			sb.append(STICKERS_NAME);
			sb.append(" TEXT, ");
			sb.append(STICKERS_COUNTRY);
			sb.append(" TEXT, ");
			sb.append(STICKERS_QUANTITY);
			sb.append(" integer");
			sb.append(");");
			return sb.toString();
		}

	}
}
