/*
 * @author:gonghaifeng2010@hotmail.com
 * @date:2013-01-18
 */
package com.android.sswj;

import java.util.ArrayList;
import java.util.HashMap;

import net.youmi.android.AdManager;
import net.youmi.android.AdView;

import com.hiphone.ui.HiAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SswjActivity extends Activity implements OnItemClickListener{
    
    private ListView lv = null;
    static final String[] BOOKS = new String[] {
    	"论语", "孟子", "大学", "中庸", 
    	"诗经", "尚书", "礼记", "周易", "春秋"
    };
    static final String[] BOOK_KINDS = new String[]{
    	"四书", "四书", "四书", "四书",
    	"五经", "五经", "五经", "五经", "五经"
    };
    
    private TextView title_bar;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);
        title_bar = (TextView)findViewById(R.id.title_bar);
        title_bar.setText(R.string.app_name);
        
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
        //Hide all screen decorations 
        
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>(); 
        
        for(int i = 0; i < BOOKS.length; i++){
        	HashMap<String, Object> map = new HashMap<String, Object>();
	
        	if(i == 0 || i == 4){
        		map.put("Item1", BOOK_KINDS[i]);
        	}
        	else{
        		map.put("Item1", "");
        	}
        	map.put("Item2", BOOKS[i]);//资源的ID
        	
        	listItem.add(map);
        }
        
        lv = (ListView)findViewById(R.id.list);
        
        lv.setAdapter(
        		new HiAdapter(this, listItem, 
        				R.layout.list_item,
        				new String[]{"Item1", "Item2"}, 
        				new int[] {R.id.list_item1, R.id.list_item2}));
        
        lv.setOnItemClickListener(this);
        
        AdManager.init(this,"217ea86a99759a81", "c34a6f1aa80ed33c", 30, false);
		LinearLayout adViewLayout = (LinearLayout) findViewById(R.id.adViewLayout);
		adViewLayout.addView(new AdView(this), 
				new LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, 
						LinearLayout.LayoutParams.WRAP_CONTENT));
    }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	
		@SuppressWarnings("unchecked")
		HashMap<String,String> map=(HashMap<String,String>)lv.getItemAtPosition(arg2);
		String book_name=map.get("Item2");
//		Toast.makeText(ZzbjActivity.this, book_name, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		intent.putExtra("book_name", book_name);
		intent.setClass(this, BookActivity.class);
		startActivity(intent);
	}
    
    
}