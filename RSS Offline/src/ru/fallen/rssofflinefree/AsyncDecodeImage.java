package ru.fallen.rssofflinefree;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

public class AsyncDecodeImage extends AsyncTask<File, Void, Bitmap>{
	DecodeCallBack dcb;
	int psedoUniqRand;
	ImageView newsImage;
	public AsyncDecodeImage(DecodeCallBack dcb, ImageView newsImage) {
		// TODO Auto-generated constructor stub
		this.dcb = dcb;
		//this.psedoUniqRand = psedoUniqRand;
		this.newsImage = newsImage;
	}

	@Override
	protected Bitmap doInBackground(File... params) {
		// TODO Auto-generated method stub
		Bitmap myBitmap = BitmapFactory.decodeFile(params[0].getAbsolutePath());
		return myBitmap;
	}
	@Override
	protected void onPostExecute(Bitmap result) {
		// TODO Auto-generated method stub
		dcb.onDecoded(result, psedoUniqRand, newsImage);
		super.onPostExecute(result);
	}

}
