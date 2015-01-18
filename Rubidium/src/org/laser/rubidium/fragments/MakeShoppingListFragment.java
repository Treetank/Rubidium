package org.laser.rubidium.fragments;

import java.util.ArrayList;
import java.util.List;

import org.laser.rubidium.MainActivity;
import org.laser.rubidium.R;
import org.laser.rubidium.contentprovider.RubidiumContentProvider;
import org.laser.rubidium.database.ShoppingListTable;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

			final Uri newUri = MakeShoppingListFragment.this.getActivity().getContentResolver()
					.insert(RubidiumContentProvider.CONTENT_URI, values);

			MakeShoppingListFragment.this.textItemName.setText("");
			MakeShoppingListFragment.this.textItemPrice.setText("");

			Toast.makeText(MakeShoppingListFragment.this.getActivity(), newUri.toString(), Toast.LENGTH_LONG).show();
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
}
