package com.tispr.fastdfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;

import com.tispr.fastdfs.pool.ConnectionPool;
import com.tispr.fastdfs.pool.FileServerPoolSysout;
import com.tispr.file.FileConfigSetting;

public class FileServerImpl implements FileServer {
	private String connnectString;

	private int port = 22133;
	private int size;

	private int waitTimes = 2;

	private ConnectionPool pool = null;

	private String multipartSaveDir;
	
	public FileServerImpl(int size,int heartBeatTime,String multipartSaveDir) throws IOException {
		this.size = size;
		this.multipartSaveDir = multipartSaveDir;
		pool = new ConnectionPool(this.size,heartBeatTime);
	}

	public String uploadFile(File file) throws IOException, Exception {
		if(file==null){
			return null;
		}
		return uploadFile(file, getFileExtName(file.getName()));
	}

	public String uploadFile(File file, String suffix) throws IOException,
			Exception {
		if(file==null){
			return null;
		}
		byte[] fileBuff = getFileBuffer(file);
		return send(fileBuff, suffix);
	}

	public String uploadFile(byte[] fileBuff, String suffix)
			throws IOException, Exception {
		return send(fileBuff, suffix);
	}

	private String send(byte[] fileBuff, String fileExtName)
			throws IOException, Exception {
		String upPath = null;
		if(fileBuff==null){
			return null;
		}
		TrackerServer trackerServer = null;
		try {
			trackerServer = pool.checkout(waitTimes);
			StorageServer storageServer = null;
			StorageClient1 client1 = new StorageClient1(trackerServer,
					storageServer);
			upPath = client1.upload_file1(fileBuff, fileExtName, null);
			pool.checkin(trackerServer);
		} catch (InterruptedException e) {
			// ȷʵû�п�������,������Ҫɾ����fastdfs����
			FileServerPoolSysout.warn("ImageServerImpl execution send throw :"+e);
			throw e;
		} catch (NullPointerException e) {
			FileServerPoolSysout.warn("ImageServerImpl execution send throw :"+e);
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			// ����io�쳣�������쳣��Ĭ��ɾ�����������������
			FileServerPoolSysout.warn("ImageServerImpl execution send throw :"+e);
			pool.drop(trackerServer);
			throw e;
		}
		return "http://"+FileConfigSetting.FAST_DFS_DOWNLOAD_HOSTPORT+":"+FileConfigSetting.FAST_DFS_HOSTPORT+"/"+upPath;
	}

	private String getFileExtName(String name) {
		String extName = null;
		if (name != null && name.contains(".")) {
			extName = name.substring(name.lastIndexOf(".") + 1);
		}
		return extName;
	}

	
	private byte[] getFileBuffer(File file) {
		byte[] fileByte = null;
		FileInputStream fis=null;
		try {
			fis = new FileInputStream(file);
			fileByte = new byte[fis.available()];
			fis.read(fileByte);
		} catch (FileNotFoundException e) {
			FileServerPoolSysout.warn("ImageServerImpl read file  throw :"+e);
		} catch (IOException e) {
			FileServerPoolSysout.warn("ImageServerImpl read file  throw :"+e);
		}finally{
			try {
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				fis=null;
			}
		}
		return fileByte;
	}

	@Override
	public String getConnnectString() {
		return connnectString;
	}

	@Override
	public int getPort() {
		return port;
	}

	public int getSize() {
		return size;
	}

	public int getWaitTimes() {
		return waitTimes;
	}

	public void setWaitTimes(int waitTimes) {
		this.waitTimes = waitTimes;
	}

	@Override
	public boolean deleteFile(String fileId) throws IOException, Exception {
		boolean result=false;
		TrackerServer trackerServer =null;
		try {
			trackerServer = pool.checkout(waitTimes);
			StorageServer storageServer = null;
			StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
			result=client1.delete_file1(fileId)==0?true:false;
			pool.checkin(trackerServer);
		} catch (InterruptedException e) {
			// ȷʵû�п�������,������Ҫɾ����fastdfs����
			FileServerPoolSysout.warn("ImageServerImpl execution deleteFile throw :"+e);
			throw e;
		} catch (NullPointerException e) {
			FileServerPoolSysout.warn("ImageServerImpl execution deleteFile throw :"+e);
			throw e;
		} catch (Exception e) {
			// ����io�쳣�������쳣��Ĭ��ɾ�����������������
			FileServerPoolSysout.warn("ImageServerImpl execution deleteFile throw :"+e);
			e.printStackTrace();
			pool.drop(trackerServer);
			throw e;
		}
		return result;
	}

