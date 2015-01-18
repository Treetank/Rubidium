package org.laser.rubidium.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import org.laser.rubidium.database.RubidiumDatabaseHelper;
import org.laser.rubidium.database.ShoppingListTable;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class RubidiumContentProvider extends ContentProvider {

	private RubidiumDatabaseHelper database;

	private static final int SHOPPINGLIST = 10;
	private static final int SHOPPINGLIST_ID = 20;

	private static final String AUTHORITY = "org.laser.rubidium.contentprovider";

	private static final String BASE_PATH = "rubidium";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/rubidium";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/rubidium";

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, SHOPPINGLIST);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", SHOPPINGLIST_ID);
	}

	private void checkColumns(final String[] projection) {
		final String[] available = ShoppingListTable.ALL_COLUMNS;
		if (projection != null) {
			final HashSet<String> rc = new HashSet<String>(Arrays.asList(projection));
			final HashSet<String> ac = new HashSet<String>(Arrays.asList(available));

			if (!ac.containsAll(rc)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

	@Override
	public int delete(final Uri uri, final String selection, final String[] selectionArgs) {
		final int uriType = sURIMatcher.match(uri);
		final SQLiteDatabase db = this.database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case SHOPPINGLIST:
			rowsDeleted = db.delete(ShoppingListTable.TABLE_NAME, selection, selectionArgs);
			break;
		case SHOPPINGLIST_ID:
			final String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = db.delete(ShoppingListTable.TABLE_NAME, ShoppingListTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = db.delete(ShoppingListTable.TABLE_NAME, ShoppingListTable.COLUMN_ID + "=" + id + " and "
						+ selection, null);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		this.getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(final Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(final Uri uri, final ContentValues values) {
		final int uriType = sURIMatcher.match(uri);
		final SQLiteDatabase db = this.database.getWritableDatabase();
		long id = 0;
		switch (uriType) {
		case SHOPPINGLIST:
			id = db.insert(ShoppingListTable.TABLE_NAME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		this.getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		this.database = new RubidiumDatabaseHelper(this.getContext());
		return false;
	}

	@Override
	public Cursor query(final Uri uri, final String[] projection, final String selection, final String[] selectionArgs,
			final String sortOrder) {
		final SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(ShoppingListTable.TABLE_NAME);

		this.checkColumns(projection);

		final int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case SHOPPINGLIST:
			break;
		case SHOPPINGLIST_ID:
			qb.appendWhere(ShoppingListTable.COLUMN_ID + "=" + uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		final SQLiteDatabase db = this.database.getReadableDatabase();
		final Cursor cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(this.getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(final Uri uri, final ContentValues values, final String selection, final String[] selectionArgs) {
		final int uriType = sURIMatcher.match(uri);
		final SQLiteDatabase db = this.database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case SHOPPINGLIST:
			rowsUpdated = db.update(ShoppingListTable.TABLE_NAME, values, selection, selectionArgs);
			break;
		case SHOPPINGLIST_ID:
			final String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = db.update(ShoppingListTable.TABLE_NAME, values, ShoppingListTable.COLUMN_ID + "=" + id,
						selectionArgs);
			} else {
				rowsUpdated = db.update(ShoppingListTable.TABLE_NAME, values, ShoppingListTable.COLUMN_ID + "=" + id
						+ " and " + selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		this.getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
