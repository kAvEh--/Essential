package com.glass.essentialwords;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class HelpFragment extends Fragment {

	int _page;
	ImageView _image;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_help, container, false);

		_image = (ImageView) view.findViewById(R.id.help_main);
		switch (_page) {
		case 2:
			_image.setImageResource(R.drawable.help2);
			break;

		case 3:
			_image.setImageResource(R.drawable.help3);
			break;

		case 4:
			_image.setImageResource(R.drawable.help4);
			break;

		default:
			_image.setImageResource(R.drawable.help1);
			break;
		}
		return view;
	}

	public void setPage(int p) {
		this._page = p;
	}

}