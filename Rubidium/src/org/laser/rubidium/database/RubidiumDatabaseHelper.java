package org.laser.rubidium.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RubidiumDatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "rubidium.db";
	private static final int DATABASE_VERSION = 5;

	public RubidiumDatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(final SQLiteDatabase database) {
		ShoppingListTable.onCreate(database);
		// Othertable.onCreate(database);
	}

	@Override
	public void onUpgrade(final SQLiteDatabase database, final int oldVersion, final int newVersion) {
		ShoppingListTable.onUpgrade(database, oldVersion, newVersion);
		// Othertable.onUpgrade(database, oldVersion, newVersion);
	}
}
