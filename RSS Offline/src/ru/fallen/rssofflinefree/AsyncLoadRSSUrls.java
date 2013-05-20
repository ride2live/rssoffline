package ru.fallen.rssofflinefree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;

public class AsyncLoadRSSUrls extends AsyncTask<Void, Void, String []>{
	onLoadUrlsCallback luc;
	public AsyncLoadRSSUrls(onLoadUrlsCallback luc) {
		// TODO Auto-generated constructor stub
		this.luc = luc;
		
	}

	@Override
	protected String[] doInBackground(Void... params) {
		// TODO Auto-generated method stub
		URL url;
		String [] urlsArray = null;
		ArrayList<String> urls = new ArrayList<String>();
		try {
			url = new URL(Parameters.RSSListLink);
			InputStreamReader isr = new InputStreamReader(url.openStream());
			BufferedReader br = new BufferedReader(isr);
			String line="";
			while ((line = br.readLine()) !=null)
			{
				urls.add(line);
				//System.out.println (line);
			}
			urlsArray = urls.toArray(new String [urls.size()]);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return urlsArray;
		
				

	}
	@Override
	protected void onPostExecute(String[] result) {
		// TODO Auto-generated method stub
		if (luc !=null)
			luc.rssload(result);
		super.onPostExecute(result);
	}

}
