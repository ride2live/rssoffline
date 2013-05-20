package ru.fallen.rssofflinefree;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsCursorAdapter extends CursorAdapter implements DecodeCallBack{
private boolean topElementReached = false;
private boolean isScrolling;
private Animation a;
private ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
private ArrayList<String> tagList = new ArrayList<String>();
private ArrayList<String> oldTagList = new ArrayList<String>();
public void setScrolling(boolean isScrolling) {
	this.isScrolling = isScrolling;
}
	//Context mContext;
//Cursor c;
	public NewsCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, true);
		// TODO Auto-generated constructor stub
		//this.c = c;
		//mContext = context;
		a = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
		a.setDuration(100);
		a.setFillAfter(true);
	}
	@Override
		public Cursor getCursor() {
			// TODO Auto-generated method stub
			return super.getCursor();
		}
	
	@Override
		public void changeCursor(Cursor cursor) {
			// TODO Auto-generated method stub
			super.changeCursor(cursor);
		}
	
	@Override
	public void bindView(View v, Context context, Cursor cursor) {
		// TODO Auto-generated method stub
		TextView bodyView = (TextView)v.findViewById(R.id.body);
		ImageView newsImage = (ImageView)v.findViewById(R.id.newsImage);
		TextView titleView = (TextView)v.findViewById(R.id.newsTitle);
		//int position = cursor.getPosition();  //ugly hack for  updating real images
		NewsItem newsItem = new NewsItem(cursor);
		bodyView.setText(newsItem.getDescription() + "\n" +new Date (newsItem.getDate()).toGMTString());
		titleView.setText(newsItem.getTitle() + " "+ newsItem.getImagePath());
		String imagePath = newsItem.getImagePath();
		File file = new File (imagePath);
		//int pseudoRand = (int)Math.round(Math.random()*10000);
		//newsImage.setTag(pseudoRand);
		newsImage.setImageResource(android.R.color.holo_blue_dark);
		if (file.exists() && file.isFile())
		{
			//newsImage.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
			//newsImage.setTag(file.getAbsolutePath());
			newsImage.setVisibility(View.VISIBLE);
			Bitmap myBitmap = BitmapFactory.decodeFile((file.getAbsolutePath()));
			if (myBitmap!=null)
				newsImage.setImageBitmap(myBitmap);
			//animateImageView(visibleImageView , absoluteFilePathImageTag);
			/*if (isScrolling == false)
			{
				
				//AsyncDecodeImage adi = new AsyncDecodeImage(this, newsImage);
				//adi.execute(file);
			}
			else
			{
				
			}*/
		}
		else
		{
			newsImage.setVisibility(View.GONE);
			newsImage.setTag(null);
			
		}
		
		
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		// TODO Auto-generated method stub
		//decodeImagesAndPutInView();
		LayoutInflater li = LayoutInflater.from(context);
		View v = li.inflate(R.layout.listview_item, parent,false);
		imageViewList.add((ImageView) v.findViewById(R.id.newsImage));
		return v;
	}

	@Override
	public void onDecoded(Bitmap bitmap, int psedoUniqRand, ImageView newsImage) {
		// TODO Auto-generated method stub
		if (bitmap !=null && newsImage!=null && newsImage.isShown())
	  	{
	  		//System.out.println (ids.size());
	  		newsImage.setVisibility(View.VISIBLE);
	  		newsImage.setImageBitmap(bitmap);
	  	}
	  	else
	  	{
	  		//newsImage.setImageResource(R.drawable.ic_launcher);
	  	}

	}
	/*public void decodeImagesAndPutInView ()
	{
		
		System.out.println ("decode AND PUT, size =" + imageViewList.size());
		for (String element : oldTagList) {
			System.out.println (element);
		}
		for (int i = 0; i < imageViewList.size(); i++) {
			
			ImageView visibleImageView = imageViewList.get(i);
			Object tag = visibleImageView.getTag();
			if (tag == null)
			{
				visibleImageView.setImageResource(android.R.color.holo_orange_dark);
				visibleImageView.setVisibility(View.GONE);
				System.out.println ("tag is null");
			}
			else
			{
					visibleImageView.setVisibility(View.VISIBLE);
					String absoluteFilePathImageTag = (String) tag;
					tagList.add(absoluteFilePathImageTag);

					if (oldTagList.contains(absoluteFilePathImageTag) == false)
					{
						//visibleImageView.setImageResource(android.R.color.transparent);
						Bitmap myBitmap = BitmapFactory.decodeFile((absoluteFilePathImageTag));
						visibleImageView.setImageBitmap(myBitmap);
						animateImageView(visibleImageView , absoluteFilePathImageTag);
					}
					else
					{
						System.out.println ("decoded already " + absoluteFilePathImageTag);
					}
			}
				//System.out.println ((String)tag);
			
		}
		oldTagList.clear();
		oldTagList.addAll (tagList);

		tagList.clear();
		
	}*/
	private void animateImageView(ImageView visibleImageView, String absoluteFilePathImageTag) {
		// TODO Auto-generated method stub
		System.out.println ("start animation " +absoluteFilePathImageTag );
		visibleImageView.startAnimation(a);
	}
	
	


}
