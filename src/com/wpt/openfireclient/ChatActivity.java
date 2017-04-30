package com.wpt.openfireclient;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChatActivity extends Activity {
	String account;
	String chatNick;
	public static String chatContents;
	TextView chatTextView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_chat);
		/** 设置top面板信息 */
		// chatNick=getIntent().getStringExtra("nick");
//		account=getIntent().getStringExtra("account");

		// 这里的用户昵称和账号是手动添加的。
		chatNick = "忍者无敌笑哈哈";
		account = "wpt@127.0.0.1/Spark 2.6.3";
		TextView nick_tv = (TextView) findViewById(R.id.chat_top_nick);
		nick_tv.setText(chatNick);

		Button send_btn = (Button) findViewById(R.id.chat_btn_send);
		send_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				EditText input = (EditText) findViewById(R.id.chat_input);
				String content = input.getText().toString();
				try {
					XMPPConnection connection = ConnUtil.getConnection();
					ChatManager cm = connection.getChatManager();
					Chat chat = cm.createChat(account, new MessageListener() {
						public void processMessage(Chat arg0, Message msg) {
							Log.i("---", msg.getFrom() + "说：" + msg.getBody());
							// 添加消息到聊天窗口,
						}
					});
					Message m = new Message();
					m.setBody(content);
					chat.sendMessage(m);
				} catch (XMPPException e) {
					e.printStackTrace();
				}
			}
		});
	}
}