package com.wpt.openfireclient;


import java.util.Collection;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

public class ClientConServer {
	private static int PORT=5222;
	private Context context;
	public ClientConServer(Context context){
		this.context=context;

	}
	//这里收到消息后，通过广播将消息发送到需要的地方.哈哈，既然收到了服务器发送来的信息，如何处理自己决定。
	private Handler handler = new Handler(){  
        public void handleMessage(android.os.Message m) {  
            Message msg=new Message();  
            msg=(Message) m.obj; 
            //把从服务器获得的消息通过广播发送  
            Intent intent = new Intent("org.yhn.mes");  
            String[] message=new String[]{  
                    msg.getFrom(),  
                    msg.getBody()};
            if (message != null) {
            	if (message[0] != null) {
            		Log.i("wpt", "==========收到服务器消息  From==========="+message[0].toString());
				}
            	if (message[1] != null) {
            		Log.i("wpt", "==========收到服务器消息  Body==========="+message[1].toString());
				}
			}
            intent.putExtra("message", message);  
            context.sendBroadcast(intent);  
        };  
    };
	
	public boolean login(String a,String p){
			//ConnectionConfiguration config = new ConnectionConfiguration("192.168.0.124", PORT);
		/** 是否启用安全验证 */
			//config.setSASLAuthenticationEnabled(false);
		/** 是否启用调试 */
        //config.setDebuggerEnabled(true);
        /** 创建connection链接 */
			//XMPPConnection connection = new XMPPConnection(config);
		XMPPConnection connection = ConnUtil.getConnection();
		try {
			/** 建立连接 */
			connection.connect();
			
			
			/** 登录*/
			connection.login(a, p);
			/** 开启读写线程，并加入到管理类中*/
			//ClientSendThread cst=new ClientSendThread(connection);
			//cst.start();
			//ManageClientThread.addClientSendThread(a, cst);
			
			//获取用户组、成员信息。
			System.out.println("======开始获取组及用户==========");
			Roster roster = connection.getRoster();
			Collection<RosterGroup> entriesGroup = roster.getGroups();
			
			
			System.out.println("组的个数："+entriesGroup.size());
			for(RosterGroup group: entriesGroup){
				Collection<RosterEntry> entries = group.getEntries();
				System.out.println("=========groupName==="+group.getName());
				for (RosterEntry entry : entries) {
					//Presence presence = roster.getPresence(entry.getUser());
					//Log.i("---", "user: "+entry.getUser());
					Log.i("wpt", "组成员的名字："+ entry.getName());
					Log.i("wpt", "组成员的user："+ entry.getUser());
					Presence presence = roster.getPresence(entry.getUser());
					Mode mode = presence.getMode();
					int userState = retrieveState(mode, presence.isAvailable());
					Log.i("wpt", "用户状态：" + presence.isAvailable());
					
					//Log.i("---", "tyep: "+entry.getType());
					//Log.i("---", "status: "+entry.getStatus());
					//Log.i("---", "groups: "+entry.getGroups());
				}
			}
			System.out.println("======结束获取组及用户==========");
			
			
			//在登陆以后应该建立一个监听消息的监听器，用来监听收到的消息：
			ChatManager chatManager = connection.getChatManager();
			chatManager.addChatListener(new MyChatManagerListener());
			
			
			return true;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return false;
	 }
	private  int retrieveState(Mode mode, boolean isOnline) {
		int userState = 0;
		if (mode == Mode.dnd) {
			userState = 3;
		} else if (mode == Mode.away || mode == Mode.xa) {
			userState = 1;
		}
		return userState;
	}
	/** message listener*/  
    private class MyChatManagerListener implements ChatManagerListener {  
    	
    	
        public void chatCreated(Chat chat, boolean arg1) {  
            chat.addMessageListener(new MessageListener(){  
                public void processMessage(Chat arg0, Message msg) {  
                    /**通过handler转发消息*/  
                	Log.i("wpt", "chatCreated_body=" + msg.getBody());
                    android.os.Message m=handler.obtainMessage();
                    m.obj=msg;  
                    m.sendToTarget(); 
                }  
            });  
        } 
    }
}
