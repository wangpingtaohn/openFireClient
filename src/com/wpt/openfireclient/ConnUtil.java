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
			/** 是否启用安全验证 */
			config.setSASLAuthenticationEnabled(false);
			/** 是否启用调试 */
	        //config.setDebuggerEnabled(true);
	        /** 创建connection链接 */
			connection = new XMPPConnection(config);
		
		}
		
		return connection;
		
	}
}
