package com.android.sswj;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import net.youmi.android.AdView;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class BookActivity extends Activity implements OnTouchListener {
	
    static final String[] BOOKS = new String[] {
    	"论语", "孟子", "大学", "中庸", 
    	"诗经", "尚书", "礼记", "周易", "春秋"
    };

    static final String[] files = new String[]{
    	"lunyu", "mengzi", "daxue", "zhongyong",
    	"shijing", "shangshu", "liji", "zhouyi", "chunqiu"
    };
    String book_name;
    private TextView title_bar;
    private TextView book_content;
    private ScrollView sView;
    private int lastY = 0;
    private int offset = 0;
    //记录阅读位置
    private static int MODE = MODE_PRIVATE;
    private String PREFERENCE_NAME = "ReadMark";
    private String book = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.book);
		//使用悬浮布局嵌入广告
 		//初始化广告视图
 		AdView adView = new AdView(this);
 		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
 		//设置广告出现的位置(悬浮于屏幕右下角)		 
 		params.gravity=Gravity.BOTTOM|Gravity.RIGHT; 
 		//将广告视图加入Activity中
 		addContentView(adView, params); 
		
		book_name = getIntent().getStringExtra("book_name");
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
		title_bar = (TextView)findViewById(R.id.title_bar);
		title_bar.setText(book_name);	
		

		
		book_content = (TextView)findViewById(R.id.book_content);
		book_content.setText(readbook(book_name));
		
        sView = (ScrollView)findViewById(R.id.book_scrollview);
        sView.setFillViewport(false);
        
        sView.setOnTouchListener(new OnTouchListener() {
        	private int touchEventId = -9983761;
        	Handler handler = new Handler(){
        		@Override
        		public void handleMessage(Message msg) {
        			super.handleMessage(msg);
        			View scroller = (View)msg.obj;
        			if (msg.what==touchEventId) {
        				if(lastY ==scroller.getScrollY()) {
        					handleStop(scroller);
        				}else {
        					handler.sendMessageDelayed(handler.obtainMessage(touchEventId,scroller), 1);
        					lastY = scroller.getScrollY();
        				}
        			}
        		}
        	};
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int eventAction = event.getAction();
				switch (eventAction) {
					case MotionEvent.ACTION_UP:
						handler.sendMessageDelayed(handler.obtainMessage(touchEventId,v), 5);
						break;
				default:
					break;
				}
				return false;
			}
		});
	}
	
	private void handleStop(Object view) {
		lastY = ((View) view).getScrollY();
		Log.i("Hi","handleStop lastY="+lastY);
	}
	
	private Runnable mScrollToY = new Runnable() {
        @Override   
        public void run() {
            if (offset > 0) {    
                 sView.scrollTo(0, offset);//change the position of scrollbar    
             }    
         }    
    };   
     
    
    @Override
	protected void onStart() {
    	Log.i("Hi","onStart");
		for(int i = 0; i < BOOKS.length; i++){
			if(BOOKS[i].equals(book_name)){
				book = files[i];
				break;
			}
		}
		PREFERENCE_NAME += book;
		SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
		offset = sharedPreferences.getInt(book, 0);
		lastY = offset;
		Log.i("Hi",offset+"offset"+ book +"book");
		sView.post(mScrollToY);
		super.onStart();
	}

	@Override
	protected void onRestart() {
		Log.i("Hi","onRestart");
		super.onRestart();
	}
    
	@Override
	protected void onResume() {
		Log.i("Hi","onResume");
		super.onResume();
	}
	
	protected void onStop() {
    	Log.i("Hi","onStop");
    	Log.i("Hi","lastY="+lastY);
    	SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCE_NAME, MODE);
    	SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(book, lastY);
		editor.commit();
		super.onStop();
    };
	
	private String readbook(String book_name) {
		int has = -1;
		Log.i("Hi", book_name);
		for(int i = 0; i < BOOKS.length; i++){
			if(BOOKS[i].equals(book_name)){
				has = i;
				break;
			}
		}
		if( -1 < has && has < BOOKS.length){
			Resources res = getResources();
			AssetManager am =  res.getAssets();
			InputStream is = null;
			byte[] buffer = null;
			
			try {
				is = am.open(files[has]);
				buffer = readInput(is);
				try {
						return new String(buffer,"GBK");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(is != null)
						is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "空";
	}

	public static byte[] readInput(InputStream in ) throws IOException{
		   ByteArrayOutputStream out=new ByteArrayOutputStream();
		   int len=0;
		   byte[] buffer=new byte[1024];
		   while((len=in.read(buffer))>0){
		    out.write(buffer,0,len);
		   }
		   out.close();
		   in.close();
		   return out.toByteArray();
		}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.i("Hi","onTouch");
		if(event.getAction() == MotionEvent.ACTION_UP) {
		}
		return false;
	}
	
	
}