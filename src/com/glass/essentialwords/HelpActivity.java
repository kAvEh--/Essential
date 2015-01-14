package com.glass.essentialwords;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

@SuppressLint("ClickableViewAccessibility")
public class HelpActivity extends Activity {

	FragmentManager fm;
	FragmentTransaction ft;

	HelpFragment fragment;

	int indicator = 1;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	ImageButton next;
	ImageButton prev;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);

		// Gesture detection
		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		RelativeLayout main = (RelativeLayout) findViewById(R.id.help_main_layout);
		main.setOnTouchListener(gestureListener);

		next = (ImageButton) findViewById(R.id.help_next);
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nextHelp();
			}
		});
		prev = (ImageButton) findViewById(R.id.help_prev);
		prev.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				prevHelp();
			}
		});

		fragment = new HelpFragment();
		fragment.setPage(indicator);
		fm = getFragmentManager();

		ft = fm.beginTransaction();

		ft.add(R.id.helpFragment, fragment);
		ft.commit();
	}

	public void nextHelp() {
		if (indicator > 3)
			return;
		indicator++;
		if (indicator == 4)
			next.setVisibility(View.INVISIBLE);
		prev.setVisibility(View.VISIBLE);
		fragment = new HelpFragment();
		fragment.setPage(indicator);
		fm = getFragmentManager();

		ft = fm.beginTransaction();

		ft.setCustomAnimations(R.anim.right_in, R.anim.left_out).replace(
				R.id.helpFragment, fragment);
		ft.commit();
	}

	public void prevHelp() {
		if (indicator < 2)
			return;
		indicator--;
		if (indicator == 1)
			prev.setVisibility(View.INVISIBLE);
		next.setVisibility(View.VISIBLE);
		fragment = new HelpFragment();
		fragment.setPage(indicator);
		fm = getFragmentManager();

		ft = fm.beginTransaction();

		ft.setCustomAnimations(R.anim.left_in, R.anim.right_out).replace(
				R.id.helpFragment, fragment);
		ft.commit();
	}

	public void setNext() {

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
					nextHelp();
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					// Right
					prevHelp();
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
