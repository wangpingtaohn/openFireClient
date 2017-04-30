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
	//�����յ���Ϣ��ͨ���㲥����Ϣ���͵���Ҫ�ĵط�.��������Ȼ�յ��˷���������������Ϣ����δ����Լ�������
	private Handler handler = new Handler(){  
        public void handleMessage(android.os.Message m) {  
            Message msg=new Message();  
            msg=(Message) m.obj; 
            //�Ѵӷ�������õ���Ϣͨ���㲥����  
            Intent intent = new Intent("org.yhn.mes");  
            String[] message=new String[]{  
                    msg.getFrom(),  
                    msg.getBody()};
            if (message != null) {
            	if (message[0] != null) {
            		Log.i("wpt", "==========�յ���������Ϣ  From==========="+message[0].toString());
				}
            	if (message[1] != null) {
            		Log.i("wpt", "==========�յ���������Ϣ  Body==========="+message[1].toString());
				}
			}
            intent.putExtra("message", message);  
            context.sendBroadcast(intent);  
        };  
    };
	
	public boolean login(String a,String p){
			//ConnectionConfiguration config = new ConnectionConfiguration("192.168.0.124", PORT);
		/** �Ƿ����ð�ȫ��֤ */
			//config.setSASLAuthenticationEnabled(false);
		/** �Ƿ����õ��� */
        //config.setDebuggerEnabled(true);
        /** ����connection���� */
			//XMPPConnection connection = new XMPPConnection(config);
		XMPPConnection connection = ConnUtil.getConnection();
		try {
			/** �������� */
			connection.connect();
			
			
			/** ��¼*/
			connection.login(a, p);
			/** ������д�̣߳������뵽��������*/
			//ClientSendThread cst=new ClientSendThread(connection);
			//cst.start();
			//ManageClientThread.addClientSendThread(a, cst);
			
			//��ȡ�û��顢��Ա��Ϣ��
			System.out.println("======��ʼ��ȡ�鼰�û�==========");
			Roster roster = connection.getRoster();
			Collection<RosterGroup> entriesGroup = roster.getGroups();
			
			
			System.out.println("��ĸ�����"+entriesGroup.size());
			for(RosterGroup group: entriesGroup){
				Collection<RosterEntry> entries = group.getEntries();
				System.out.println("=========groupName==="+group.getName());
				for (RosterEntry entry : entries) {
					//Presence presence = roster.getPresence(entry.getUser());
					//Log.i("---", "user: "+entry.getUser());
					Log.i("wpt", "���Ա�����֣�"+ entry.getName());
					Log.i("wpt", "���Ա��user��"+ entry.getUser());
					Presence presence = roster.getPresence(entry.getUser());
					Mode mode = presence.getMode();
					int userState = retrieveState(mode, presence.isAvailable());
					Log.i("wpt", "�û�״̬��" + presence.isAvailable());
					
					//Log.i("---", "tyep: "+entry.getType());
					//Log.i("---", "status: "+entry.getStatus());
					//Log.i("---", "groups: "+entry.getGroups());
				}
			}
			System.out.println("======������ȡ�鼰�û�==========");
			
			
			//�ڵ�½�Ժ�Ӧ�ý���һ��������Ϣ�ļ����������������յ�����Ϣ��
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
                    /**ͨ��handlerת����Ϣ*/  
                	Log.i("wpt", "chatCreated_body=" + msg.getBody());
                    android.os.Message m=handler.obtainMessage();
                    m.obj=msg;  
                    m.sendToTarget(); 
                }  
            });  
        } 
    }
}
