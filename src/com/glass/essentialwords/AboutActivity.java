package com.glass.essentialwords;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		TextView body_1 = (TextView) findViewById(R.id.about_body_1);
		TextView body_2 = (TextView) findViewById(R.id.about_body_2);
		TextView body_3 = (TextView) findViewById(R.id.about_body_3);
		TextView body_4 = (TextView) findViewById(R.id.about_body_4);
		TextView body_5 = (TextView) findViewById(R.id.about_body_5);
		TextView body_6 = (TextView) findViewById(R.id.about_body_6);

		body_1.setText(getResources().getString(R.string.about1));
		body_2.setText(getResources().getString(R.string.about2));
		body_3.setText(getResources().getString(R.string.about3));
		body_4.setText("- " + getResources().getString(R.string.about4));
		body_5.setText("- " + getResources().getString(R.string.about5));
		body_6.setText(getResources().getString(R.string.about6));
	}
}
