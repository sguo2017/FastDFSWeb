package com.tispr.fastdfs.pool;


/**
 *
 */
public class FileServerPoolSysout {
	
//	private static Logger logger=Logger.getLogger("imageServerLogger");
	
	public static void info(Object o){
//		logger.info(o.toString());
	}
	public static void warn(Object o) {
//		logger.warn(o.toString());
		System.err.println(o);
	}
}
