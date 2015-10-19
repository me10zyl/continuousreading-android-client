package net.xicp.zyl_me.continuousreading;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{
	
	private ArrayList<String> viewArr = new ArrayList<String>();
	private ArrayList<String> idArr = new ArrayList<String>();
	public ArrayList<String> getIdArray() {
		return idArr;
	}

	public void setIdArray(ArrayList<String> idArr) {
		this.idArr = idArr;
	}

	private Context context;
	
	public ListViewAdapter(Context context) {
		super();
		this.context = context;
	}
	
	public List<String> getViewArray()
	{
		return viewArr;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return viewArr.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return viewArr.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = LayoutInflater.from(context);
		View inflate = inflater.inflate(R.layout.item_listview, null);
		TextView textview = (TextView) inflate.findViewById(R.id.tv);
		textview.setText(viewArr.get(position));
		return inflate;
	}

}
