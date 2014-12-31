package com.kAvEh.essentialwords;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LessonAdapter extends BaseAdapter {

	private FragmentActivity activity;
	private String[][] word_lists;
	private int[][] word_data;
	private int _count;

	public LessonAdapter(FragmentActivity a, String[][] b, int[][] l, int c) {
		activity = a;
		word_lists = b;
		word_data = l;
		_count = c;
	}

	public void setListData(String[][] b) {
		this.word_lists = b;
	}

	public void setListNum(int[][] b) {
		this.word_data = b;
	}

	public int getCount() {
		return _count;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView lessonTitle;
		Button excer;
		RelativeLayout header;
		TextView ex_1;
		TextView ex_2;
		TextView ex_3;
		TextView l_1;
		TextView l_2;
		TextView l_3;
		TextView l_4;
		TextView l_5;
		RelativeLayout more;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			vi = inflater.inflate(R.layout.lessons_data, null);
			viewHolder = new ViewHolder();
			viewHolder.lessonTitle = (TextView) vi
					.findViewById(R.id.list_heder_title);

			viewHolder.excer = (Button) vi.findViewById(R.id.lesson_excer);
			viewHolder.excer.setOnClickListener(mExcerClickListener);

			viewHolder.header = (RelativeLayout) vi
					.findViewById(R.id.lesson_list_header);
			viewHolder.ex_1 = (TextView) vi.findViewById(R.id.list_example_1);
			viewHolder.ex_2 = (TextView) vi.findViewById(R.id.list_example_2);
			viewHolder.ex_3 = (TextView) vi.findViewById(R.id.list_example_3);
			viewHolder.l_1 = (TextView) vi.findViewById(R.id.list_leitner_1);
			viewHolder.l_2 = (TextView) vi.findViewById(R.id.list_leitner_2);
			viewHolder.l_3 = (TextView) vi.findViewById(R.id.list_leitner_3);
			viewHolder.l_4 = (TextView) vi.findViewById(R.id.list_leitner_4);
			viewHolder.l_5 = (TextView) vi.findViewById(R.id.list_leitner_5);
			viewHolder.header.setOnClickListener(mHeaderClickListener);
			viewHolder.more = (RelativeLayout) vi
					.findViewById(R.id.lesson_list_body);
			viewHolder.more.setOnClickListener(moreClickListener);
			vi.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) vi.getTag();
		}
		viewHolder.lessonTitle.setText("Lesson " + (position + 1));
		viewHolder.ex_1.setText(word_lists[position][0]);
		viewHolder.ex_2.setText(word_lists[position][1]);
		viewHolder.ex_3.setText(word_lists[position][2]);

		if (word_data[position][0] > 0)
			viewHolder.l_1.setText(word_data[position][0] + "");
		else
			viewHolder.l_1.setText("-");
		if (word_data[position][1] > 0)
			viewHolder.l_2.setText(word_data[position][1] + "");
		else
			viewHolder.l_2.setText("-");
		if (word_data[position][2] > 0)
			viewHolder.l_3.setText(word_data[position][2] + "");
		else
			viewHolder.l_3.setText("-");
		if (word_data[position][3] > 0)
			viewHolder.l_4.setText(word_data[position][3] + "");
		else
			viewHolder.l_4.setText("-");
		if (word_data[position][4] > 0)
			viewHolder.l_5.setText(word_data[position][4] + "");
		else
			viewHolder.l_5.setText("-");

		if (position >= 30)
			viewHolder.excer.setVisibility(View.INVISIBLE);
		else
			viewHolder.excer.setVisibility(View.VISIBLE);
		
		viewHolder.excer.setTag(position);
		viewHolder.header.setTag(position);
		viewHolder.more.setTag(position);

		return vi;
	}

	private View.OnClickListener mExcerClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			Intent i = new Intent(activity, ExcerciseActivity.class);
			i.putExtra("Num", position);
			activity.startActivity(i);
		}
	};

	private View.OnClickListener mHeaderClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			Intent i = new Intent(activity, LessonActivity.class);
			i.putExtra("Num", position + 1);
			activity.startActivity(i);
		}
	};

	private View.OnClickListener moreClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			ShowCardFragment fr = new ShowCardFragment();
			fr.setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);
			fr.setlesson(position + 1);
			fr.show(activity.getSupportFragmentManager(), "Hello");
		}
	};

}