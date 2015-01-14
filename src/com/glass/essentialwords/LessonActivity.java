package com.glass.essentialwords;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LessonActivity extends FragmentActivity implements
		TextToSpeech.OnInitListener {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

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

	String day = "روز";

	private TextToSpeech tts;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lesson);

		Intent i = getIntent();
		mLesson = i.getIntExtra("Num", 1);
		int toWord = i.getIntExtra("Word", 0);
		int toId = i.getIntExtra("WordID", 0);

		tts = new TextToSpeech(this, this);

		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		RelativeLayout main_rl = (RelativeLayout) findViewById(R.id.detail_main);
		main_rl.setOnTouchListener(gestureListener);

		setTitle("Lesson " + mLesson);
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		String[] menu;
		int[] icons;
		if (mLesson > 30) {
			menu = new String[4];
			menu[0] = "Add Word";
			menu[1] = "Leitner";
			menu[2] = "Help";
			menu[3] = "About Us";
			icons = new int[4];
			icons[0] = R.drawable.ic_action_add;
			icons[1] = R.drawable.ic_action_leitner;
			icons[2] = R.drawable.ic_action_help;
			icons[3] = R.drawable.ic_action_info;
		} else {
			menu = new String[5];
			menu[0] = "Add Word";
			menu[1] = "Excercise";
			menu[2] = "Leitner";
			menu[3] = "Help";
			menu[4] = "About Us";
			icons = new int[5];
			icons[0] = R.drawable.ic_action_add;
			icons[1] = R.drawable.ic_action_exer;
			icons[2] = R.drawable.ic_action_leitner;
			icons[3] = R.drawable.ic_action_help;
			icons[4] = R.drawable.ic_action_info;
		}
		mDrawerList.setAdapter(new DrawerMenuAdapter(LessonActivity.this, menu,
				icons));
		if (mLesson > 30) {
			mDrawerList
					.setOnItemClickListener(new DrawerItemClickListenerCustom());
		} else {
			mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		}

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_action_drawer, /*
									 * nav drawer image to replace 'Up' caret
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

		// end of Drawer Menu

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
		if (toId != 0) {
			for (int j = 0; j < words.size(); j++) {
				if (words.get(j).getID() == toId) {
					indicator = j;
					break;
				}
			}
		}
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
		front_fr.setLessonNum(_w.getLesson());
		front_fr.setNumber((indicator + 1) + "/" + words.size());
		ft = fm.beginTransaction();

		ft.add(R.id.frontFragment, front_fr);
		ft.commit();

		setLeitner();
	}

	private void setLeitner() {
		final ImageView bar = (ImageView) findViewById(R.id.footer_blue_bar);
		final TextView text = (TextView) findViewById(R.id.footer_percent);
		final RelativeLayout footer_text = (RelativeLayout) findViewById(R.id.footer_holder);
		final ImageView footer_image = (ImageView) findViewById(R.id.footer_add_leitner);
		footer_image.setOnClickListener(new OnClickListener() {

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
					text.setText(day + " 1");
					footer_image.setVisibility(View.GONE);
					footer_text.setVisibility(View.VISIBLE);
				}
			}
		});
		float percent = 0;
		footer_image.setVisibility(View.GONE);
		footer_text.setVisibility(View.GONE);
		switch (_w.getLeitnerStage()) {
		case 0: {
			percent = 0;
			footer_image.setVisibility(View.VISIBLE);
		}
			break;
		case 1: {
			percent = .3f;
			text.setText(day + " 1");
			footer_text.setVisibility(View.VISIBLE);
		}
			break;
		case 2: {
			percent = (float) (1 + (0.9 * (2 - _w.getLeitnerPart())));
			text.setText(day + " " + (4 - _w.getLeitnerPart()));
			footer_text.setVisibility(View.VISIBLE);
		}
			break;
		case 3: {
			percent = (float) (2.8 + ((4 - _w.getLeitnerPart()) * 0.5));
			text.setText(day + " " + (8 - _w.getLeitnerPart()));
			footer_text.setVisibility(View.VISIBLE);
		}
			break;
		case 4: {
			percent = (float) (4.8 + ((8 - _w.getLeitnerPart()) * 0.275));
			text.setText(day + " " + (16 - _w.getLeitnerPart()));
			footer_text.setVisibility(View.VISIBLE);
		}
			break;
		case 5: {
			percent = (float) (7 + ((16 - _w.getLeitnerPart()) * 0.18125));
			text.setText(day + " " + (32 - _w.getLeitnerPart()));
			footer_text.setVisibility(View.VISIBLE);
		}
			break;
		case 6: {
			percent = 9.9f;
			text.setText("100%");
			footer_text.setVisibility(View.VISIBLE);
		}
			break;
		default:
			break;
		}
		bar.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, percent));
	}

	public void removeLeitner(View v) {
		_w.setLeitnerPart(0);
		_w.setLeitnerStage(0);
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.setLeitner(_w.getID(), 0, 0);
		db.close();

		setLeitner();
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
		front_fr = new FrontCardFragment();
		front_fr.setWord(_w.getWord());
		front_fr.setLesson("Lesson " + _w.getLesson());
		front_fr.setLessonNum(_w.getLesson());
		front_fr.setNumber((indicator + 1) + "/" + words.size());
		ft = fm.beginTransaction();
		ft.setCustomAnimations(R.animator.card_flip_right_in,
				R.animator.card_flip_right_out, R.animator.card_flip_left_in,
				R.animator.card_flip_left_out).replace(R.id.frontFragment,
				front_fr);
		ft.commit();
	}

	@SuppressLint("NewApi")
	public void editCard(View v) {
		EditCardFragment edit = new EditCardFragment();
		edit.setContent(_w);
		edit.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
		edit.show(getSupportFragmentManager(), "Hello");
	}

	@SuppressLint("NewApi")
	public void updateList() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		words = db.getLesson(mLesson);
		db.close();
		_w = words.get(indicator);
		front_fr = new FrontCardFragment();
		front_fr.setWord(_w.getWord());
		front_fr.setLesson("Lesson " + _w.getLesson());
		front_fr.setLessonNum(_w.getLesson());
		front_fr.setNumber((indicator + 1) + "/" + words.size());
		ft = fm.beginTransaction();
		ft.replace(R.id.frontFragment, front_fr);
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
		front_fr.setLessonNum(_w.getLesson());
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
		front_fr.setLessonNum(_w.getLesson());
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

		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

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
			}
				break;

			case 1: {
				Intent i = new Intent(LessonActivity.this,
						ExerciseActivity.class);
				i.putExtra("Num", mLesson - 1);
				startActivity(i);
			}
				break;
			case 2: {
				Intent i = new Intent(LessonActivity.this,
						LeitnerActivity.class);
				startActivity(i);
			}

				break;
			case 3: {
				Intent i = new Intent(LessonActivity.this, HelpActivity.class);
				startActivity(i);
			}
				break;

			case 4: {
				Intent i = new Intent(LessonActivity.this, AboutActivity.class);
				startActivity(i);
			}
				break;
			default:
				break;
			}
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListenerCustom implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			switch (position) {
			case 0: {
				AddNewWordFragment fr = new AddNewWordFragment();
				fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
				fr.show(getSupportFragmentManager(), "Hello");
			}
				break;

			case 1: {
				Intent i = new Intent(LessonActivity.this,
						LeitnerActivity.class);
				startActivity(i);
			}

				break;
			default:
				break;
			}
		}
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

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// Left
					if (indicator < words.size() - 1)
						next_word();
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// Right
					if (indicator > 0)
						prev_word();
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
	}
}