package com.kAvEh.essentialwords;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FrontCardFragment extends Fragment {

	private String _word;
	private String _num;
	private String _stage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_card_front, container,
				false);
		TextView wordbox = (TextView) view.findViewById(R.id.card_word);
		wordbox.setText(this._word);
		TextView num = (TextView) view.findViewById(R.id.number_indicator);
		num.setText(this._num);
		TextView stage = (TextView) view.findViewById(R.id.lesson_indicator);
		stage.setText(this._stage);
		return view;
	}

	public void setWord(String word) {
		this._word = word;
	}

	public void setNumber(String num) {
		this._num = num;
	}

	public void setLesson(String stage) {
		this._stage = stage;
	}

}
