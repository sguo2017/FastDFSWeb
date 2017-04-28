package com.tispr.file;

import java.io.InputStream;
import java.util.Properties;

public class FileConfigSetting {
	
	public static String FILE_STORE_TYPE ="";  
	
	
	public static String FAST_DFS_HOSTPORT =""; 
	public static String FAST_DFS_DOWNLOAD_HOSTPORT =""; 
	public static String FAST_DFS_POOL_SIZE =""; 
	public static String FAST_DFS_HEART_BEAT_TIME =""; 
	public static String FAST_DFS_SAVE_DIR =""; 
	
	public static String BDP_IP = "";
	public static String BDP_PORT = "";
	public static String BDP_CONTEXTPATH = "";
	public static String BDP_SCHEMA = "";
	public static String BDP_HEAD_TOKEN = "";
	
	public static  String ATTACHMENT_MAX_SIZE = "";
	

	
	public static String SMS_SEND_SWITCH = "off";
	public static String SMS_SEND_URL = "";
	public static String SMS_SEND_APPKEY = "";
	public static String SMS_SEND_SECRET = "";
	public static String SMS_SEND_FREE_SIGNNAME = "";
	public static String SMS_SEND_TEMPLATE_CODE = "";
	

	static {
		try{
			InputStream in  = Utils.getResourceAsStream("fileconfig.properties");
			Properties props = new Properties();
			props.load(in);
			init(props);
			if (Utils.isEmpty(FILE_STORE_TYPE)) {
				FILE_STORE_TYPE = "FTP";
			}
			if (props.getProperty("fast_dfs_hostport") == null) {
				FAST_DFS_HOSTPORT = "80";
			}
			if (Utils.isEmpty(ATTACHMENT_MAX_SIZE)) {
				ATTACHMENT_MAX_SIZE = "10000";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	 
	
	public static void init(Properties props ){
		FILE_STORE_TYPE = props.getProperty("file_store_type");
		FAST_DFS_POOL_SIZE = props.getProperty("fast_dfs_pool_size");//3
		FAST_DFS_HEART_BEAT_TIME = props.getProperty("fast_dfs_heart_beat_time");//15
		FAST_DFS_SAVE_DIR = props.getProperty("fast_dfs_save_dir");//15
		FAST_DFS_HOSTPORT = props.getProperty("fast_dfs_hostport");
		ATTACHMENT_MAX_SIZE = props.getProperty("attachment_max_size");
		
		FAST_DFS_DOWNLOAD_HOSTPORT = props.getProperty("fast_dfs_down_hostname");
		
		BDP_IP = props.getProperty("bdp_ip");
		BDP_PORT = props.getProperty("bdp_port");
		BDP_CONTEXTPATH = props.getProperty("bdp_contextpath");
		BDP_SCHEMA = props.getProperty("bdp_schema");
		BDP_HEAD_TOKEN = props.getProperty("bdp_head_token");
		
		SMS_SEND_SWITCH = props.getProperty("sms_send_switch");
		SMS_SEND_URL = props.getProperty("sms_send_url");
		SMS_SEND_APPKEY = props.getProperty("sms_send_appkey");
		SMS_SEND_SECRET = props.getProperty("sms_send_secret");
		SMS_SEND_FREE_SIGNNAME = props.getProperty("sms_send_free_signname");
		SMS_SEND_TEMPLATE_CODE = props.getProperty("sms_send_template_code");
	}
	
}
