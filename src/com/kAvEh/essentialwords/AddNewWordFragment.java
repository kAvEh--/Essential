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

public class AddNewWordFragment extends DialogFragment {

	EditText word;
	EditText desc;
	EditText trans;
	int lesson;

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

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(vi)
				// Add action buttons
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {
						DatabaseHandler db = new DatabaseHandler(
								getActivity());
						lesson = db.getLessonToAdd();
						Word w = new Word(0, lesson, word.getText().toString(),
								desc.getText().toString(), "", "", trans
										.getText().toString(), 0, 0, 0);
						db.addWord(w);
						db.close();
						Toast.makeText(getActivity(), word.getText().toString() + " added to lesson " + lesson, 
								   Toast.LENGTH_LONG).show();
						AddNewWordFragment.this.getDialog().cancel();
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								AddNewWordFragment.this.getDialog().cancel();
							}
						});
		return builder.create();
	}
}