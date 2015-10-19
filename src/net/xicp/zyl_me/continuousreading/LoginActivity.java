package net.xicp.zyl_me.continuousreading;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import net.xicp.zyl_me.continuousreading.AsyncHttpUtil.OnRecieveListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {
	private AsyncHttpUtil asyncHttpUtil;
	private Button btn_login;
	private EditText et_username, et_password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		final String ip = getResources().getString(R.string.ip);
		asyncHttpUtil = new AsyncHttpUtil();
		btn_login = (Button) findViewById(R.id.sign_in_button); 
		et_username = (EditText)findViewById(R.id.email);
		et_password = (EditText) findViewById(R.id.password);
		final AsyncHttpUtil asyncHttpUtil = new AsyncHttpUtil();
		btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map<String,String> map = new HashMap<String,String>();
				final String username = et_username.getText().toString();
				final String password = et_password.getText().toString();
				if(username == null || password == null || "".equals(username) || "".equals(password))
				{
					Toast.makeText(LoginActivity.this, "plz enter username & password", Toast.LENGTH_SHORT).show();
					return;
				}
				map.put("username", username);
				map.put("password", password);
				asyncHttpUtil.doPost("http://"+ip+"/continuousreading/android/user/login", new JSONObject(map), new OnRecieveListener() {
					
					@Override
					public void onRecieveMessage(int state, String response) {
						// TODO Auto-generated method stub
						if(state == AsyncHttpUtil.POST_RESPONSE)
						{
				
							try {
								JSONTokener tokener = new JSONTokener(response);
								JSONObject json = (JSONObject) tokener.nextValue();
								boolean success = json.getBoolean("success");
								String message = json.getString("message");
								if(success)
								{
								    Intent intent = new Intent();
									intent.setClass(LoginActivity.this, MainActivity.class);
									JSONObject user = json.getJSONObject("user");
									intent.putExtra("userid", user.getString("id"));
									intent.putExtra("username", user.getString("username"));
									intent.putExtra("message", message);
									startActivity(intent);
									LoginActivity.this.finish();
								}else{
									Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}else{
							Toast.makeText(LoginActivity.this, "Connect to server error :" + response, Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});
	}
}
