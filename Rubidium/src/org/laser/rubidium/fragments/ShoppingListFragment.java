package org.laser.rubidium.fragments;

import org.laser.rubidium.MainActivity;
import org.laser.rubidium.contentprovider.RubidiumContentProvider;
import org.laser.rubidium.database.ShoppingListTable;
import org.laser.rubidium.database.objects.ShoppingListItem;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ShoppingListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

	public interface ShoppingListFragmentListener {
	}

	private static final String TAG = ShoppingListFragment.class.getName();

	public static ShoppingListFragment newInstance() {
		final ShoppingListFragment fragment = new ShoppingListFragment();
		return fragment;
	}

	private ShoppingListFragmentListener listener;

	private final OnItemLongClickListener editItemListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
			final ShoppingListItem item = ShoppingListItem.newInstance((Cursor) ShoppingListFragment.this
					.getListAdapter().getItem(position));
			Toast.makeText(ShoppingListFragment.this.getActivity(), item.getItem() + " selected long click",
					Toast.LENGTH_LONG).show();
			return true;
		}
	};

	private SimpleCursorAdapter adapter;

	public ShoppingListFragment() {
	}

	private void fillData() {
		final String[] from = new String[] { ShoppingListTable.COLUMN_ITEM_NAME };
		final int[] to = new int[] { android.R.id.text1 };

		this.getLoaderManager().initLoader(0, null, this);
		this.adapter = new SimpleCursorAdapter(this.getActivity(), android.R.layout.simple_list_item_1, null, from, to,
				0);

		this.setListAdapter(this.adapter);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/*
		 * final String[] values = new String[] { "one", "two", "three", "four" }; final ArrayAdapter<String> adapter = new
		 * ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, values); this.setListAdapter(adapter);
		 */
		this.fillData();
		this.getListView().setOnItemLongClickListener(this.editItemListener);
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		if (activity instanceof ShoppingListFragmentListener) {
			this.listener = (ShoppingListFragmentListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement ShoppingListFragment.ShoppingListFragmentListener");
		}
		((MainActivity) activity).onSectionAttached(1);

	}

	@Override
	public Loader<Cursor> onCreateLoader(final int arg0, final Bundle arg1) {
		final String[] projection = ShoppingListTable.ALL_COLUMNS;
		final CursorLoader cl = new CursorLoader(this.getActivity(), RubidiumContentProvider.CONTENT_URI, projection,
				null, null, null);
		return cl;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.listener = null;
	}

	@Override
	public void onListItemClick(final ListView l, final View v, final int position, final long id) {
		final ShoppingListItem item = ShoppingListItem.newInstance((Cursor) this.getListAdapter().getItem(position));
		Toast.makeText(this.getActivity(), item.getItem() + " selected", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
		this.adapter.swapCursor(null);
	}

	@Override
	public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
		this.adapter.swapCursor(data);
	}
}
