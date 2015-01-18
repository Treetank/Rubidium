package org.laser.rubidium;

import org.laser.rubidium.fragments.MakeShoppingListFragment;
import org.laser.rubidium.fragments.MakeShoppingListFragment.MakeShoppingListFragmentListener;
import org.laser.rubidium.fragments.RubidiumFragment;
import org.laser.rubidium.fragments.ShoppingListFragment;
import org.laser.rubidium.fragments.ShoppingListFragment.ShoppingListFragmentListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
		ShoppingListFragmentListener, MakeShoppingListFragmentListener {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		this.mNavigationDrawerFragment = (NavigationDrawerFragment) this.getSupportFragmentManager().findFragmentById(
				R.id.navigation_drawer);
		this.mTitle = this.getTitle();

		// Set up the drawer.
		this.mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) this.findViewById(R.id.drawer_layout));
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		if (!this.mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			this.getMenuInflater().inflate(R.menu.main, menu);
			this.restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public void onNavigationDrawerItemSelected(final int position) {
		// update the main content by replacing fragments
		final FragmentManager fragmentManager = this.getSupportFragmentManager();
		final FragmentTransaction ft = fragmentManager.beginTransaction();
		Fragment newFrag;

		switch (position) {
		case 0:
			newFrag = ShoppingListFragment.newInstance();
			break;
		case 1:
			newFrag = MakeShoppingListFragment.newInstance();
			break;
		case 2:
			newFrag = RubidiumFragment.newInstance(3);
			break;
		default:
			newFrag = RubidiumFragment.newInstance(1);
			break;
		}
		ft.replace(R.id.container, newFrag);
		ft.commit();
	}

	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		final int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onSectionAttached(final int number) {
		switch (number) {
		case 1:
			this.mTitle = this.getString(R.string.title_section1);
			break;
		case 2:
			this.mTitle = this.getString(R.string.title_section2);
			break;
		case 3:
			this.mTitle = this.getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		final ActionBar actionBar = this.getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(this.mTitle);
	}

}
