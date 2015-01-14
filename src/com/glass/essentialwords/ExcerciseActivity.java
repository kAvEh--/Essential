package com.glass.essentialwords;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ExcerciseActivity extends FragmentActivity {

	int lesson_num;
	int excer_num = 0;
	ArrayList<Excercise> exc;

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

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
	ImageButton answer;

	boolean auto_next = false;

	private Handler mHandler = new Handler();

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_excrecise);
		Intent i = getIntent();
		lesson_num = i.getIntExtra("Num", 1);

		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		RelativeLayout main_rl = (RelativeLayout) findViewById(R.id.excer_main);
		main_rl.setOnTouchListener(gestureListener);

		setTitle("Excercise " + lesson_num);
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
		mDrawerList.setAdapter(new DrawerMenuAdapter(ExcerciseActivity.this,
				menu, icons));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

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
		answer = (ImageButton) findViewById(R.id.excer_answer);

		// --- load Datas
		header.setText("Lesson " + (lesson_num + 1));
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		exc = db.getExcercise(lesson_num + 1);
		ImageView excer_next = (ImageView) findViewById(R.id.excer_next);
		ImageView excer_prev = (ImageView) findViewById(R.id.excer_prev);
		setExcercise(excer_num);
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
					exc_a_layout.setBackgroundResource(R.drawable.exc_correct);
					autoShift();
				} else {
					exc_a_layout.setBackgroundResource(R.drawable.exc_error);
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
					exc_b_layout.setBackgroundResource(R.drawable.exc_correct);
					autoShift();
				} else {
					exc_b_layout.setBackgroundResource(R.drawable.exc_error);
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
					exc_c_layout.setBackgroundResource(R.drawable.exc_correct);
					autoShift();
				} else {
					exc_c_layout.setBackgroundResource(R.drawable.exc_error);
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
					exc_d_layout.setBackgroundResource(R.drawable.exc_correct);
					autoShift();
				} else {
					exc_d_layout.setBackgroundResource(R.drawable.exc_error);
				}
			}
		});

		answer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String ans = exc.get(excer_num).getAnswer();
				if (ans.equals("A"))
					exc_a_layout.setBackgroundResource(R.drawable.exc_correct);
				else if (ans.equals("B"))
					exc_b_layout.setBackgroundResource(R.drawable.exc_correct);
				else if (ans.equals("C"))
					exc_c_layout.setBackgroundResource(R.drawable.exc_correct);
				else
					exc_d_layout.setBackgroundResource(R.drawable.exc_correct);

				autoShift();
			}
		});
	}

	private void autoShift() {
		auto_next = true;
		mHandler.postDelayed(new Runnable() {
			public void run() {
				if (auto_next) {
					if (excer_num == 14)
						excer_num = 0;
					else
						excer_num += 1;

					setExcercise(excer_num);
				}
			}
		}, 800);
	}

	private void setExcercise(int _num) {
		auto_next = false;
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

		exc_num.setText(String.valueOf(_num + 1) + " / 15");
		exc_a.setText(exc.get(_num).getA());
		exc_b.setText(exc.get(_num).getB());
		exc_c.setText(exc.get(_num).getC());
		exc_d.setText(exc.get(_num).getD());
		exc_a_layout.setBackgroundResource(R.drawable.exc_default);
		exc_b_layout.setBackgroundResource(R.drawable.exc_default);
		exc_c_layout.setBackgroundResource(R.drawable.exc_default);
		exc_d_layout.setBackgroundResource(R.drawable.exc_default);
	}

	@SuppressLint("NewApi")
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
				Intent i = new Intent(ExcerciseActivity.this,
						LeitnerActivity.class);
				startActivity(i);
			}
				break;
			case 2:

				break;

			default:
				break;
			}
		}
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
					if (excer_num == 14)
						excer_num = 0;
					else
						excer_num += 1;

					setExcercise(excer_num);
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// Right
					if (excer_num == 0)
						excer_num = 14;
					else
						excer_num -= 1;

					setExcercise(excer_num);
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
