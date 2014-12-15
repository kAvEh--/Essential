package com.kAvEh.essentialwords;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<Word> mWords;
	private boolean[] flags;
	private boolean[] show;
	private TextToSpeech tts;

	public DetailAdapter(Activity a, ArrayList<Word> words, TextToSpeech t) {
		activity = a;
		mWords = words;
		tts = t;
		flags = new boolean[mWords.size()];
		Arrays.fill(flags, Boolean.FALSE);
		show = new boolean[mWords.size()];
		Arrays.fill(show, Boolean.FALSE);
	}

	public int getCount() {
		return mWords.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		TextView word;
		ImageView share;
		ImageView star;
		ImageView speek;
		LinearLayout word_layout;
		RelativeLayout trans_layout;
		LinearLayout show_know_word;
		ImageView prev_;
		TextView adv;
		TextView syn;
		RelativeLayout know_;
		RelativeLayout show_;
		TextView sentense;
		Button loadtrans;
		TextView trans;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder vh = null;
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			vi = inflater.inflate(R.layout.detail_data, null);
			vh = new ViewHolder();
			vh.word = (TextView) vi.findViewById(R.id.detail_header_word);
			vh.share = (ImageView) vi.findViewById(R.id.detail_header_share);
			vh.share.setOnClickListener(shareButtonClickListener);
			vh.star = (ImageView) vi.findViewById(R.id.detail_header_star);
			vh.star.setOnClickListener(starButtonClickListener);
			vh.speek = (ImageView) vi.findViewById(R.id.detail_header_speek);
			vh.speek.setOnClickListener(speekClickListener);
			vh.word_layout = (LinearLayout) vi
					.findViewById(R.id.detail_list_word);
			vh.trans_layout = (RelativeLayout) vi
					.findViewById(R.id.detail_list_trans);
			vh.show_know_word = (LinearLayout) vi
					.findViewById(R.id.show_know_word);
			vh.adv = (TextView) vi.findViewById(R.id.detail_word_adv);
			vh.prev_ = (ImageView) vi.findViewById(R.id.prev_answer);
			vh.know_ = (RelativeLayout) vi.findViewById(R.id.know_it);
			vh.show_ = (RelativeLayout) vi.findViewById(R.id.show_it);
			vh.know_.setOnClickListener(knowButtonClickListener);
			vh.show_.setOnClickListener(showButtonClickListener);
			vh.syn = (TextView) vi.findViewById(R.id.detail_word_syn);
			vh.sentense = (TextView) vi.findViewById(R.id.detail_word_sent);
			vh.loadtrans = (Button) vi
					.findViewById(R.id.detail_trans_loadtrans);
			vh.loadtrans.setOnClickListener(mMyButtonClickListener);
			vh.trans = (TextView) vi.findViewById(R.id.detail_word_trans);
			vi.setTag(vh);
		} else {
			vh = (ViewHolder) vi.getTag();
		}
		vh.word.setText(mWords.get(position).getWord());
		vh.adv.setText(mWords.get(position).getpart1());
		vh.syn.setText(mWords.get(position).getPart2());
		vh.trans.setText(mWords.get(position).getTrans());
		vh.loadtrans.setTag(position);
		vh.share.setTag(position);
		vh.star.setTag(position);
		vh.know_.setTag(position);
		vh.show_.setTag(position);
		vh.speek.setTag(position);
		if (flags[position]) {
			vh.trans.setVisibility(View.VISIBLE);
		} else {
			vh.trans.setVisibility(View.GONE);
		}
		if (show[position]) {
			vh.word_layout.setVisibility(View.VISIBLE);
			vh.trans_layout.setVisibility(View.VISIBLE);
			vh.show_know_word.setVisibility(View.GONE);
		} else {
			vh.word_layout.setVisibility(View.GONE);
			vh.trans_layout.setVisibility(View.GONE);
			vh.show_know_word.setVisibility(View.VISIBLE);
		}
		if (mWords.get(position).getState().equals("yes")) {
			vh.prev_.setVisibility(View.VISIBLE);
			vh.prev_.setBackgroundColor(activity.getResources().getColor(
					R.color.dark_green));
		} else if (mWords.get(position).getState().equals("no")) {
			vh.prev_.setBackgroundColor(activity.getResources().getColor(
					R.color.red));
			vh.prev_.setVisibility(View.VISIBLE);
		} else {
			vh.prev_.setVisibility(View.INVISIBLE);
		}
		if (mWords.get(position).getStar() == 0) {
			vh.star.setImageResource(R.drawable.ic_star_off);
		} else {
			vh.star.setImageResource(R.drawable.ic_star_2);
		}
		String text = mWords.get(position).getExample();
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
		vh.sentense.setText(spannable);
		return vi;
	}

	private View.OnClickListener mMyButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			if (flags[position])
				flags[position] = false;
			else
				flags[position] = true;
			notifyDataSetChanged();
		}
	};

	private View.OnClickListener speekClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			String text = mWords.get(position).getWord();
			tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
		}
	};

	private View.OnClickListener starButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			DatabaseHandler db = new DatabaseHandler(
					activity.getApplicationContext());
			if (mWords.get(position).getStar() == 0) {
				db.starWord(mWords.get(position).getID(), 1);
				mWords.get(position).setStar(1);
			} else {
				db.starWord(mWords.get(position).getID(), 0);
				mWords.get(position).setStar(0);
			}
			notifyDataSetChanged();
		}
	};

	private View.OnClickListener showButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			DatabaseHandler db = new DatabaseHandler(
					activity.getApplicationContext());
			db.showknowWord(mWords.get(position).getID(), "no");
			db.close();
			if (show[position])
				show[position] = false;
			else
				show[position] = true;
			notifyDataSetChanged();
		}
	};

	private View.OnClickListener knowButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			DatabaseHandler db = new DatabaseHandler(
					activity.getApplicationContext());
			db.showknowWord(mWords.get(position).getID(), "yes");
			db.close();
			if (show[position])
				show[position] = false;
			else
				show[position] = true;
			notifyDataSetChanged();
		}
	};

	private View.OnClickListener shareButtonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			Intent sharingIntent = new Intent(
					android.content.Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			String shareBody = "I`m Learning " + mWords.get(position).getWord()
					+ " via kAvEh&Nasrin`s App";
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
					"Learn new word with kAvEh&Nasrin");
			sharingIntent
					.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
			activity.startActivity(Intent.createChooser(sharingIntent,
					"Share via"));
		}
	};
}