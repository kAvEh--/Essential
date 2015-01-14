package com.glass.essentialwords;

import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LessonListAdapter extends BaseAdapter {
	private Context mContext;

	String[] word;
	int[] leitner_state;

	// Constructor
	public LessonListAdapter(Context c, String[] w, int[] l) {
		mContext = c;
		this.word = w;
		this.leitner_state = l;
	}

	@Override
	public int getCount() {
		return word.length;
	}

	@Override
	public Object getItem(int position) {
		return word[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout rl = new RelativeLayout(mContext);
		rl.setBackgroundResource(R.drawable.bg3);
		rl.setPadding(0, 0, 0, (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 20, mContext.getResources()
						.getDisplayMetrics()));
		TextView tv = new TextView(mContext);
		tv.setText(word[position]);
		tv.setTextSize(19);
		tv.setId(999);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

		ImageView im = new ImageView(mContext);
		switch (leitner_state[position]) {
		case 1:
			im.setImageResource(R.drawable.ic_leitner_0);
			break;
		case 2:
			im.setImageResource(R.drawable.ic_leitner_1);
			break;
		case 3:
			im.setImageResource(R.drawable.ic_leitner_2);
			break;
		case 4:
			im.setImageResource(R.drawable.ic_leitner_3);
			break;
		case 5:
			im.setImageResource(R.drawable.ic_leitner_4);
			break;
		case 6:
			im.setImageResource(R.drawable.ic_leitner_5);
			break;

		default:
			break;
		}

		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						40, mContext.getResources().getDisplayMetrics()),
				(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						30, mContext.getResources().getDisplayMetrics()));
		lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		lp2.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		im.setLayoutParams(lp2);

		rl.addView(tv, lp);
		rl.addView(im);
		return rl;
	}

}