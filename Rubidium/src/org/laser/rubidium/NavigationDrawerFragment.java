package org.laser.rubidium;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Fragment used for managing interactions for and presentation of a navigation
 * drawer. See the <a href=
 * "https://developer.android.com/design/patterns/navigation-drawer.html#Interaction"
 * > design guidelines</a> for a complete explanation of the behaviors
 * implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

	/**
	 * Callbacks interface that all activities using this fragment must
	 * implement.
	 */
	public static interface NavigationDrawerCallbacks {
		/**
		 * Called when an item in the navigation drawer is selected.
		 */
		void onNavigationDrawerItemSelected(int position);
	}

	/**
	 * Remember the position of the selected item.
	 */
	private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

	/**
	 * Per the design guidelines, you should show the drawer on launch until the
	 * user manually expands it. This shared preference tracks this.
	 */
	private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

	/**
	 * A pointer to the current callbacks instance (the Activity).
	 */
	private NavigationDrawerCallbacks mCallbacks;

	/**
	 * Helper component that ties the action bar to the navigation drawer.
	 */
	private ActionBarDrawerToggle mDrawerToggle;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerListView;

	private View mFragmentContainerView;
	private int mCurrentSelectedPosition = 0;
	private boolean mFromSavedInstanceState;

	private boolean mUserLearnedDrawer;

	public NavigationDrawerFragment() {
	}

	private ActionBar getActionBar() {
		return ((ActionBarActivity) this.getActivity()).getSupportActionBar();
	}

	public boolean isDrawerOpen() {
		return this.mDrawerLayout != null
				&& this.mDrawerLayout.isDrawerOpen(this.mFragmentContainerView);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Indicate that this fragment would like to influence the set of
		// actions in the action bar.
		this.setHasOptionsMenu(true);
	}

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		try {
			this.mCallbacks = (NavigationDrawerCallbacks) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(
					"Activity must implement NavigationDrawerCallbacks.");
		}
	}

	@Override
	public void onConfigurationChanged(final Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Forward the new configuration the drawer toggle component.
		this.mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Read in the flag indicating whether or not the user has demonstrated
		// awareness of the
		// drawer. See PREF_USER_LEARNED_DRAWER for details.
		final SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this.getActivity());
		this.mUserLearnedDrawer = sp
				.getBoolean(PREF_USER_LEARNED_DRAWER, false);

		if (savedInstanceState != null) {
			this.mCurrentSelectedPosition = savedInstanceState
					.getInt(STATE_SELECTED_POSITION);
			this.mFromSavedInstanceState = true;
		}

		// Select either the default item (0) or the last selected item.
		this.selectItem(this.mCurrentSelectedPosition);
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		// If the drawer is open, show the global app actions in the action bar.
		// See also
		// showGlobalContextActionBar, which controls the top-left area of the
		// action bar.
		if (this.mDrawerLayout != null && this.isDrawerOpen()) {
			inflater.inflate(R.menu.global, menu);
			this.showGlobalContextActionBar();
		}
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(final LayoutInflater inflater,
			final ViewGroup container, final Bundle savedInstanceState) {
		this.mDrawerListView = (ListView) inflater.inflate(
				R.layout.fragment_navigation_drawer, container, false);
		this.mDrawerListView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(final AdapterView<?> parent,
							final View view, final int position, final long id) {
						NavigationDrawerFragment.this.selectItem(position);
					}
				});
		this.mDrawerListView.setAdapter(new ArrayAdapter<String>(this
				.getActionBar().getThemedContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				new String[] { this.getString(R.string.title_section1),
						this.getString(R.string.title_section2),
						this.getString(R.string.title_section3) }));
		this.mDrawerListView
				.setItemChecked(this.mCurrentSelectedPosition, true);
		return this.mDrawerListView;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		this.mCallbacks = null;
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (this.mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		if (item.getItemId() == R.id.action_example) {
			Toast.makeText(this.getActivity(), "Example action.",
					Toast.LENGTH_SHORT).show();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(final Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_SELECTED_POSITION, this.mCurrentSelectedPosition);
	}

	private void selectItem(final int position) {
		this.mCurrentSelectedPosition = position;
		if (this.mDrawerListView != null) {
			this.mDrawerListView.setItemChecked(position, true);
		}
		if (this.mDrawerLayout != null) {
			this.mDrawerLayout.closeDrawer(this.mFragmentContainerView);
		}
		if (this.mCallbacks != null) {
			this.mCallbacks.onNavigationDrawerItemSelected(position);
		}
	}

	/**
	 * Users of this fragment must call this method to set up the navigation
	 * drawer interactions.
	 * 
	 * @param fragmentId
	 *            The android:id of this fragment in its activity's layout.
	 * @param drawerLayout
	 *            The DrawerLayout containing this fragment's UI.
	 */
	public void setUp(final int fragmentId, final DrawerLayout drawerLayout) {
		this.mFragmentContainerView = this.getActivity().findViewById(
				fragmentId);
		this.mDrawerLayout = drawerLayout;

		// set a custom shadow that overlays the main content when the drawer
		// opens
		this.mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener

		final ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the navigation drawer and the action bar app icon.
		this.mDrawerToggle = new ActionBarDrawerToggle(this.getActivity(), /*
																			 * host
																			 * Activity
																			 */
		this.mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.navigation_drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
		R.string.navigation_drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			@Override
			public void onDrawerClosed(final View drawerView) {
				super.onDrawerClosed(drawerView);
				if (!NavigationDrawerFragment.this.isAdded()) {
					return;
				}

				NavigationDrawerFragment.this.getActivity()
						.supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(final View drawerView) {
				super.onDrawerOpened(drawerView);
				if (!NavigationDrawerFragment.this.isAdded()) {
					return;
				}

				if (!NavigationDrawerFragment.this.mUserLearnedDrawer) {
					// The user manually opened the drawer; store this flag to
					// prevent auto-showing
					// the navigation drawer automatically in the future.
					NavigationDrawerFragment.this.mUserLearnedDrawer = true;
					final SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(NavigationDrawerFragment.this
									.getActivity());
					sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
							.commit();
				}

				NavigationDrawerFragment.this.getActivity()
						.supportInvalidateOptionsMenu(); // calls
				// onPrepareOptionsMenu()
			}
		};

		// If the user hasn't 'learned' about the drawer, open it to introduce
		// them to the drawer,
		// per the navigation drawer design guidelines.
		if (!this.mUserLearnedDrawer && !this.mFromSavedInstanceState) {
			this.mDrawerLayout.openDrawer(this.mFragmentContainerView);
		}

		// Defer code dependent on restoration of previous instance state.
		this.mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				NavigationDrawerFragment.this.mDrawerToggle.syncState();
			}
		});

		this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
	}

	/**
	 * Per the navigation drawer design guidelines, updates the action bar to
	 * show the global app 'context', rather than just what's in the current
	 * screen.
	 */
	private void showGlobalContextActionBar() {
		final ActionBar actionBar = this.getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setTitle(R.string.app_name);
	}
}
