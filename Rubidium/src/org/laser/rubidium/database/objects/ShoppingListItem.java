package org.laser.rubidium.database.objects;

import org.laser.rubidium.database.ShoppingListTable;

import android.database.Cursor;

public class ShoppingListItem {

	public static ShoppingListItem newInstance(final Cursor c) {
		final ShoppingListItem retVal = new ShoppingListItem();
		retVal.setId(c.getInt(c.getColumnIndex(ShoppingListTable.COLUMN_ID)));
		retVal.setItem(c.getString(c.getColumnIndex(ShoppingListTable.COLUMN_ITEM_NAME)));
		retVal.setPrice(c.getString(c.getColumnIndex(ShoppingListTable.COLUMN_PRICE)));
		retVal.setStore(c.getString(c.getColumnIndex(ShoppingListTable.COLUMN_STORE_NAME)));
		return retVal;
	}

	private int id;
	private String item;
	private String price;
	private String store;

	public int getId() {
		return this.id;
	}

	public String getItem() {
		return this.item;
	}

	public String getPrice() {
		return this.price;
	}

	public String getStore() {
		return this.store;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setItem(final String item) {
		this.item = item;
	}

	public void setPrice(final String price) {
		this.price = price;
	}

	public void setStore(final String store) {
		this.store = store;
	}
}
