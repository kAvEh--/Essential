package com.kAvEh.essentialwords;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LessonListAdapter extends BaseAdapter {
    private Context mContext;
 
    String[] word;
 
    // Constructor
    public LessonListAdapter(Context c, String[] w){
        mContext = c;
        this.word = w;
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
        TextView tv = new TextView(mContext);
        tv.setText(word[position]);
        tv.setBackgroundResource(R.drawable.bgg);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(19);
        return tv;
    }
 
}