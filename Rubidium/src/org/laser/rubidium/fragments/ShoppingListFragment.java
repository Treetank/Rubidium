package org.laser.rubidium.fragments;

import java.util.ArrayList;
import java.util.List;

import org.laser.rubidium.MainActivity;
import org.laser.rubidium.R;
import org.laser.rubidium.contentprovider.RubidiumContentProvider;
import org.laser.rubidium.database.RubidiumDatabaseHelper;
import org.laser.rubidium.database.ShoppingListTable;
import org.laser.rubidium.database.objects.ShoppingListItem;
import org.laser.rubidium.tools.GroceryListAdapter;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class ShoppingListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>,
		ActionBar.OnNavigationListener {

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
			final ContentValues cv = new ContentValues();
			if (item.isPurchased()) {
				cv.put(ShoppingListTable.COLUMN_PURCHASED, RubidiumDatabaseHelper.DB_FALSE);
			} else {
				cv.put(ShoppingListTable.COLUMN_PURCHASED, RubidiumDatabaseHelper.DB_TRUE);
			}
			ShoppingListFragment.this.getActivity().getContentResolver()
					.update(Uri.parse(RubidiumContentProvider.CONTENT_URI + "/" + item.getId()), cv, null, null);
			return true;
		}
	};

	private GroceryListAdapter listAdapter;
	private SpinnerAdapter spinnerAdapter;

	public ShoppingListFragment() {
	}

	private void addNavBar() {
		final ActionBar actionbar = ((ActionBarActivity) this.getActivity()).getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		this.fillNavSpinner();
		actionbar.setListNavigationCallbacks(this.spinnerAdapter, this);
	}

	private void deletePurchasedItems() {
		final String where = ShoppingListTable.COLUMN_PURCHASED + " = ?";
		final String[] selectionArgs = { RubidiumDatabaseHelper.DB_TRUE };
		this.getActivity().getContentResolver().delete(RubidiumContentProvider.CONTENT_URI, where, selectionArgs);
	}

	private void fillData() {
		this.getLoaderManager().initLoader(0, null, this);
		this.listAdapter = new GroceryListAdapter(this.getActivity(), null, 0);

		this.setListAdapter(this.listAdapter);
	}

	private void fillNavSpinner() {
		final List<String> list = new ArrayList<String>();
		list.add("Walmart");
		list.add("Costco");

		this.spinnerAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.shopping_list_nav_spinner, list);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	@Override
	public Loader<Cursor> onCreateLoader(final int arg0, final Bundle arg1) {
		final String[] projection = ShoppingListTable.ALL_COLUMNS;
		final CursorLoader cl = new CursorLoader(this.getActivity(), RubidiumContentProvider.CONTENT_URI, projection,
				null, null, null);
		return cl;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.shopping_list, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.listener = null;
	}

	@Override
	public void onListItemClick(final ListView l, final View v, final int position, final long id) {
		final ShoppingListItem item = ShoppingListItem.newInstance((Cursor) this.getListAdapter().getItem(position));
		Toast.makeText(this.getActivity(), item.getItem() + " selected", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
		this.listAdapter.swapCursor(null);
	}

	@Override
	public void onLoadFinished(final Loader<Cursor> loader, final Cursor data) {
		this.listAdapter.swapCursor(data);
	}

	@Override
	public boolean onNavigationItemSelected(final int arg0, final long arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_clear_purchased:
			this.deletePurchasedItems();
			return true;
		case R.id.action_unmark_all:
			this.setAllItemsUnpurchased();
			return true;
		default:
			return false;
		}
	}

	@Override
	public void onPrepareOptionsMenu(final Menu menu) {
		final ActionBar actionbar = ((ActionBarActivity) this.getActivity()).getSupportActionBar();

		final boolean drawerOpen = ((DrawerLayout) this.getActivity().findViewById(R.id.drawer_layout))
				.isDrawerOpen(GravityCompat.START);
		menu.findItem(R.id.action_clear_purchased).setVisible(!drawerOpen);
		menu.findItem(R.id.action_unmark_all).setVisible(!drawerOpen);
		if (drawerOpen) {
			actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		} else {
			this.addNavBar();
		}
	}

	private void setAllItemsUnpurchased() {
		final ContentValues cv = new ContentValues();
		cv.put(ShoppingListTable.COLUMN_PURCHASED, RubidiumDatabaseHelper.DB_FALSE);
		this.getActivity().getContentResolver().update(RubidiumContentProvider.CONTENT_URI, cv, null, null);
	}
}
