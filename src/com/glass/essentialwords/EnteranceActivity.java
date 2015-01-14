package com.glass.essentialwords;

import ir.adad.Adad;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class EnteranceActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enterance);
		
		ImageButton leitner = (ImageButton) findViewById(R.id.ent_leitner);
		leitner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnteranceActivity.this, LeitnerActivity.class);
//				i.putExtra("Num", position + 1);
				startActivity(i);
			}
		});
		
		ImageButton lessons = (ImageButton) findViewById(R.id.ent_lessons);
		lessons.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnteranceActivity.this, MainActivity.class);
//				i.putExtra("Num", position + 1);
				startActivity(i);
			}
		});
		
		ImageButton help = (ImageButton) findViewById(R.id.ent_help);
		help.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnteranceActivity.this, HelpActivity.class);
				startActivity(i);
			}
		});
		
		ImageButton about = (ImageButton) findViewById(R.id.ent_about);
		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(EnteranceActivity.this, AboutActivity.class);
				startActivity(i);
			}
		});
		
		TextView leitner_in = (TextView) findViewById(R.id.ent_leitner_in);
		TextView leitner_out = (TextView) findViewById(R.id.ent_leitner_out);
		
		DatabaseHandler db = new DatabaseHandler(getApplicationContext());
		db.updateLeitner();
		int l_out = db.getLearnedWordsCount();
		int l_in = db.getLeitnerWordsCount();
		db.close();
		
		leitner_in.setText(l_in + "");
		leitner_out.setText(l_out + "");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
