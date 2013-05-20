package ru.fallen.rssofflinefree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.os.AsyncTask;
import android.text.Html;
import android.text.format.DateFormat;

public class AsyncLoadNews extends AsyncTask<Void, Boolean, String> {
	onLoadCallback lcb;
	onLoadImagesCallBack olic;
	NewsDatabase nd;
	String [] urls;
	SimpleDateFormat formatter;
	private boolean isImageLoadNeeded;
	public AsyncLoadNews(onLoadImagesCallBack olic, onLoadCallback lcb, NewsDatabase nd, String [] urls, boolean isImageLoadNeeded) {
		// TODO Auto-generated constructor stub
		this.lcb = lcb;
		this.olic = olic;
		this.nd = nd;
		this.isImageLoadNeeded =isImageLoadNeeded;
		this.urls = urls;
	}
	@Override
	protected String doInBackground(Void... params) {
		// TODO Auto-generated method stub
		formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		for (int i = 0; i < urls.length; i++) {
			dwonloadRss (urls[i]);
		}
		
		if(urls.length == 1)
		{
		 	nd.refreshNewsFromTempTable (urls[0]);
		 	publishProgress(true);
		 	return urls[0];
		}
		else
		{
			nd.refreshNewsFromTempTable ("all");
			publishProgress(true);
			return Parameters.ALL_REFRESH;
		}
	}
	
	@Override
	protected void onProgressUpdate(Boolean... values) {
		// TODO Auto-generated method stub
		olic.imageDownloaded();
		super.onProgressUpdate(values);
	}
	
