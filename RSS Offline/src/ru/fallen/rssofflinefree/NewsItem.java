package ru.fallen.rssofflinefree;

import android.database.Cursor;

public class NewsItem {
	private String title = "";
	private String link= "";
	private String description = "";
	private String imageUrl = "";
	private String rssurl ="";
	private long date = -1;
	private String imagePath = "no";
	public NewsItem ()
	{
		
	}
	public NewsItem (Cursor c) //cursor should be in correct position 
	{
		setTitle(c.getString(c.getColumnIndex(Parameters.NEWS_TITLE)));
		setDescription(c.getString(c.getColumnIndex(Parameters.NEWS_BODY)));
		setImageUrl(c.getString(c.getColumnIndex(Parameters.NEWS_IMAGEURL)));
		setLink(c.getString(c.getColumnIndex(Parameters.NEWS_LINK)));
		setDate(c.getLong(c.getColumnIndex(Parameters.NEWS_DATE)));
		setImagePath(c.getString(c.getColumnIndex(Parameters.NEWS_IMAGEPATH)));
	}
	public String getRssurl() {
		return rssurl;												
	}
	public void setRssurl(String rssurl) {
		this.rssurl = rssurl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public void setDate(long date) {
		// TODO Auto-generated method stub
		this.date = date;
	}
	public long getDate() {
		// TODO Auto-generated method stub
		return date ;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	
}
