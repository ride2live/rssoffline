package ru.fallen.rssofflinefree;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NewsDatabase extends SQLiteOpenHelper {

	private static final String TAG = "NewsDatabase";
	public NewsDatabase(Context context) {

		super(context, "newsDB2", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL("CREATE TABLE feeds ('_id' INTEGER PRIMARY KEY AUTOINCREMENT, 'title' TEXT, 'url' TEXT, 'imagepath_feed' TEXT, 'rssurl' TEXT);");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			db.execSQL("CREATE TABLE news ('_id'  INTEGER PRIMARY KEY AUTOINCREMENT, 'title' TEXT, 'link' TEXT, 'body' TEXT, 'image_path' TEXT, 'rssurl' TEXT, 'image_url' TEXT,  'date' INTEGER);");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			//db.execSQL("INSERT INTO Destination SELECT * FROM Source");//put temp to news
			db.execSQL("CREATE TABLE news_temp ('_id'  INTEGER PRIMARY KEY AUTOINCREMENT, 'title' TEXT, 'link' TEXT, 'body' TEXT, 'image_path' TEXT, 'rssurl' TEXT, 'image_url' TEXT,  'date' INTEGER);");
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	 public boolean putFeeds (NewsFeed newsFeed)
	 {
		 //if urlKeyExists - just update row, else - create new 
		 boolean added = false;
		 Utils.showLogV(TAG, "put titl " +newsFeed.getTitle());
		 Utils.showLogV(TAG, "put imageurl " +newsFeed.getImageUrl());
		 Utils.showLogV(TAG, "put url " +newsFeed.getUrl());
		 Utils.showLogV(TAG, "put rssurl " +newsFeed.getRssUrl());
		 SQLiteDatabase db = getWritableDatabase();
		 //Cursor c = db.query("feeds", new String [] {"rssurl"}, null, null, null, null, null);
		 ContentValues values = new ContentValues();
		 values.put("title", newsFeed.getTitle());
		 values.put("url", newsFeed.getUrl());
		 values.put("imagepath_feed", newsFeed.getImageUrl());
		 int rowsUpdated  = db.update("feeds", values, "rssurl = '" + newsFeed.getRssUrl() +"'", null);
		 if (rowsUpdated == 0)
		 {
			 values.put("rssurl", newsFeed.getRssUrl());
			 long id_key =  db.insert("feeds", null, values);
			 Utils.showLogV(TAG, "Новый id " +id_key);
		 }
		 Utils.showLogV(TAG, "Обновлено ячеек " +rowsUpdated);
		 newsFeed.getTitle();
		 return added;
	 }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public NewsFeed getFeed(String urlKey) {
		
		// TODO Auto-generated method stub
		NewsFeed newsFeed  = new NewsFeed(); 
		Cursor c = getReadableDatabase().query("feeds", null, "rssurl = '"+urlKey+"'", null, null, null, null);
		int k =0;
	
		/*while (c.moveToNext()) {
			
			System.out.println(k+ "-ая строка");
			k++;
			for (int i = 0; i < c.getColumnCount(); i++) {
				try {
					if (c.getString(i) == null)
						System.out.println("col name " + c.getColumnName(i) + "null");
					else
						System.out.println("col name " + c.getColumnName(i)+ c.getString(i));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
			
		}*/
		if (c!=null && c.getCount() == 1)
			c.moveToFirst();
		else
		{
			//System.out.println("Stupid fail!!! cursor count null or < > 1");
			return null;
		}
		
		newsFeed.setTitle(c.getString(c.getColumnIndex("title")));
		newsFeed.setImageUrl(c.getString(c.getColumnIndex("imagepath_feed")));
		newsFeed.setRssUrl(c.getString(c.getColumnIndex("rssurl")));
		newsFeed.setUrl(c.getString(c.getColumnIndex("url")));
		c.close();
		return newsFeed;
	}

	public int getFeedCount() {
		// TODO Auto-generated method stub
		Cursor c = getReadableDatabase().query("feeds", null, null, null, null, null, null);
		if (c == null)
		{
			//System.out.println("getFeedCount == -1");
			return -1;
		}
		int count = c.getCount();
		c.close();
		//System.out.println("getFeedCount == "+ count );
		return count;
	}

	public String[] getFeedsUrls() {
		// TODO Auto-generated method stub
		Cursor c = getReadableDatabase().query("feeds", null, null, null, null, null, null);
		if (c == null)
		{
			//System.out.println("getFeedsUrls ==null");
			return null;
		}
		else
		{
			//System.out.println("cursor count " + c.getCount());
		}
			
		String [] urls = new String [c.getCount()]; 
		for (int i = 0; i < urls.length; i++) {
			c.moveToPosition(i);
			urls [i] = c.getString(c.getColumnIndex("rssurl"));
			//System.out.println("urls [i]");
		}
		//int count = c.getCount();
		//c.close();
		//System.out.println("getFeeds == ok " );
		return urls;
	}

	public void putRssItem(NewsItem newsItem) {
		// TODO Auto-generated method stub
		 //System.out.println ("put titl " +newsItem.getTitle());
		 
		// System.out.println ("put date " +newsItem.getDate());
		 //System.out.println ("put url " +newsItem.getLink());
		// System.out.println ("put rssurl " +newsItem.description);
		 //System.out.println ("put rssurl " +newsItem.getRssurl());
		 SQLiteDatabase db = getWritableDatabase();
		// db.query("feeds", null, null, null, null, null, null);
	 //"CREATE TABLE news ( _ID INTEGER PRIMARY KEY AUTOINCREMENT, 'title' TEXT, 'link' TEXT, 'body' TEXT, 'imagepath_item' TEXT, 'rssurl' TEXT);");
		 ContentValues values = new ContentValues();
		 values.put("title", newsItem.getTitle());
		 values.put("date", newsItem.getDate());
		 values.put("link", newsItem.getLink());
		 values.put("body", newsItem.getDescription());
		 values.put("image_url", newsItem.getImageUrl());
		 values.put("image_path", newsItem.getImagePath());
		 values.put("rssurl", newsItem.getRssurl());
		 db.insert("news", null, values);
		 
	}

	/*public ArrayList<NewsItem> getNews(String urlKey) {
		// TODO Auto-generated method stub
		Cursor c = getReadableDatabase().query("news", null, "rssurl = '"+urlKey+"'", null, null, null, "date ASC");
		if (c !=null && c.getCount()<0)
			return null;
		//System.out.println (c.getCount());
		ArrayList<NewsItem> allItemsByUrl = new ArrayList<NewsItem>();
		while (c.moveToNext())
		{
			NewsItem newsItem = new NewsItem();
			System.out.println ();
			newsItem.setTitle(c.getString(c.getColumnIndex(Parameters.NEWS_TITLE)));
			newsItem.setDescription(c.getString(c.getColumnIndex(Parameters.NEWS_BODY)));
			newsItem.setImageUrl(c.getString(c.getColumnIndex(Parameters.NEWS_IMAGEPATH)));
			newsItem.setLink(c.getString(c.getColumnIndex(Parameters.NEWS_LINK)));
			newsItem.setDate(c.getLong(c.getColumnIndex(Parameters.NEWS_DATE)));
			allItemsByUrl.add(newsItem);
		}
		return allItemsByUrl;
	}*/
	public int deleteRowsByUrl (String url)
	{
		return getWritableDatabase().delete("news",  "rssurl = '" + url +"'", null);
	}

	public Cursor getImageUrls(String urlKey) {
		// TODO Auto-generated method bstub
		return getReadableDatabase().query(true, "news", new String [] {Parameters.NEWS_IMAGEURL} , "rssurl = '" + urlKey +"' AND " + Parameters.NEWS_IMAGEPATH +" = 'no'", null, null, null, null, null);
		
	}

	public Cursor getNewsCursor(String urlKey) {
		// TODO Auto-generated method stub
		Cursor c = getReadableDatabase().query("news", null, "rssurl = '"+urlKey+"'", null, null, null, "date DESC");
		//System.out.println (c.getColumnIndex("_ID"));
		return c;
	}

	public void putCashedImage(String imageUrl, String absolutePath) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(Parameters.NEWS_IMAGEPATH, absolutePath);
		//System.out.println (imageUrl + " Put to base "+ absolutePath);
		getWritableDatabase().update("news", cv, Parameters.NEWS_IMAGEURL + " = '"+imageUrl+"'" , null);
	}

	public void putRssItemToTemp(NewsItem newsItem) {
		// TODO Auto-generated method stub
		 ContentValues values = new ContentValues();
		 values.put("title", newsItem.getTitle());
		 values.put("date", newsItem.getDate());
		 values.put("link", newsItem.getLink());
		 values.put("body", newsItem.getDescription());
		 values.put("image_url", newsItem.getImageUrl());
		 values.put("image_path", newsItem.getImagePath());
		 values.put("rssurl", newsItem.getRssurl());
		 getWritableDatabase().insert("news_temp", null, values);
	}

	public void refreshNewsFromTempTable(String updatingUrlFeed) {
		// TODO Auto-generated method stub
		putKnowableImagePaths ();
		SQLiteDatabase db = getWritableDatabase();
		try {
			if (updatingUrlFeed.equals("all"))
			{
				db.delete("news", null, null);
			}
			else
			{
				db.delete("news", "rssurl = '" + updatingUrlFeed +"'", null);
			}
			
			db.execSQL("INSERT INTO 'news' SELECT * FROM 'news_temp';");
			db.delete("news_temp", null, null);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void putKnowableImagePaths() {
		// TODO Auto-generated method stub
		SQLiteDatabase db = getWritableDatabase();
		Cursor emptyImagePathsCursor = db.query(true, "news_temp", new String [] {Parameters.NEWS_IMAGEURL}, null, null, null, 
				null, null, null);
		while (emptyImagePathsCursor.moveToNext())
		{
			String currentPath = emptyImagePathsCursor.getString(0);
			Cursor foundPath = db.query(true, "news", new String [] {Parameters.NEWS_IMAGEPATH}, 
					Parameters.NEWS_IMAGEURL + "= '"+ currentPath + "'", null, null, null, null, null);
			if (foundPath == null || foundPath.getCount()<1)
				return;
			foundPath.moveToFirst();
			ContentValues cv = new ContentValues();
			//System.out.println ("catched previos path " +foundPath.getString(0));
			cv.put(Parameters.NEWS_IMAGEPATH, foundPath.getString(0));
			//cv.put(currentPath, foundPath.getString(0));
			db.update("news_temp", cv, Parameters.NEWS_IMAGEURL + "= '"+ currentPath + "'", null);
			
		}
		
		
	}
	
}
