package net.xicp.zyl_me.continuousreading;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xicp.zyl_me.continuousreading.AsyncHttpUtil.OnRecieveListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private ListView lv;
	private ListViewAdapter listViewAdapter;
	private TextView tv_welcome;
	private AsyncHttpUtil asyncHttpUtil;
	private String userid;
	private String ip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ip = getResources().getString(R.string.ip);
		tv_welcome = (TextView) findViewById(R.id.tv_welcome);
		lv = (ListView) findViewById(R.id.lv);
		listViewAdapter = new ListViewAdapter(this);
		lv.setAdapter(listViewAdapter);
		List<String> arr = listViewAdapter.getViewArray();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		final String message = bundle.getString("message");
		userid = bundle.getString("userid");
		tv_welcome.setText(Html.fromHtml(message));
		asyncHttpUtil = new AsyncHttpUtil();
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				String url = listViewAdapter.getViewArray().get(position);
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setMessage("determine to delete it?");
				builder.setNegativeButton("no", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				});
				builder.setPositiveButton("yes", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String urlid = listViewAdapter.getIdArray().get(
								position);
						Map<String, String> map = new HashMap<String, String>();
						map.put("urlid", urlid);
						asyncHttpUtil.doPost("http://" + ip
								+ "/continuousreading/android/url/delete", map,
								new OnRecieveListener() {

									@Override
									public void onRecieveMessage(int state,
											String response) {
										// TODO Auto-generated method stub
										if (state == AsyncHttpUtil.POST_RESPONSE) {
											refresh(null);
										} else {
											Toast.makeText(
													getApplicationContext(),
													response,
													Toast.LENGTH_SHORT).show();
										}
									}
								});
					}
				});
				builder.create();
				builder.show();
				return true;
			}
		});
		refresh(null);
	}

	public void refresh(View v) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("userid", userid);
		asyncHttpUtil.doPost("http://" + ip
				+ "/continuousreading/android/url/list", map,
				new OnRecieveListener() {

					@Override
					public void onRecieveMessage(int state, String response) {
						// TODO Auto-generated method stub
						if (state == AsyncHttpUtil.POST_RESPONSE) {
							JSONTokener tokener = new JSONTokener(response);
							try {
								JSONObject jsonObject = (JSONObject) tokener
										.nextValue();
								JSONArray urls = jsonObject
										.getJSONArray("urls");
								listViewAdapter.getViewArray().clear();
								listViewAdapter.getIdArray().clear();
								for (int i = 0; i < urls.length(); i++) {
									JSONObject url = urls.getJSONObject(i);
									listViewAdapter.getViewArray().add(
											url.getString("name"));
									listViewAdapter.getIdArray().add(
											url.getString("id"));
								}
								listViewAdapter.notifyDataSetChanged();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							Toast.makeText(getApplicationContext(), response,
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
}