	@Override
	public byte[] getFileByID(String fileId)throws IOException,Exception{
		byte[] file_buff=null;
		TrackerServer trackerServer =null;
		try {
			trackerServer = pool.checkout(waitTimes);
			StorageServer storageServer = null;
			StorageClient1 client1 = new StorageClient1(trackerServer, storageServer);
//			result=client1.download_file1(fileId);
	        String group_name = fileId.substring(0, fileId.indexOf("/"));//"group1";  
	        String remote_filename = fileId.replace(group_name+"/", "");//"M00/00/00/Ci0vyFOaj3aAZWbLAAAAD5s_cqg063.txt";  
	        FileInfo fi = client1.get_file_info(group_name, remote_filename);
	        String ex = fileId.substring(fileId.lastIndexOf(".") + 1, fileId.length());
	        File file = new File(multipartSaveDir+System.currentTimeMillis() + "." + ex);
	        file_buff =client1.download_file(group_name, remote_filename);
/*	        FileOutputStream fos = new FileOutputStream(file);
	        if(file_buff != null){
	        	fos.write(file_buff);
	        }
	        fos.close();*/
			pool.checkin(trackerServer);
		} catch (InterruptedException e) {
			// ȷʵû�п�������,������Ҫɾ����fastdfs����
			FileServerPoolSysout.warn("ImageServerImpl execution getFileByID throw :"+e);
			throw e;
		} catch (NullPointerException e) {
			FileServerPoolSysout.warn("ImageServerImpl execution getFileByID throw :"+e);
			throw e;
		} catch (Exception e) {
			// ����io�쳣�������쳣��Ĭ��ɾ�����������������
			FileServerPoolSysout.warn("ImageServerImpl execution getFileByID throw :"+e);
			e.printStackTrace();
			pool.drop(trackerServer);
			throw e;
		}
		return file_buff;
	}
	
	@Override
	public byte[] downFileByID(String fileId)throws IOException,Exception{
		byte[] file_buff=null;
		TrackerServer trackerServer =null;
		try {
			trackerServer = pool.checkout(waitTimes);
			StorageServer storageServer = null;  
	        StorageClient storageClient = new StorageClient(trackerServer, storageServer); 
	        String group_name = "group1";  
	        String remote_filename = "M00/00/00/Ci0vyFOaj3aAZWbLAAAAD5s_cqg063.txt";  
	        FileInfo fi = storageClient.get_file_info(group_name, remote_filename);
	        File file = new File(multipartSaveDir+System.currentTimeMillis()+".txt");
	        FileOutputStream fos = new FileOutputStream(file);
	        file_buff =storageClient.download_file(group_name, remote_filename);
	        if(file_buff != null){
	        	fos.write(file_buff);
	        }
	        fos.close();
			pool.checkin(trackerServer);
		} catch (InterruptedException e) {
			// ȷʵû�п�������,������Ҫɾ����fastdfs����
			FileServerPoolSysout.warn("ImageServerImpl execution getFileByID throw :"+e);
			throw e;
		} catch (NullPointerException e) {
			FileServerPoolSysout.warn("ImageServerImpl execution getFileByID throw :"+e);
			throw e;
		} catch (Exception e) {
			// ����io�쳣�������쳣��Ĭ��ɾ�����������������
			FileServerPoolSysout.warn("ImageServerImpl execution getFileByID throw :"+e);
			e.printStackTrace();
			pool.drop(trackerServer);
			throw e;
		}
		return file_buff;
		
/*		int connectTimeout =3000;
		int readTimeout = 15000;
		String rsp = WebUtils.doPost("http://" + connnectString+"/"+fileId, new HashMap(),Constants.CHARSET_UTF8, connectTimeout, readTimeout,new HashMap<String, String>());
		return rsp.getBytes();*/

	}

}
