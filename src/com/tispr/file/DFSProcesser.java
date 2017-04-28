package com.tispr.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.tispr.fastdfs.FileServerImpl;

public class DFSProcesser {

	public String getFileUrl(String fileId){
		return null;
	}
	
	public void del(String fileId) {
		return ;
	}

	public static final DFSProcesser inst = new DFSProcesser();
	
	public static DFSProcesser getInst(){
		return inst;
	}

	public String upload(File file, String fileFileName, String subFolder) {
		try {
			return this.getFileSer().uploadFile(file, Utils.getFileExt(fileFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public String upload(File file, String fileFileName, String subFolder, int width, int height) {


		return "";
	}

	public String upload(InputStream in, String fileFileName, String subFolder) {
		return "";
	}

	public String upload(byte[] in, String fileFileName, String subFolder) {
		return "";
	}

	public String replaceUrl(String path) {
		return "";
	}

	public String getStroeType() {
		return "DFS";
	}

	public void delLocaoFile(String path) {
		// TODO Auto-generated method stub
		
	}
	
	private FileServerImpl s =null;
	
	private FileServerImpl getFileSer() throws IOException{
		if(null == s){
			try {
				s = new FileServerImpl(Integer.parseInt(FileConfigSetting.FAST_DFS_POOL_SIZE),
						Integer.parseInt(FileConfigSetting.FAST_DFS_HEART_BEAT_TIME),
						FileConfigSetting.FAST_DFS_SAVE_DIR);
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}			
		}
		return s;
	}
}
