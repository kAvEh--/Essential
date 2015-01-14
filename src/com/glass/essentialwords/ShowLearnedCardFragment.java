package com.glass.essentialwords;

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

public class ShowLearnedCardFragment extends DialogFragment {

	ArrayList<Word> words;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);
		View rootView = inflater.inflate(R.layout.fragment_show_cards,
				container, false);
		TextView title = (TextView) rootView.findViewById(R.id.dialog_title);
		title.setText("Words Learned by Leitner");
		DatabaseHandler db = new DatabaseHandler(getActivity());
		words = db.getLearnedWords();
		db.close();
		String[] tmp = new String[words.size()];
		int[] leitner_stat = new int[words.size()];
		for (int i = 0; i < words.size(); i++) {
			tmp[i] = words.get(i).getWord();
			leitner_stat[i] = words.get(i).getLeitnerStage();
		}
		GridView grid = (GridView) rootView.findViewById(R.id.grid_view);
		grid.setAdapter(new LessonListAdapter(getActivity(), tmp, leitner_stat));
		grid.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	        	Intent i = new Intent(getActivity(), LessonActivity.class);
	        	Word _w = words.get(position);
				i.putExtra("Num", _w.getLesson());
				i.putExtra("WordID", _w.getID());
				startActivity(i);
	        }
	    });
		return rootView;
	}
	
	@Override
	public void onDestroyView() {
	  if (getDialog() != null && getRetainInstance())
	    getDialog().setDismissMessage(null);
	  super.onDestroyView();
	}
}