	@SuppressWarnings("deprecation")
	private void dwonloadRss(String urlString) {
		// TODO Auto-generated method stub
		try {
			
			XmlPullParserFactory factory;
			URL url = new URL (urlString);
			URLConnection uCon = url.openConnection();
			InputStream is = uCon.getInputStream();
			factory = XmlPullParserFactory.newInstance();
			 //factory.setNamespaceAware(true);
		     XmlPullParser xpp = factory.newPullParser();
		     xpp.setInput(is, null);
		     int parserCode;
		     boolean imageTagOpened = false;
		     boolean urlTagOpened = false;
		     boolean descrTagOpened = false;
	    	 boolean enclosureTagOpened = false;
			 boolean itemTagOpened = false;
			 boolean linkTagOpened = false;
			 boolean titleTagOpened = false;
			 boolean pubDateTagOpened = false;
			 NewsFeed newsFeed = new NewsFeed();
			 newsFeed.rssUrl = urlString;
			 NewsItem newsItem = null;
			 //System.out.println ("deleted rows = " +nd.deleteRowsByUrl(urlString));
		     while ((parserCode = xpp.next()) != XmlPullParser.END_DOCUMENT)
		     {
		    	 if (xpp.getName()!=null )
	    		 {

					if (xpp.getEventType() == XmlPullParser.START_TAG)
			    	 {
			    		 if (xpp.getName().equals(Parameters.IMAGE_TAG))
			    			 imageTagOpened = true;
			    		if (xpp.getName().equals(Parameters.TITLE_IMAGE_URL_TAG))
			    			 urlTagOpened = true;
						if (xpp.getName().equals(Parameters.DESCRIPTION_TAG))
			    			 descrTagOpened = true;
						if (itemTagOpened)
						{
							
							int attrCount = xpp.getAttributeCount();
							if (attrCount > 0)
									{
								for (int i = 0; i < attrCount; i++) {
									if (xpp.getAttributeName(i).equals("url")) {
										//System.out.println("attr name "+ xpp.getAttributeName(i));
										if (newsItem.getImageUrl().equals(""))
										{
										/*String pathImage = downloadImageToCash (xpp.getAttributeValue(i));
										if (pathImage !=null && !pathImage.equals(""))
										{
										newsItem.setImageUrl(pathImage);
											//System.out.println("Put news image " + pathImage );
											
										}*/
										//System.out.println("Фотка новости "
											//	+ xpp.getAttributeValue(i));
										newsItem.setImageUrl(xpp.getAttributeValue(i));
										}
									}

								}

							}
						}
			    			 enclosureTagOpened = true;
						if (xpp.getName().equals(Parameters.ITEM_TAG))
						{
							newsItem = new NewsItem();
			    			 itemTagOpened = true;
						}
						if (xpp.getName().equals(Parameters.LINK_TAG))
			    			 linkTagOpened = true;
						if (xpp.getName().equals(Parameters.TITLE_TAG))
			    			 titleTagOpened = true;
						if (xpp.getName().equals(Parameters.DATE_TAG))
							pubDateTagOpened = true;
			    	 }
			    	 if (xpp.getEventType() == XmlPullParser.END_TAG)
			    	 {
						if (xpp.getName().equals(Parameters.IMAGE_TAG)) // News Feed there should be done
						{
							imageTagOpened = false;
						}
						if (xpp.getName().equals(Parameters.TITLE_IMAGE_URL_TAG))
							urlTagOpened = false;
						if (xpp.getName().equals(Parameters.DESCRIPTION_TAG))
							descrTagOpened = false;
						if (xpp.getName().equals(Parameters.ENCLOSURE_TAG))
			    			 enclosureTagOpened = false;
						if (xpp.getName().equals(Parameters.ITEM_TAG))
						{
							newsItem.setRssurl(urlString);
							 nd.putRssItemToTemp(newsItem);
			    			 itemTagOpened = false;
						}
						if (xpp.getName().equals(Parameters.LINK_TAG))
			    			 linkTagOpened = false;
						if (xpp.getName().equals(Parameters.TITLE_TAG))
			    			 titleTagOpened = false;
						if (xpp.getName().equals(Parameters.TITLE_TAG))
			    			 titleTagOpened = false;
						if (xpp.getName().equals(Parameters.DATE_TAG))
			    			 pubDateTagOpened = false;
			    		 
			    	 }
	    		 }
		    	 
		    	/* if (xpp.getName()!=null && xpp.getName().equals(Parameters.TITLE_IMAGE_URL_TAG) && imageTagOpened)
		    	 {
		    		 System.out.println (xpp.getName());
		    		 if (xpp.getText()!=null)
		    			 System.out.println (xpp.getText());
		    	 }*/
		    	 if (parserCode == XmlPullParser.TEXT)
		    	 {
		    		 String text = xpp.getText();
			    	 if (imageTagOpened) 
			    	 {
			    		 if (urlTagOpened)// feed logo
			    		 {
			    			 
			    			 String newsFeedImage = downloadImageToCash (text);
			    			 if (newsFeedImage !=null)
			    				 newsFeed.setImageUrl(newsFeedImage);
			    		 }
			    		 	 
			    		 
			    		/* if (titleTagOpened)
			    		 {
			    			 newsFeed.setTitle(text);
			    			 System.out.println ("Титул " + text);
			    		 }
			    			
			    		 
			    		 if (linkTagOpened)
			    		 {
			    			 newsFeed.setUrl(text);
			    			 System.out.println ("Ссыль " + text);
			    		 }*/
			    	 }

			    	 else
			    	 {
			    		 if (titleTagOpened && !itemTagOpened)
			    		 {
			    			 
			    			 newsFeed.setTitle(text);
			    			// System.out.println ("Титул " + text);
			    		 }
			    			
			    		 
			    		 if (linkTagOpened)
			    		 {
			    			 newsFeed.setUrl(text);
			    			// System.out.println ("Ссыль " + text);
			    		 }
			    			 
			    	 }
			    	 if (itemTagOpened )
			    	 {
			    		 if (descrTagOpened)
			    		 {
			    			// System.out.println ("Тело  " + text);
			    			 text = text.replaceAll("<img (.+?)>", "");
			    				
			    				
			    				String formated = Html.fromHtml(text, null   , null).toString();
			    				formated = formated.replaceAll("\n\n", "");
			    			 newsItem.setDescription(formated);
			    		 }
			    		 if (linkTagOpened)
			    		 {
			    			 newsItem.setLink(text);
			    			 //System.out.println ("Ссыль на новость  " + text);
			    		 }
			    		 if (titleTagOpened)
			    		 {
			    			 newsItem.setTitle(text);
			    			// System.out.println ("Заголовок новости  " + text);
			    		 }
			    		 if (pubDateTagOpened)
			    		 {
			    			 
			    			 Date date;
							try {
								date = formatter.parse(text);
								//System.out.println (date.toGMTString());
								newsItem.setDate (date.getTime());
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			    			 
			    		 }
			    	 }
		    	
		    	 }
		    	 
		     }
		     nd.putFeeds(newsFeed); 
		     
		     
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String downloadImageToCash(String attributeValue) {
		// TODO Auto-generated method stub
		File file = null;
		try {
			URL url = new URL(attributeValue);

			InputStream is = url.openStream();
			byte [] buffer = new byte [1024];
			File tempPath = new File(Parameters.TEMP);
			if (!tempPath.exists())
				tempPath.mkdirs();
			String extension = url.getFile();
			int point = extension.lastIndexOf(".");
			if (point ==-1)
				extension = "";
			else
				extension = extension.substring(point);	
			file = new File (Parameters.TEMP + String.valueOf(Math.round(Math.random() * 100000)) + extension);
			while (file.exists())
			{
				file = new File (Parameters.TEMP + String.valueOf(Math.round(Math.random() * 100000)) + extension);
			}
			OutputStream os = new FileOutputStream(file);
			int l = 0;  
			while ((l =is.read(buffer)) != -1)
			{
				os.write(buffer, 0, l);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			file = null;
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			file = null;
			e.printStackTrace();
			
		}
		
		if (file !=null)
			return file.getAbsolutePath();
		else
			return null;
	}

	@Override
	protected void onPostExecute(String urlKey) {
		// TODO Auto-generated method stub
		lcb.onFinishLoading(urlKey);
		
		if (isImageLoadNeeded)
		{
			AsyncLoadImages ali = new AsyncLoadImages(urls, olic);
			ali.execute(nd);
		}
		
		super.onPostExecute(urlKey);

	}
	

}
