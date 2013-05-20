package ru.fallen.rssofflinefree;

import android.os.Environment;
import ru.fallen.rssofflinefree.R;

public class Parameters {
public static String TITLE_TAG = "title";
public static String ITEM_TAG = "item";
public static String LINK_TAG = "link";
public static String ENCLOSURE_TAG = "enclosure";
public static String TITLE_IMAGE_URL_TAG = "url";
public static String DESCRIPTION_TAG = "description";
public static String IMAGE_TAG = "image";
public static String DATE_TAG = "pubDate";
public static String RSSListLink = "https://dl.dropbox.com/u/102433765/rsslist.txt";
public final static String TEMP = Environment.getExternalStorageDirectory().getAbsolutePath()+"/RSScatch/temp/";
public final static int PREV = 0;
public final static  int NEXT = 1;
public static final String ALL_REFRESH = "all";
public static String NEWS_TITLE = "title";
public static String NEWS_LINK = "link";
public static String NEWS_BODY = "body";
public static String NEWS_IMAGEURL = "image_url";
public static String NEWS_IMAGEPATH= "image_path"; 
public static String NEWS_RSS_URL = "rssurl";
public static String NEWS_DATE = "date";


}
