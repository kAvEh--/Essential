package com.kAvEh.essentialwords;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerMenuAdapter extends BaseAdapter {
	private Activity activity;

	private String[] titles;
	private int[] icons;

	// Constructor
	public DrawerMenuAdapter(Activity c, String[] t, int[] i) {
		activity = c;
		titles = t;
		icons = i;
	}

	@Override
	public int getCount() {
		return titles.length;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	static class ViewHolder {
		TextView menuTitle;
		ImageView menuIcon;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			vi = inflater.inflate(R.layout.drawer_menu_item, null);
			viewHolder = new ViewHolder();
			viewHolder.menuTitle = (TextView) vi.findViewById(R.id.menu_title);
			viewHolder.menuIcon = (ImageView) vi.findViewById(R.id.menu_image);

			vi.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) vi.getTag();
		}

		viewHolder.menuTitle.setText(titles[position]);
		viewHolder.menuIcon.setImageResource(icons[position]);

		return vi;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

}