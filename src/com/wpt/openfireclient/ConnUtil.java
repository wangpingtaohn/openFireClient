package com.wpt.openfireclient;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

public class ConnUtil {
	
	
	private static XMPPConnection connection;
	public static synchronized XMPPConnection getConnection(){
		if(connection!=null){
			return connection;
		}else{
			ConnectionConfiguration config = new ConnectionConfiguration("192.168.126.152", 5222);
			/** �Ƿ����ð�ȫ��֤ */
			config.setSASLAuthenticationEnabled(false);
			/** �Ƿ����õ��� */
	        //config.setDebuggerEnabled(true);
	        /** ����connection���� */
			connection = new XMPPConnection(config);
		
		}
		
		return connection;
		
	}
}
