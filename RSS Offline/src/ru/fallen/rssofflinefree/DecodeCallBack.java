package ru.fallen.rssofflinefree;

import android.graphics.Bitmap;
import android.widget.ImageView;

public interface DecodeCallBack {
	void onDecoded (Bitmap bitmap, int psedoUniqRand, ImageView newsImage);
}
