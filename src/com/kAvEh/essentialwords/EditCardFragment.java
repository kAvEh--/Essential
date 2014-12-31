package com.kAvEh.essentialwords;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditCardFragment extends DialogFragment {

	EditText word;
	EditText desc;
	EditText trans;
	int lesson;
	boolean isLeitner = false;
	Word _word;

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View vi = inflater.inflate(R.layout.fragment_add_word, null);
		word = (EditText) vi.findViewById(R.id.add_word);
		desc = (EditText) vi.findViewById(R.id.add_desc);
		trans = (EditText) vi.findViewById(R.id.add_trans);
		word.setText(_word.getWord());
		desc.setText(_word.getPart2());
		trans.setText(_word.getTrans());

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(vi)
				// Add action buttons
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						DatabaseHandler db = new DatabaseHandler(getActivity());
						lesson = db.getLessonToAdd();
						Word tmp = new Word(_word.getID(), _word.getLesson(),
								word.getText().toString(), _word.getpart1(),
								desc.getText().toString(), _word.getExample(),
								trans.getText().toString(), _word.getStar(),
								_word.getLeitnerStage(), _word.getLeitnerPart());
						db.editWord(tmp);
						db.close();
						Toast.makeText(getActivity(),
								word.getText().toString() + " edited.",
								Toast.LENGTH_LONG).show();
						((LessonActivity) getActivity()).updateList();
						EditCardFragment.this.getDialog().cancel();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								EditCardFragment.this.getDialog().cancel();
							}
						});
		return builder.create();
	}

	public void setFlag() {
		isLeitner = true;
	}

	public void setContent(Word w) {
		_word = w;
	}
}