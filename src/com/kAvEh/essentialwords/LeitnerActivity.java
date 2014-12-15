package com.kAvEh.essentialwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LeitnerActivity extends Activity implements
		TextToSpeech.OnInitListener {

	HashMap<String, String> data;
	Word _w;
	TextView _word;
	ImageButton _speek;
	FragmentManager fm;
	FragmentTransaction ft;
	LeitnerFrontFragment front_fr;
	LeitnerBackFragment back_fr;
	ArrayList<Word> words;

	ImageButton next;
	ImageButton prev;

	int indicator;

	private TextToSpeech tts;

	int last_stage = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leitner);

		tts = new TextToSpeech(this, this);

		indicator = 0;

		_word = (TextView) findViewById(R.id.card_word);
		_speek = (ImageButton) findViewById(R.id.card_voice);

		next = (ImageButton) findViewById(R.id.leitner_next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				next_word();
			}
		});
		prev = (ImageButton) findViewById(R.id.leitner_prev);
		prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prev_word();
			}
		});

		front_fr = new LeitnerFrontFragment();
		back_fr = new LeitnerBackFragment();

		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.updateLeitner();
		db.close();
		updateStage();
	}

	public void generateword(View v) {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.generateLeitner();
		db.close();
		updateStage();
	}

	private void setLeitnerBar() {
		if (_w == null)
			return;
		final ImageView bar = (ImageView) findViewById(R.id.footer_blue_bar);
		final TextView text = (TextView) findViewById(R.id.footer_percent);
		// text.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (_w.getLeitnerStage() == 0) {
		// DatabaseHandler db = new DatabaseHandler(
		// getApplicationContext());
		// db.setLeitner(_w.getID(), 1, 1);
		// db.close();
		// _w.setLeitnerStage(1);
		// _w.setLeitnerPart(1);
		// bar.setLayoutParams(new LinearLayout.LayoutParams(
		// LayoutParams.WRAP_CONTENT,
		// LayoutParams.MATCH_PARENT, .5f));
		// text.setText("0 %");
		// }
		// }
		// });
		float percent = 0;
		switch (_w.getLeitnerStage()) {
		case 0: {
			percent = 0;
			text.setText("Add To Leitner");
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

	public void updateStage() {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.updateCounter();
		data = db.getLeitnerData();
		db.close();
		if (Integer.parseInt(data.get("stage5")) > 0) {
			showStage(5);
		} else if (Integer.parseInt(data.get("stage4")) > 0) {
			showStage(4);
		} else if (Integer.parseInt(data.get("stage3")) > 0) {
			showStage(3);
		} else if (Integer.parseInt(data.get("stage2")) > 0) {
			showStage(2);
		} else if (Integer.parseInt(data.get("stage1")) > 0) {
			showStage(1);
		} else {
			showStage(0);
		}
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
		front_fr = new LeitnerFrontFragment();
		front_fr.setWord(_w.getWord());
		front_fr.setLesson("Lesson " + _w.getLesson());
		front_fr.setNumber((indicator + 1) + "/" + words.size());
		ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.right_in, R.anim.left_out).replace(
				R.id.frontFragment, front_fr);
		ft.commit();
		setLeitnerBar();
	}

	public void speech(View v) {
		this.tts.speak(_w.getWord(), TextToSpeech.QUEUE_FLUSH, null);
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
		front_fr = new LeitnerFrontFragment();
		front_fr.setWord(_w.getWord());
		front_fr.setLesson("Lesson " + _w.getLesson());
		front_fr.setNumber((indicator + 1) + "/" + words.size());
		ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.left_in, R.anim.right_out).replace(
				R.id.frontFragment, front_fr);
		ft.commit();
		setLeitnerBar();
	}

	@SuppressLint("NewApi")
	public void flipCard(View v) {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.setLeitner(_w.getID(), 1, 1);
		db.close();
		back_fr = new LeitnerBackFragment();
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
		front_fr = new LeitnerFrontFragment();
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

	public void levelUp(View v) {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.setLeitner(_w.getID(), _w.getLeitnerStage() + 1,
				(int) Math.pow(2, _w.getLeitnerStage()));
		db.close();
		updateStage();
	}

	public void ignoreCard(View v) {
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.setLeitner(_w.getID(), 6, 1);
		db.close();
		updateStage();
	}

	@SuppressLint("NewApi")
	private void showStage(int stage) {
		if (stage == 0) {
			_w = null;
			next.setVisibility(View.INVISIBLE);
			prev.setVisibility(View.INVISIBLE);
			NoLeitnerFragmet no_fr = new NoLeitnerFragmet();
			fm = getFragmentManager();
			ft = fm.beginTransaction();
			if (fm.findFragmentById(R.id.frontFragment) == null)
				ft.add(R.id.frontFragment, no_fr);
			else
				ft.setCustomAnimations(R.anim.left_in, R.anim.right_out)
						.replace(R.id.frontFragment, no_fr);
			ft.commit();
		} else {
			DatabaseHandler db = new DatabaseHandler(getApplicationContext());
			words = db.getLeitnerWords(stage);
			db.close();
			if (indicator >= words.size())
				indicator = words.size() - 1;
			if (words.size() < 2) {
				next.setVisibility(View.INVISIBLE);
				prev.setVisibility(View.INVISIBLE);
			} else {
				if (indicator > 1)
					prev.setVisibility(View.VISIBLE);
				if (indicator < words.size())
					next.setVisibility(View.VISIBLE);
			}
			_w = words.get(indicator);
			// get fragment manager
			fm = getFragmentManager();

			// add
			front_fr = new LeitnerFrontFragment();
			front_fr.setWord(_w.getWord());
			front_fr.setLesson("Lesson " + _w.getLesson());
			front_fr.setNumber((indicator + 1) + "/" + words.size());
			ft = fm.beginTransaction();

			if (fm.findFragmentById(R.id.frontFragment) == null)
				ft.add(R.id.frontFragment, front_fr);
			else if (last_stage == 0 || last_stage == stage)
				ft.setCustomAnimations(R.anim.left_in, R.anim.right_out)
						.replace(R.id.frontFragment, front_fr);
			else
				ft.setCustomAnimations(R.animator.card_flip_right_in,
						R.animator.card_flip_right_out,
						R.animator.card_flip_left_in,
						R.animator.card_flip_left_out).replace(
						R.id.frontFragment, front_fr);
			ft.commit();
			last_stage = stage;
		}
		setLeitnerBar();
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
