package com.wpt.openfireclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	
	private EditText accountEditText;
	private EditText passwordEditText;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		accountEditText = (EditText) findViewById(R.id.login_account_edit);
		passwordEditText = (EditText) findViewById(R.id.login_pwd_edit);

		findViewById(R.id.login_btn).setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				final String account = accountEditText.getText().toString();
				final String password = passwordEditText.getText().toString();
				if (account.equals("") || password.equals("")) {
					Toast.makeText(MainActivity.this, "账号或密码不能为空！",
							Toast.LENGTH_SHORT).show();
				} else {
					final ClientConServer ccs = new ClientConServer(MainActivity.this);
					new Thread(){
						public void run() {
							boolean b = ccs.login(account, password);
							// 如果登录成功
							if (b) {
//								Toast.makeText(MainActivity.this, "登陆成功！",
//										Toast.LENGTH_SHORT).show();
								
								Intent intent = new Intent(MainActivity.this,ChatActivity.class);
								intent.putExtra("account", account);
								startActivity(intent);
								
								
							} else {
//								Toast.makeText(MainActivity.this, "登陆失败！",
//										Toast.LENGTH_SHORT).show();
							}
						};
					}.start();
					
				}
			}
		});
	}
	
}
