package com.kAvEh.essentialwords;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LessonActivity extends Activity implements
		TextToSpeech.OnInitListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;

	private int mLesson;

	Word _w;
	TextView _word;
	ImageButton _speek;
	FragmentManager fm;
	FragmentTransaction ft;
	FrontCardFragment front_fr;
	BackCardFragment back_fr;
	ArrayList<Word> words;

	ImageButton next;
	ImageButton prev;

	int indicator;

	private TextToSpeech tts;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson);

		Intent i = getIntent();
		mLesson = i.getIntExtra("Num", 1);
		int toWord = i.getIntExtra("Word", 0);

		tts = new TextToSpeech(this, this);

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

		// render word card ----------
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		words = db.getLesson(mLesson);
		db.close();

		_word = (TextView) findViewById(R.id.card_word);
		_speek = (ImageButton) findViewById(R.id.card_voice);

		next = (ImageButton) findViewById(R.id.lesson_next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				next_word();
			}
		});
		prev = (ImageButton) findViewById(R.id.lesson_prev);
		prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prev_word();
			}
		});

		// show first card ---------------------
		if (toWord > 0 && toWord <= words.size())
			indicator = toWord - 1;
		else
			indicator = 0;
		if (indicator < words.size() - 1)
			next.setVisibility(View.VISIBLE);
		else
			next.setVisibility(View.INVISIBLE);
		if (indicator > 0)
			prev.setVisibility(View.VISIBLE);
		else
			prev.setVisibility(View.INVISIBLE);
		front_fr = new FrontCardFragment();
		back_fr = new BackCardFragment();

		_w = words.get(indicator);

		fm = getFragmentManager();

		front_fr.setWord(_w.getWord());
		front_fr.setLesson("Lesson " + _w.getLesson());
		front_fr.setNumber((indicator + 1) + "/" + words.size());
		ft = fm.beginTransaction();

		ft.add(R.id.frontFragment, front_fr);
		ft.commit();

		setLeitner();
	}

	private void setLeitner() {
		final ImageView bar = (ImageView) findViewById(R.id.footer_blue_bar);
		final TextView text = (TextView) findViewById(R.id.footer_percent);
		text.setBackgroundResource(0);
		text.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (_w.getLeitnerStage() == 0) {
					DatabaseHandler db = new DatabaseHandler(
							getApplicationContext());
					db.setLeitner(_w.getID(), 1, 1);
					db.close();
					_w.setLeitnerStage(1);
					_w.setLeitnerPart(1);
					bar.setLayoutParams(new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.MATCH_PARENT, .5f));
					text.setText("0 %");
					text.setBackgroundResource(0);
				}
			}
		});
		float percent = 0;
		switch (_w.getLeitnerStage()) {
		case 0: {
			percent = 0;
			text.setText("");
			text.setBackgroundResource(R.drawable.add);
		}
			break;
		case 1: {
			percent = .3f;
			text.setText("0 %");
		}
			break;
		case 2: {
			percent = (float) (1 + (0.9 * (2 - _w.getLeitnerPart())));
			text.setText(Math.round(((4 - _w.getLeitnerPart()) * 3.2)) + " %");
		}
			break;
		case 3: {
			percent = (float) (2.8 + ((4 - _w.getLeitnerPart()) * 0.5));
			text.setText(Math.round(((8 - _w.getLeitnerPart()) * 3.2)) + " %");
		}
			break;
		case 4: {
			percent = (float) (4.8 + ((8 - _w.getLeitnerPart()) * 0.275));
			text.setText(Math.round(((16 - _w.getLeitnerPart()) * 3.2)) + " %");
		}
			break;
		case 5: {
			percent = (float) (7 + ((16 - _w.getLeitnerPart()) * 0.18125));
			text.setText(Math.round(((32 - _w.getLeitnerPart()) * 3.2)) + " %");
		}
			break;
		case 6: {
			percent = 9.9f;
			text.setText("100 %");
		}
			break;
		default:
			break;
		}
		bar.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, percent));
	}

	@SuppressLint("NewApi")
	public void flipCard(View v) {
		back_fr = new BackCardFragment();
		back_fr.setWordBack(_w.getWord(), _w.getpart1(), _w.getPart2(),
				_w.getExample(), _w.getTrans());
		ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.card_flip_right_in,
				R.animator.card_flip_right_out, R.animator.card_flip_left_in,
				R.animator.card_flip_left_out).replace(R.id.frontFragment,
				back_fr);
		ft.commit();
	}

	@SuppressLint("NewApi")
	public void flipCardBack(View v) {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.setLeitner(_w.getID(), 1, 1);
		db.close();
		front_fr = new FrontCardFragment();
		front_fr.setWord(_w.getWord());
		front_fr.setLesson("Lesson " + _w.getLesson());
		front_fr.setNumber((indicator + 1) + "/" + words.size());
		ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.card_flip_right_in,
				R.animator.card_flip_right_out, R.animator.card_flip_left_in,
				R.animator.card_flip_left_out).replace(R.id.frontFragment,
				front_fr);
		ft.commit();
	}

	public void speech(View v) {
		this.tts.speak(_w.getWord(), TextToSpeech.QUEUE_FLUSH, null);
	}

	@SuppressLint("NewApi")
	public void next_word() {
		indicator++;
		if (indicator < words.size() - 1)
			next.setVisibility(View.VISIBLE);
		else
			next.setVisibility(View.INVISIBLE);
		prev.setVisibility(View.VISIBLE);
		_w = words.get(indicator);
		front_fr = new FrontCardFragment();
		front_fr.setWord(_w.getWord());
		front_fr.setLesson("Lesson " + _w.getLesson());
		front_fr.setNumber((indicator + 1) + "/" + words.size());
		ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.right_in, R.anim.left_out).replace(
				R.id.frontFragment, front_fr);
		ft.commit();
		setLeitner();
	}

	@SuppressLint("NewApi")
	public void prev_word() {
		indicator--;
		if (indicator > 0)
			prev.setVisibility(View.VISIBLE);
		else
			prev.setVisibility(View.INVISIBLE);
		next.setVisibility(View.VISIBLE);
		_w = words.get(indicator);
		front_fr = new FrontCardFragment();
		front_fr.setWord(_w.getWord());
		front_fr.setLesson("Lesson " + _w.getLesson());
		front_fr.setNumber((indicator + 1) + "/" + words.size());
		ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.left_in, R.anim.right_out).replace(
				R.id.frontFragment, front_fr);
		ft.commit();
		setLeitner();
	}

	public void shareWord(View v) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String shareBody = "I`m Learning '" + _w.getWord() + "' via our App";
		sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				"Learn new word with Our App");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

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

	@Override
	public void onDestroy() {
		// Don't forget to shutdown!
		if (tts != null) {
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {

		if (status == TextToSpeech.SUCCESS) {

			int result = tts.setLanguage(Locale.US);

			// tts.setPitch(5); // set pitch level

			// tts.setSpeechRate(2); // set speech speed rate

			if (result == TextToSpeech.LANG_MISSING_DATA
					|| result == TextToSpeech.LANG_NOT_SUPPORTED) {
				Log.e("TTS", "Language is not supported");
			} else {

			}

		} else {
			Log.e("TTS", "Initilization Failed");
		}

	}
}