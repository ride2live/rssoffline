package ru.fallen.rssofflinefree;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Dictionary;

import android.database.Cursor;
import android.os.AsyncTask;

public class AsyncLoadImages extends AsyncTask<NewsDatabase, String, Boolean> {

	private String[] urls;
	private onLoadImagesCallBack olic;

	public AsyncLoadImages(String[] urls, onLoadImagesCallBack olic) {
		// TODO Auto-generated constructor stub
		this.urls = urls;
		this.olic = olic;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		//olic.imageDownloaded();
		super.onProgressUpdate(values);
	}
	@Override
	protected Boolean doInBackground(NewsDatabase... params) {
		// TODO Auto-generated method stub
		NewsDatabase nd = params[0];
		for (int i = 0; i < urls.length; i++) {
			startLoadImages(nd, urls [i]);
		}
	
		return true;
		
	}
	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		olic.imagesLoaded();
		super.onPostExecute(result);
	}
	public void startLoadImages (NewsDatabase nd, String urlKey)
	{
		Cursor c = nd.getImageUrls(urlKey);
		while (c.moveToNext()){
			URL url;
			//String currentPath = c.getString(c.getColumnIndex(Parameters.NEWS_IMAGEURL));
			String imageUrl = c.getString(0);
			try {
				url = new URL(imageUrl);
				URLConnection uCon = url.openConnection();
				uCon.setConnectTimeout(500);
				uCon.connect();
				InputStream is = uCon.getInputStream();
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
				File file = new File (Parameters.TEMP + String.valueOf(Math.round(Math.random() * 100000)) + extension);
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
				//System.out.println ("loading image " +imageUrl);
				nd.putCashedImage (imageUrl, file.getAbsolutePath());
				publishProgress("");
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//UUID id = UUID.fromString(imageUrls[i]);
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			
			
		}
		
	}

}
