package com.kAvEh.essentialwords;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

@SuppressLint("NewApi")
public class BackCardFragment extends Fragment {

	String _word;
	String _part1;
	String _part2;
	String _example;
	String _trans;

	TextView word;
	TextView part1;
	TextView part2;
	TextView example;
	ImageButton loadTrans;
	TextView trans;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_card_back, container,
				false);
		word = (TextView) view.findViewById(R.id.back_word);
		word.setText(_word);
		part1 = (TextView) view.findViewById(R.id.back_part1);
		part1.setText(_part1);
		part2 = (TextView) view.findViewById(R.id.back_part2);
		part2.setText(_part2);
		example = (TextView) view.findViewById(R.id.back_example);
		loadTrans = (ImageButton) view.findViewById(R.id.back_loadtrans);
		loadTrans.setOnClickListener(loadTransListener);
		trans = (TextView) view.findViewById(R.id.back_trans);
		trans.setText(_trans);
		String text = _example;
		int f1 = text.indexOf("~");
		int f2 = text.indexOf("~", f1 + 1);
		int f3 = text.indexOf("~", f2 + 1);
		int f4 = text.indexOf("~", f3 + 1);
		text = text.replaceAll("~", " ");
		SpannableString spannable = new SpannableString(text);
		// here we set the color
		spannable.setSpan(new StyleSpan(Typeface.BOLD), f1, f2, 0);
		spannable.setSpan(new ForegroundColorSpan(Color.BLUE), f1, f2, 0);
		spannable.setSpan(new StyleSpan(Typeface.BOLD), f3, f4, 0);
		spannable.setSpan(new ForegroundColorSpan(Color.BLUE), f3, f4, 0);
		example.setText(spannable);

		return view;
	}

	private View.OnClickListener loadTransListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			trans.setVisibility(View.VISIBLE);
			loadTrans.setVisibility(View.GONE);
		}
	};

	public void setWordBack(String word, String part1, String part2,
			String example, String trans) {
		this._word = word;
		this._part1 = part1;
		this._part2 = part2;
		this._example = example;
		this._trans = trans;
	}
}
