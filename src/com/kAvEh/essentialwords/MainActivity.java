package com.kAvEh.essentialwords;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends FragmentActivity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	private ListView lv;
	
	private int lastScroll;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		String[] menu = new String[4];
		menu[0] = "Add Word";
		menu[1] = "Leitner";
		menu[2] = "Help";
		menu[3] = "About Us";
		int[] icons = new int[4];
		icons[0] = R.drawable.ic_action_add;
		icons[1] = R.drawable.ic_action_leitner;
		icons[2] = R.drawable.ic_action_help;
		icons[3] = R.drawable.ic_action_info;
		mDrawerList.setAdapter(new DrawerMenuAdapter(MainActivity.this, menu,
				icons));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_action_drawer, /*
										 * nav drawer image to replace 'Up'
										 * caret
										 */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// ListView ----------
		lv = (ListView) findViewById(R.id.lession_list);
		new ShowListTask().execute();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent(MainActivity.this, LessonActivity.class);
				i.putExtra("Num", position + 1);
				startActivity(i);
			}
		});

	}

	private void initialize() {
		new ShowListTask().execute();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		lastScroll = lv.getFirstVisiblePosition();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.action_bar_main, menu);
		// Associate searchable configuration with the SearchView
		// SearchManager searchManager = (SearchManager)
		// getSystemService(Context.SEARCH_SERVICE);
		// SearchView searchView = (SearchView)
		// menu.findItem(R.id.action_search)
		// .getActionView();
		// searchView.setSearchableInfo(searchManager
		// .getSearchableInfo(getComponentName()));
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return false;
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0: {
				AddNewWordFragment fr = new AddNewWordFragment();
				fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
				fr.show(getSupportFragmentManager(), "Hello");
				initialize();
			}
				break;

			case 1: {
				Intent i = new Intent(MainActivity.this, LeitnerActivity.class);
				startActivity(i);
			}
				break;
			case 2:

				break;
			case 3:

				break;
			default:
				break;
			}
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private class ShowListTask extends
			AsyncTask<Void, HashMap<String, Integer>, Void> {

		ListView lv;
		String[][] word_lists;
		int[][] word_data;
		LessonAdapter adapter;
		int lesson_num;

		@Override
		protected void onPostExecute(Void unused) {
			// stop the loading animation or something
		}

		@Override
		protected void onPreExecute() {
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			lesson_num = db.getLessonNum();
			db.close();
			lv = (ListView) findViewById(R.id.lession_list);
			word_lists = new String[lesson_num][3];
			word_data = new int[lesson_num][5];
			adapter = new LessonAdapter(MainActivity.this, word_lists,
					word_data, lesson_num);
			lv.setAdapter(adapter);
			if (lastScroll > 0) {
				lv.setSelectionFromTop(lastScroll, 0);
			}
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		protected Void doInBackground(Void... params) {
			DatabaseHandler db;
			ArrayList<Word> list;
			HashMap<String, Integer> temp;
			int[] leitner;
			for (int i = 0; i < lesson_num; i++) {
				db = new DatabaseHandler(getApplicationContext());
				list = db.getLesson(i + 1);
				db.close();
				temp = new HashMap<String, Integer>();
				leitner = new int[5];
				for (int j = 0; j < list.size(); j++) {
					switch (list.get(j).getLeitnerStage()) {
					case 1:
						leitner[0]++;
						break;
					case 2:
						leitner[1]++;
						break;
					case 3:
						leitner[2]++;
						break;
					case 4:
						leitner[3]++;
						break;
					case 5:
						leitner[4]++;
						break;
					default:
						break;
					}
				}
				if (list.size() > 0)
					word_lists[i][0] = list.get(0).getWord();
				if (list.size() > 1)
					word_lists[i][1] = list.get(1).getWord();
				else
					word_lists[i][1] = "-";
				if (list.size() > 2)
					word_lists[i][2] = list.get(2).getWord();
				else
					word_lists[i][2] = "-";
				adapter.setListData(word_lists);
				word_data[i][0] = leitner[0];
				word_data[i][1] = leitner[1];
				word_data[i][2] = leitner[2];
				word_data[i][3] = leitner[3];
				word_data[i][4] = leitner[4];
				adapter.setListNum(word_data);
				publishProgress(temp);
			}

			return (null);
		}

		@Override
		protected void onProgressUpdate(HashMap<String, Integer>... items) {
			adapter.notifyDataSetChanged();
		}
	}
}