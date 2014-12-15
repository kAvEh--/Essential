package com.kAvEh.essentialwords;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class ExcerciseActivity extends Activity {

	int lesson_num;
	int excer_num = 0;
	ArrayList<Excercise> exc;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;

	TextView header;
	TextView exc_num;
	TextView exc_question_type;
	TextView exc_question;
	TextView exc_a;
	TextView exc_b;
	TextView exc_c;
	TextView exc_d;
	RelativeLayout exc_a_layout;
	RelativeLayout exc_b_layout;
	RelativeLayout exc_c_layout;
	RelativeLayout exc_d_layout;
	ImageView prev_answer;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_excrecise);
		Intent i = getIntent();
		lesson_num = i.getIntExtra("Num", 1);

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
			selectItem(0);
		}

		// ---------------
		header = (TextView) findViewById(R.id.excer_header);
		exc_num = (TextView) findViewById(R.id.excer_num);
		exc_question_type = (TextView) findViewById(R.id.excer_question_general);
		exc_question = (TextView) findViewById(R.id.excer_question);
		exc_a = (TextView) findViewById(R.id.excer_a);
		exc_b = (TextView) findViewById(R.id.excer_b);
		exc_c = (TextView) findViewById(R.id.excer_c);
		exc_d = (TextView) findViewById(R.id.excer_d);
		exc_a_layout = (RelativeLayout) findViewById(R.id.excer_a_layout);
		exc_b_layout = (RelativeLayout) findViewById(R.id.excer_b_layout);
		exc_c_layout = (RelativeLayout) findViewById(R.id.excer_c_layout);
		exc_d_layout = (RelativeLayout) findViewById(R.id.excer_d_layout);

		// --- load Datas
		header.setText("Lesson " + (lesson_num + 1));
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		exc = db.getExcercise(lesson_num + 1);
		ImageView excer_next = (ImageView) findViewById(R.id.excer_next);
		ImageView excer_prev = (ImageView) findViewById(R.id.excer_prev);
		ImageView correct = (ImageView) findViewById(R.id.show_correct);
		prev_answer = (ImageView) findViewById(R.id.prev_answer);
		setExcercise(excer_num);
		correct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (exc.get(excer_num).getAnswer().equals("A")) {
					exc_a_layout.setBackgroundColor(getResources().getColor(
							R.color.green));
				} else if (exc.get(excer_num).getAnswer().equals("B")) {
					exc_b_layout.setBackgroundColor(getResources().getColor(
							R.color.green));
				} else if (exc.get(excer_num).getAnswer().equals("C")) {
					exc_c_layout.setBackgroundColor(getResources().getColor(
							R.color.green));
				} else {
					exc_d_layout.setBackgroundColor(getResources().getColor(
							R.color.green));
				}
			}
		});
		excer_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (excer_num == 14)
					excer_num = 0;
				else
					excer_num += 1;

				setExcercise(excer_num);
			}
		});
		excer_prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (excer_num == 0)
					excer_num = 14;
				else
					excer_num -= 1;

				setExcercise(excer_num);
			}
		});
		exc_a_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHandler db = new DatabaseHandler(
						getApplicationContext());
				db.answerQuestion(exc.get(excer_num).getID(), "A");
				db.close();
				exc.get(excer_num).setState("A");
				if (exc.get(excer_num).getAnswer().equals("A")) {
					exc_a_layout.setBackgroundColor(getResources().getColor(
							R.color.green));
					prev_answer.setVisibility(View.VISIBLE);
					prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_accept));
				} else {
					exc_a_layout.setBackgroundColor(getResources().getColor(
							R.color.red));
					prev_answer.setVisibility(View.VISIBLE);
					prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_cancel));
				}
			}
		});
		exc_b_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHandler db = new DatabaseHandler(
						getApplicationContext());
				db.answerQuestion(exc.get(excer_num).getID(), "B");
				db.close();
				exc.get(excer_num).setState("B");
				if (exc.get(excer_num).getAnswer().equals("B")) {
					exc_b_layout.setBackgroundColor(getResources().getColor(
							R.color.green));
					prev_answer.setVisibility(View.VISIBLE);
					prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_accept));
				} else {
					exc_b_layout.setBackgroundColor(getResources().getColor(
							R.color.red));
					prev_answer.setVisibility(View.VISIBLE);
					prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_cancel));
				}
			}
		});
		exc_c_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHandler db = new DatabaseHandler(
						getApplicationContext());
				db.answerQuestion(exc.get(excer_num).getID(), "C");
				db.close();
				exc.get(excer_num).setState("C");
				if (exc.get(excer_num).getAnswer().equals("C")) {
					exc_c_layout.setBackgroundColor(getResources().getColor(
							R.color.green));
					prev_answer.setVisibility(View.VISIBLE);
					prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_accept));
				} else {
					exc_c_layout.setBackgroundColor(getResources().getColor(
							R.color.red));
					prev_answer.setVisibility(View.VISIBLE);
					prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_cancel));
				}
			}
		});
		exc_d_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DatabaseHandler db = new DatabaseHandler(
						getApplicationContext());
				db.answerQuestion(exc.get(excer_num).getID(), "D");
				db.close();
				exc.get(excer_num).setState("D");
				if (exc.get(excer_num).getAnswer().equals("D")) {
					exc_d_layout.setBackgroundColor(getResources().getColor(
							R.color.green));
					prev_answer.setVisibility(View.VISIBLE);
					prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_accept));
				} else {
					exc_d_layout.setBackgroundColor(getResources().getColor(
							R.color.red));
					prev_answer.setVisibility(View.VISIBLE);
					prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_cancel));
				}
			}
		});
	}

	private void setExcercise(int _num) {
		if (exc.get(_num).getType().equals("matching")) {
			exc_question_type.setText(this.getResources().getString(
					R.string.first_test));
			exc_question.setText(exc.get(_num).getQuestion());
		} else {
			exc_question_type.setText(this.getResources().getString(
					R.string.second_test));
			String text = exc.get(_num).getQuestion();
			int f1 = text.indexOf("~");
			int f2 = text.indexOf("~", f1 + 1);
			text = text.replaceAll("~", " ");
			SpannableString spannable = new SpannableString(text);
			// here we set the color
			spannable.setSpan(new StyleSpan(Typeface.BOLD), f1, f2, 0);
			spannable.setSpan(new UnderlineSpan(), (f1 + 1), f2, 0);
			exc_question.setText(spannable);
		}

		if (exc.get(_num).getState().equals("n")) {
			prev_answer.setVisibility(View.INVISIBLE);
		} else {
			if (exc.get(_num).getState().equals(exc.get(_num).getAnswer())) {
				prev_answer.setVisibility(View.VISIBLE);
				prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_accept));
			} else {
				prev_answer.setVisibility(View.VISIBLE);
				prev_answer.setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation_cancel));
			}
		} 
		exc_num.setText(String.valueOf(_num + 1) + " / 15");
		exc_a.setText(exc.get(_num).getA());
		exc_b.setText(exc.get(_num).getB());
		exc_c.setText(exc.get(_num).getC());
		exc_d.setText(exc.get(_num).getD());
		exc_a_layout.setBackgroundColor(getResources().getColor(
				R.color.light_yellow));
		exc_b_layout.setBackgroundColor(getResources().getColor(
				R.color.light_yellow));
		exc_c_layout.setBackgroundColor(getResources().getColor(
				R.color.light_yellow));
		exc_d_layout.setBackgroundColor(getResources().getColor(
				R.color.light_yellow));
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_main, menu);
		// Associate searchable configuration with the SearchView
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setSearchableInfo(searchManager
				.getSearchableInfo(getComponentName()));
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view

		return super.onPrepareOptionsMenu(menu);
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_websearch:
			// create intent to perform web search for this planet
			Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// catch event that there's no activity to handle intent
			if (intent.resolveActivity(getPackageManager()) != null) {
				startActivity(intent);
			} else {
				Toast.makeText(this, R.string.app_not_available,
						Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
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
		/*
		 * update the main content by replacing fragments Fragment fragment =
		 * new LessionFragment(); Bundle args = new Bundle();
		 * fragment.setArguments(args);
		 * 
		 * FragmentManager fragmentManager = getFragmentManager();
		 * fragmentManager.beginTransaction().replace(R.id.content_frame,
		 * fragment).commit();
		 * 
		 * // update selected item and title, then close the drawer
		 * mDrawerList.setItemChecked(position, true);
		 * setTitle(mPlanetTitles[position]);
		 * mDrawerLayout.closeDrawer(mDrawerList);
		 */
	}

	@SuppressLint("NewApi")
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

}
