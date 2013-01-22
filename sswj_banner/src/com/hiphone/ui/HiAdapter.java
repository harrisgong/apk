package com.hiphone.ui;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HiAdapter extends BaseAdapter{
	
	private LayoutInflater mLayoutInflater;
	
	private List<? extends Map<String, ?>> list;
	private int LayoutID;
	private String[] IDs;
	private int[] Views;
	
	public HiAdapter(Context context,List<? extends Map<String, ?>> list, int LayoutID, 
			String[] IDs, int[] Views){
		this.list = list;
		this.LayoutID = LayoutID;
		this.IDs = IDs;
		this.Views = Views;
		this.mLayoutInflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mLayoutInflater.inflate(LayoutID, null);
		}
		Map<String, ?> item = list.get(position);
		int count = Views.length;
		for(int i = 0; i< count; i++){
			View v = convertView.findViewById(Views[i]);
			bindView(v, item, IDs[i]);
		}
		
		convertView.setTag(position);
		
		return convertView;
	}
	
	private void bindView(View view, Map<String, ?> item, String ID) {
		Object data = item.get(ID);
		if (view instanceof TextView) { 
			((TextView) view).setText(data == null ? "" : data.toString());
		}
	}
	
	
}
