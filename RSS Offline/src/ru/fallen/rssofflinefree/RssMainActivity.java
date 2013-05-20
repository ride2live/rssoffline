package ru.fallen.rssofflinefree;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class RssMainActivity extends Activity implements onLoadCallback,
		onLoadUrlsCallback, onLoadImagesCallBack {
	LinearLayout ll;
	ListView list;
	private static int PREVIOUS = -1;
	private static int NEXT = 1;
	TextView titleRss;
	int currentFeed = 0;
	int feedCount;
	TextView testTextView;
	private Animation inAnimation;
	// ImageView newsImage;
	ProgressDialog loadingDialog;
	String[] urls;
	NewsDatabase nb;
	private int startIndex;
	protected boolean isScrollingState;
	protected boolean isTopListReached;
	private float yStartPull = 0;
	private boolean isPulling, isAnimatingSlide;
	private float pullValue;
	private float listLastPosition;
	private int height;
	private int width;
	RelativeLayout listLayout;
	CheckBox imagesCheckBox;
	FrameLayout touchPad;
	protected boolean refreshingNow;
	NewsCursorAdapter ca;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		isAnimatingSlide = false;
		list = (ListView) findViewById(R.id.mainList);
		imagesCheckBox = (CheckBox)findViewById(R.id.imageCheckBox);
		imagesCheckBox.setChecked(Utils.isImagesOn(this));
		imagesCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				Utils.setImagesOn(getApplicationContext(), isChecked);
				
			}
		});
		titleRss = (TextView) findViewById(R.id.titleRss);
		// newsImage = (ImageView)findViewById(R.id.imageRss);
		ll = (LinearLayout) findViewById(R.id.ll);
		listLayout = (RelativeLayout)findViewById(R.id.listLayout);
		touchPad= (FrameLayout)findViewById(R.id.touchPad);
		nb = new NewsDatabase(this);
		setListListeners();
		urls = getAvailableFeeds();
		if (urls != null && urls.length > 0) {
			System.out.println("urls !=null && urls.length >0");
			showFeed(urls[currentFeed]);
			System.out
			.println("current feed value = " +currentFeed);
		} else {

			downloadRssUrls();
		}
	}

	private void setListListeners() {
		// TODO Auto-generated method stub
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				CursorAdapter tempC = (CursorAdapter) arg0.getAdapter();
				Cursor c = tempC.getCursor();
				c.moveToPosition(arg2);
				
				
				
				
				
				showNewsOptionsDialog (c);
				/*LinearLayout newsOptions = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.news_option_menu, listLayout, false);
				LayoutParams params = (RelativeLayout.LayoutParams)newsOptions.getLayoutParams();
				params.addRule(RelativeLayout.CENTER_IN_PARENT);
				newsOptions.setLayoutParams(params);
				listLayout.addView(newsOptions);
				/*testTextView = (TextView) arg1.findViewById(R.id.testText);
				testTextView.setVisibility(View.VISIBLE);
				// TODO Auto-generated method stub
				/*int xCoordinateItem = arg1.getLeft();
				int yCoordinateItem =  arg1.getTop();
				TextView testTextView = new TextView(getApplicationContext());
				testTextView.setHeight(20);
				testTextView.setWidth(100);
				testTextView.setText("blablabla");
				Toast.makeText(getApplicationContext(), "longclick", Toast.LENGTH_LONG).show();
//				testTextView.setLeft(xCoordinateItem);
//				testTextView.setTop(yCoordinateItem);*/
				return false;
			}
		});
		list.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
					System.out.println("onScrollState SCROLL_STATE_FLING");
					
					isScrollingState = true;
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					System.out.println("onScrollState SCROLL_STATE_IDLE");
					
					isScrollingState = false;
					//ca.decodeImagesAndPutInView();
					break;
				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					//ca.decodeImagesAndPutInView();
					System.out
							.println("onScrollState SCROLL_STATE_TOUCH_SCROLL");
					isScrollingState = true;
				
					break;
				default:
					break;
				}
				// System.out.println
				sendIsScrollingToListAdapter();
				if (isScrollingState && testTextView !=null)
					testTextView.setVisibility(View.GONE);
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem == 0) {
					isTopListReached = true;
				} else {
					isTopListReached = false;
				}
				//System.out.println("onScroll " + firstVisibleItem);
			}

		});
		list.getScrollY();
		list.setOnTouchListener(new OnTouchListener() {
			float xDown = 0;
			float yDown = 0;
			float xUp = 0;
			float yUp = 0;


			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (isAnimatingSlide)
					return true;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					
					xDown = event.getX();
					yDown = event.getY();
					//System.out.println ("xDown " + xDown);
					//System.out.println ("yDown " + yDown);
					
					break;
				case MotionEvent.ACTION_UP:
					xUp = event.getX();
					yUp = event.getY();
					float move = xDown - xUp;
					float yMove = yDown - yUp;
					//System.out.println ("move " + move);
					//System.out.println ("yMove " + yMove);
					// Toast.makeText(getApplicationContext(),String.valueOf(yDown)
					// +"  " + String.valueOf(yUp), Toast.LENGTH_SHORT).show();
					yMove = Math.abs(yMove);

					if (move > 120 && yMove < 50) {
						if (currentFeed < urls.length - 1) {
							currentFeed++;
							startSlideOutAnimation(Parameters.NEXT);
												}
						else
						{
							animateEnd (NEXT);
						}
						
						// Toast.makeText(getApplicationContext(), "slide left",
						// Toast.LENGTH_SHORT).show();

						// slide(PREVIOUS);
					} else if (move < -120 && yMove < 50) {

						if (currentFeed > 0) {

							currentFeed--;
							startSlideOutAnimation(Parameters.PREV);
						}
						else
						{
							animateEnd (PREVIOUS);
						}
						// Toast.makeText(getApplicationContext(),
						// "slide right", Toast.LENGTH_SHORT).show();
						// slide(NEXT);
					}
					// yUp = event.getY();
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		touchPad.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				Animation a;
				System.out.println ("action " + event.getAction());
				switch (event.getAction())
				{
				
				case MotionEvent.ACTION_MOVE:
					
					System.out.println ("yMove " + event.getY());
					if (event.getY() > height/2 && !refreshingNow)
					{
						refreshingNow = true;
						animateListBack(event.getY());
						refreshCurrent(null);
					}
					else if (!refreshingNow)
					{
						a =new TranslateAnimation( 0 ,0 , event.getY(), event.getY());
						a.setDuration(100);
						a.setFillAfter(true);
						listLayout.startAnimation(a);
					}
					break;
				case MotionEvent.ACTION_UP:
					if (!refreshingNow)
						animateListBack(event.getY());
				default:
					break;
				}
				return true;
			}
		});

	}
	protected void showNewsOptionsDialog(final Cursor c) {
		// TODO Auto-generated method stub
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.news_option_menu);
		dialog.setTitle("context menu");
		Button share = (Button) dialog.findViewById(R.id.shareButton);
		Button full = (Button) dialog.findViewById(R.id.fullButton);
		share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_SEND);
				//i.putExtra(Intent.EXTRA_TEXT, c.getString(3));
				Toast.makeText(getApplicationContext(), c.getString(4), Toast.LENGTH_LONG).show(); //3 - news url
				i.setType("image/*");
				
				i.putExtra(Intent.EXTRA_STREAM, Uri.parse(c.getString(4)));
				i.putExtra(Intent.EXTRA_TEXT, c.getString(2));
				 startActivity(i);
			}
		});
		full.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(c.getString(2)));
				 startActivity(i);
			}
		});
		dialog.show();
	}

	protected void animateEnd(int codeAction) {
		// TODO Auto-generated method stub
		Animation a = null;
		if (codeAction == Parameters.NEXT)
		{
			a =new TranslateAnimation( 0 ,-width/2 , 0, 0);
		}
		else 
		{
			a= new TranslateAnimation( 0 ,width/2 , 0, 0);
				
		}
		a.setDuration(400);
		list.startAnimation(a);
		
	}
	public void sendIsScrollingToListAdapter ()
	{
		ca.setScrolling(isScrollingState);
		System.out.println ("Send to adapter " + isScrollingState);
	}
	private void animateListBack (float from)
	{
		Animation a =
				 new TranslateAnimation( 0 ,0 , from, 0);
				 a.setDuration(400); 
				 a.setFillAfter(true);
				 listLayout.startAnimation(a);
				
	}
	

	 @Override
	public void onConfigurationChanged(Configuration newConfig) { //STUPID FUCKING DEPRICATED METHODS! GOOGLE, I HATE YOU!!!!!111111адынадынадын
		// TODO Auto-generated method stub
		
		super.onConfigurationChanged(newConfig);
		gettingDisplaySize ();
		//setContentView(R.layout.activity_main);
	}
	@TargetApi(13)
	private void gettingDisplaySize() {
		// TODO Auto-generated method stub
		try {
			height = getWindow().getWindowManager().getDefaultDisplay().getHeight();
			width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		} catch (Exception e) {
			// TODO: handle exception
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		}
		System.out.println (height);
    	System.out.println (width);
	}

	protected void startSlideOutAnimation(final int action) {
		// TODO Auto-generated method stub

		Animation outAnimation = null;

		if (action == Parameters.NEXT) {
			inAnimation = AnimationUtils.loadAnimation(this,
					R.anim.in_from_right_animation);
			outAnimation = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.to_left_slide);
		} else if (action == Parameters.PREV) {
			inAnimation = AnimationUtils.loadAnimation(this,
					R.anim.in_from_left_animation);
			outAnimation = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.to_right_slide);
		}
		outAnimation.setDuration(400);
		outAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub

				showFeed(urls[currentFeed]);
				inAnimation.setDuration(400);
				inAnimation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						isAnimatingSlide = false;
					}
				});
				list.startAnimation(inAnimation);
				
			}
		});

		list.startAnimation(outAnimation);
		isAnimatingSlide = true;

	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		gettingDisplaySize();
		super.onResume();
	}

	private void downloadRssUrls() {
		// TODO Auto-generated method stub
		AsyncLoadRSSUrls alru = new AsyncLoadRSSUrls(this);
		alru.execute();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		closeDataBase();
		super.onDestroy();
	}

	private void closeDataBase() {
		// TODO Auto-generated method stub
		if (nb != null)
			nb.close();
	}

	private String[] getAvailableFeeds() {
		// TODO Auto-generated method stub
		String[] feedsUrlsFromDB = nb.getFeedsUrls();
		return feedsUrlsFromDB;
	}
	

	private void showFeed(String urlKey) {
		// TODO Auto-generated method stub
		NewsFeed newsFeed = nb.getFeed(urlKey);
		titleRss.setText(newsFeed.getTitle());
		System.out.println("news image " + newsFeed.getImageUrl());
		/*
		 * File imgFile = new File(newsFeed.getImageUrl()); if(imgFile !=null &&
		 * imgFile.exists()){ Bitmap myBitmap =
		 * BitmapFactory.decodeFile(imgFile.getAbsolutePath()); if (myBitmap
		 * !=null) newsImage.setImageBitmap(myBitmap);
		 * 
		 * }
		 */
		//final ArrayList<NewsItem> newsItemsList = nb.getNews(urlKey);
		
		/*list.setAdapter(new MyListAdapter(newsItemsList,
				getApplicationContext()));
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), newsItemsList.get(arg2).getLink(), Toast.LENGTH_SHORT).show ();
				Intent browseIntent = new Intent(Intent.ACTION_VIEW);
				browseIntent.setData(Uri.parse(newsItemsList.get(arg2).getLink()));
				startActivity(browseIntent);
				return false;
			}
		});*/

		ca = new NewsCursorAdapter(this, nb.getNewsCursor(urls[currentFeed]), true);
		list.setAdapter(ca);
		//ca.decodeImagesAndPutInView();
		
	}

	private void startAsyncLoadNews(String[] urls) {
		showLoadingDialog();
		System.out.println("startAsyncLoadNews");
		NewsDatabase nd = new NewsDatabase(this);
		// url = "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml";
		AsyncLoadNews aln = new AsyncLoadNews(this, this, nd, urls, Utils.isImagesOn(this));
		// NewsDatabase nd = new NewsDatabase(getApplicationContext());
		aln.execute();
	}

	private void showLoadingDialog() {
		// TODO Auto-generated method stub
		loadingDialog = new ProgressDialog(this);
		loadingDialog.setTitle("Загрузка");
		loadingDialog.setMessage("Загрузка ленты новостей");
		loadingDialog.show();
	}

	@Override
	public void onFinishLoading(String keyUrlToShow) {
		// TODO Auto-generated method stub
		closeLoadingDialog();
		refreshingNow = false;
		Toast.makeText(this, "load", Toast.LENGTH_SHORT).show();
		//showFeed(urls[currentFeed]);
		
		/*if (keyUrlToShow.equals(Parameters.ALL_REFRESH))
			showFeed(urls[0]);
		else {
			for (int i = 0; i < urls.length; i++) {
				if (urls[i].equals(keyUrlToShow)) {
					showFeed(urls[i]);
					return;
				}
			}
		}*/
		/*if (Utils.isImagesOn(this))
		{
			AsyncLoadImages ali = new AsyncLoadImages();
			ali.execute(this);
			
		}*/
	}

	private void closeLoadingDialog() {
		// TODO Auto-generated method stub
		loadingDialog.cancel();
	}

	@Override
	public void rssload(String[] urls) {
		// TODO Auto-generated method stub
		System.out.println("urls loaded fromm dropbox");
		if (urls != null) {
			System.out.println("urls loaded fromm dropbox !=null");
			this.urls = urls;
			startAsyncLoadNews(urls);
		}
	}

	public void refreshAll(View V) {
			downloadRssUrls();
	}

	public void refreshCurrent(View V) {
		String[] feeedUrlToLoad = new String[] { urls[currentFeed] };
		startAsyncLoadNews(feeedUrlToLoad);
	}

	@Override
	public void imagesLoaded() {
		// TODO Auto-generated method stub
			updateAdapter();
		
	}

	@Override
	public void imageDownloaded() {
		System.out.println ("preupdate!!!$%^#$^");

		// TODO Auto-generated method stub
		updateAdapter();
	}
	private void updateAdapter ()
	{
		ca = new NewsCursorAdapter(this, nb.getNewsCursor(urls[currentFeed]), true);
		list.setAdapter(ca);
		//if (ca!=null)
		//ca.changeCursor(nb.getNewsCursor(urls[currentFeed]));

	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	@Override
	public void registerForContextMenu(View view) {
		// TODO Auto-generated method stub
		System.out.println (view);
		super.registerForContextMenu(view);
	}


}
