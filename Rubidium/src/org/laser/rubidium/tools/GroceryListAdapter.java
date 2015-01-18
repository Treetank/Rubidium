package org.laser.rubidium.tools;

import org.laser.rubidium.database.RubidiumDatabaseHelper;
import org.laser.rubidium.database.ShoppingListTable;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroceryListAdapter extends CursorAdapter {

	public GroceryListAdapter(final Context context, final Cursor c, final int flags) {
		super(context, c, flags);
	}

	@Override
	public void bindView(final View view, final Context context, final Cursor c) {
		final TextView content = (TextView) view.findViewById(android.R.id.text1);
		content.setText(c.getString(c.getColumnIndex(ShoppingListTable.COLUMN_ITEM_NAME)));
		if (c.getString(c.getColumnIndex(ShoppingListTable.COLUMN_PURCHASED)).equals(RubidiumDatabaseHelper.DB_TRUE)) {
			view.setBackgroundColor(Color.GREEN);
		} else {
			view.setBackgroundColor(Color.WHITE);
		}
	}

	@Override
	public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		return inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
	}

}
