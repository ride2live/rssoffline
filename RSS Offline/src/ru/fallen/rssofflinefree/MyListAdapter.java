package ru.fallen.rssofflinefree;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter implements DecodeCallBack{
	ArrayList<NewsItem> newsItemsList;
	ArrayList <Integer> ids;
	ArrayList <String> objects;
	Context mContext;
	
	public MyListAdapter(ArrayList<NewsItem> newsItemsList, Context context) {
		// TODO Auto-generated constructor stub
		objects = new ArrayList<String>();
		ids =  new ArrayList<Integer>();
		this.newsItemsList = newsItemsList;
		mContext = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsItemsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return newsItemsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//View v=convertView;
		LayoutInflater li = LayoutInflater.from(mContext);
		if (position ==0)
		{
			return li.inflate(R.layout.top_element_list, null);
		}
		NewsItem curNews = newsItemsList.get(position-1);
		/*if (convertView == null)
		{
		LayoutInflater li = LayoutInflater.from(mContext);
    	View v = li.inflate(R.layout.listview_item, parent,false);
			System.out.println ("convert == null");
			
        }
		else
			System.out.println ("convert != null");*/
		//final ImageView delImage = (ImageView)v.findViewById(R.id.deleteStation);
		
    	View v = li.inflate(R.layout.listview_item, parent,false);
		TextView bodyView = (TextView)v.findViewById(R.id.body);
		
		ImageView newsImage = (ImageView)v.findViewById(R.id.newsImage);
		
		//System.out.println (newsImage.toString());
		//System.out.println (curNews.getImageUrl());
		File imgFile = new  File(curNews.getImageUrl());
		if(imgFile !=null && imgFile.exists()){
			//System.out.println (newsImage.toString());
			/*int prevObjIndex  = objects.indexOf(newsImage.toString());
			int generatedRandom = (int)Math.round(Math.random()*1000);
			if (prevObjIndex == -1)
			{
				
				objects.add(newsImage.toString());
				ids.add(generatedRandom);
			}
			else
			{
				ids.set(prevObjIndex, generatedRandom);
			}*/
			//AsyncDecodeImage adi = new AsyncDecodeImage(this, generatedRandom, newsImage);
			//AsyncDecodeImage adi = new AsyncDecodeImage(this, newsImage);
			//adi.execute(imgFile);
		}
		else
		{
			newsImage.setVisibility(View.GONE);
		}
		TextView titleView = (TextView)v.findViewById(R.id.newsTitle);
		String title = newsItemsList.get(position).getTitle();
		String body = newsItemsList.get(position).getDescription();
		bodyView.setText(body + "\n" +new Date (newsItemsList.get(position).getDate()).toGMTString());
		titleView.setText(title);
        return v;
	}

	@Override
	public void onDecoded(Bitmap bitmap, int pseudoUniqFromAT, ImageView newsImage) {
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

	

}
