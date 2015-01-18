package org.laser.rubidium.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ShoppingListTable {

	public static final String TABLE_NAME = "shoppinglist";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ITEM_NAME = "item";
	public static final String COLUMN_STORE_NAME = "store";
	public static final String COLUMN_PRICE = "price";

	public static final String[] ALL_COLUMNS = { COLUMN_ID, COLUMN_ITEM_NAME, COLUMN_STORE_NAME, COLUMN_PRICE };

	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NAME + "( " + COLUMN_ITEM_NAME
			+ " TEXT NOT NULL, " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_STORE_NAME
			+ " TEXT NOT NULL," + COLUMN_PRICE + " REAL NOT NULL" + ");";

	public static void onCreate(final SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
		Log.w(ShoppingListTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}
}
