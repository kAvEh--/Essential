package com.kAvEh.essentialwords;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;

	private ListView lv;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.lessions_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_navigation_drawer, /*
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
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// selectItem(0);
		}
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
		// DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		// ArrayList<Word> list;
		// String[][] temp = new String[30][3];
		// for (int i = 0; i < 30; i++) {
		// list = db.getLesson(i + 1);
		// temp[i][0] = list.get(0).getWord();
		// temp[i][1] = list.get(1).getWord();
		// temp[i][2] = list.get(2).getWord();
		// }
		// db.close();
		// LessonAdapter adapter = new LessonAdapter(MainActivity.this, temp);
		// lv.setAdapter(adapter);
		new ShowListTask().execute();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_main, menu);
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
		// if (mDrawerToggle.onOptionsItemSelected(item)) {
		// return true;
		// }
		// // Handle action buttons
		// switch (item.getItemId()) {
		// case R.id.action_websearch:
		// // create intent to perform web search for this planet
		// Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
		// intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
		// // catch event that there's no activity to handle intent
		// if (intent.resolveActivity(getPackageManager()) != null) {
		// startActivity(intent);
		// } else {
		// Toast.makeText(this, R.string.app_not_available,
		// Toast.LENGTH_LONG).show();
		// }
		// return true;
		// default:
		return super.onOptionsItemSelected(item);
		// }
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		Intent i = new Intent(MainActivity.this, ExcerciseActivity.class);
		i.putExtra("Num", position + 1);
		startActivity(i);
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

		@Override
		protected void onPostExecute(Void unused) {
			// stop the loading animation or something
		}

		@Override
		protected void onPreExecute() {
			lv = (ListView) findViewById(R.id.lession_list);
			word_lists = new String[30][3];
			word_data = new int[30][5];
			adapter = new LessonAdapter(MainActivity.this, word_lists, word_data);
			lv.setAdapter(adapter);
		}

		@SuppressWarnings({ "unchecked" })
		@Override
		protected Void doInBackground(Void... params) {
			DatabaseHandler db;
			ArrayList<Word> list;
			HashMap<String, Integer> temp;
			int[] leitner;
			for (int i = 0; i < 30; i++) {
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
				word_lists[i][0] = list.get(0).getWord();
				word_lists[i][1] = list.get(1).getWord();
				word_lists[i][2] = list.get(2).getWord();
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