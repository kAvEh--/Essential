package com.kAvEh.essentialwords;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class ShowCardFragment extends DialogFragment {

	int lesson;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_show_cards,
				container, false);
		TextView title = (TextView) rootView.findViewById(R.id.dialog_title);
		title.setText("Lesson " + lesson);
		DatabaseHandler db = new DatabaseHandler(getActivity());
		ArrayList<Word> words = db.getLesson(lesson);
		db.close();
		String[] tmp = new String[words.size()];
		for (int i = 0; i < words.size(); i++) {
			tmp[i] = words.get(i).getWord();
		}
		GridView grid = (GridView) rootView.findViewById(R.id.grid_view);
		grid.setAdapter(new LessonListAdapter(getActivity(), tmp));
		grid.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Intent i = new Intent(getActivity(), LessonActivity.class);
				i.putExtra("Num", lesson);
				i.putExtra("Word", (position + 1));
				startActivity(i);
	        }
	    });
		return rootView;
	}

	public void setlesson(int l) {
		this.lesson = l;
	}
}