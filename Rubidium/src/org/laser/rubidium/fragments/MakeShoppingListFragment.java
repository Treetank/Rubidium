package org.laser.rubidium.fragments;

import java.util.ArrayList;
import java.util.List;

import org.laser.rubidium.MainActivity;
import org.laser.rubidium.R;
import org.laser.rubidium.contentprovider.RubidiumContentProvider;
import org.laser.rubidium.database.RubidiumDatabaseHelper;
import org.laser.rubidium.database.ShoppingListTable;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MakeShoppingListFragment extends Fragment {

	public interface MakeShoppingListFragmentListener {
	}

	public static MakeShoppingListFragment newInstance() {
		final MakeShoppingListFragment fragment = new MakeShoppingListFragment();
		return fragment;
	}

	private MakeShoppingListFragmentListener listener;

	private EditText textItemName, textItemPrice;
	private Spinner spinStoreName;
	private Button btnSubmit;

	private final OnClickListener buttonClick = new OnClickListener() {

		@Override
		public void onClick(final View v) {
			final ContentValues values = new ContentValues();

			values.put(ShoppingListTable.COLUMN_ITEM_NAME, MakeShoppingListFragment.this.textItemName.getText()
					.toString());
			values.put(ShoppingListTable.COLUMN_PRICE, MakeShoppingListFragment.this.textItemPrice.getText().toString());
			values.put(ShoppingListTable.COLUMN_STORE_NAME, MakeShoppingListFragment.this.spinStoreName
					.getSelectedItem().toString());
			values.put(ShoppingListTable.COLUMN_PURCHASED, RubidiumDatabaseHelper.DB_FALSE);

			final Uri newUri = MakeShoppingListFragment.this.getActivity().getContentResolver()
					.insert(RubidiumContentProvider.CONTENT_URI, values);

			MakeShoppingListFragment.this.textItemName.setText("");
			MakeShoppingListFragment.this.textItemPrice.setText("");

			MakeShoppingListFragment.this.textItemName.requestFocus();
		}

	};

	public MakeShoppingListFragment() {

	}

	private void linkUI(final View view) {
		this.textItemName = (EditText) view.findViewById(R.id.itemName);
		this.textItemPrice = (EditText) view.findViewById(R.id.itemPrice);
		this.spinStoreName = (Spinner) view.findViewById(R.id.storeSpinner);
		this.btnSubmit = (Button) view.findViewById(R.id.submitButton);

		this.btnSubmit.setOnClickListener(this.buttonClick);

		this.populateStoreSpinner();
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		if (activity instanceof MakeShoppingListFragmentListener) {
			this.listener = (MakeShoppingListFragmentListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement MakeShoppingListFragment.MakeShoppingListFragmentListener");
		}
		((MainActivity) activity).onSectionAttached(2);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		inflater.inflate(R.menu.make_shopping_list, menu);
		this.restoreActionBar();
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_make_shopping_list, container, false);
		this.linkUI(view);
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.listener = null;
	}

	@Override
	public void onPrepareOptionsMenu(final Menu menu) {
		final boolean drawerOpen = ((DrawerLayout) this.getActivity().findViewById(R.id.drawer_layout))
				.isDrawerOpen(GravityCompat.START);
		menu.findItem(R.id.action_clear_info).setVisible(!drawerOpen);
		menu.findItem(R.id.action_add_new_store).setVisible(!drawerOpen);
	}

	private void populateStoreSpinner() {
		final List<String> list = new ArrayList<String>();
		list.add("Walmart");
		list.add("Costco");
		list.add("FreshCo");
		list.add("Giant Tiger");
		list.add("Dollarama");

		final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
				android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		this.spinStoreName.setAdapter(dataAdapter);

		this.spinStoreName.setSelection(0);
	}

	private void restoreActionBar() {
		final ActionBar actionbar = ((ActionBarActivity) this.getActivity()).getSupportActionBar();
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	}
}
