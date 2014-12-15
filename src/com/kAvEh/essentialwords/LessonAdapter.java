package com.kAvEh.essentialwords;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LessonAdapter extends BaseAdapter {

	private Activity activity;
	private String[][] word_lists;

	public LessonAdapter(Activity a, String[][] b) {
		activity = a;
		word_lists = b;
	}

	public int getCount() {
		return 30;
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
			// viewHolder.header.setOnClickListener(mHeaderClickListener);
			vi.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) vi.getTag();
		}
		viewHolder.lessonTitle.setText("Lesson    " + (position + 1));
//		viewHolder.ex_1.setText(word_lists[position][0]);
//		viewHolder.ex_2.setText(word_lists[position][1]);
//		viewHolder.ex_3.setText(word_lists[position][2]);

		viewHolder.excer.setTag(position);

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
}