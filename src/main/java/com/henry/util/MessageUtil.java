package com.henry.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class MessageUtil {
	
	private static Object lock = new Object();
	
	private static Map<String, Properties> msgMap = new ConcurrentHashMap<String, Properties>();
	
	public static Properties getMessage( String name ) {
		synchronized(lock) {
			if( !msgMap.containsKey(name) ) {
				Properties prop = new Properties();
				try {
					prop.load(MessageUtil.class.getResourceAsStream("/"+name+".properties"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				msgMap.put( name, prop );
			}
		}
		return msgMap.get(name);
	}

}
