package com.tispr.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class Utils {
	public static boolean isEmpty(String str) {
		if (null == str || str.trim().length() == 0
				|| "null".equalsIgnoreCase(str)) {
			return true;
		}
		return false;
	}
	
	public static String toString(Date date, String pattern){
		if(date == null){
			return "";
		}
		if(pattern == null){
			pattern = "yyyy-MM-dd";
		}
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			dateString = sdf.format(date);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return dateString;
	}
	

	public static String getRandStr(int n) {
		Random random = new Random();
		String sRand = "";
		for (int i = 0; i < n; i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
		}
		return sRand;
	}
	
	public static String mapToJson(Map<String, String> map){
    	if(map==null || map.size()==0) return null;
    	String jsonStr = null;
    	StringBuilder json = new StringBuilder();
    	Iterator it = map.keySet().iterator();
    	while(it.hasNext()){
    		String key = (String) it.next();
    		String value = map.get(key);
    		json.append("\""+key+"\":\""+value+"\",");
    	}
    	if(json.length()>1){
    		jsonStr = json.substring(0, json.length()-1);
    	}
    	jsonStr = "{"+jsonStr+"}";
    	return jsonStr;
    }
	
    public static String getFileExt(String fileName) {

        int potPos = fileName.lastIndexOf('.') + 1;
        String type = fileName.substring(potPos, fileName.length());
        return type;
    }
    
	public static InputStream getResourceAsStream(String resource) {
		try {
			File file = new File(System.getProperty("CONFIG")+resource);
			InputStream stream = null;
			if(file.exists()){
				FileInputStream fileIS  = new FileInputStream(file);
				stream = fileIS;
			}
			if(null == stream){
				String stripped = resource.startsWith("/") ? resource.substring(1)
						: resource;
				stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(stripped);
			}
			return stream;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
