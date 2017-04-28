package com.tispr.fastdfs;

import java.io.File;
import java.io.IOException;

public interface FileServer {

	/**
	 * �ϴ��ļ��ӿڡ��Ѹ������ļ��ϴ���ָ���ķ�����
	 * @param file �ļ�
	 *    �ļ���չ��ͨ��file.getName()���
	 * @return �ļ��洢id  ��ʽ��group1/M00/07/FB/wKgEdVE0AMC-L27NAAAmlrYh34k971.jpg
	 * @throws IOException
	 * @throws Exception
	 */
	public String uploadFile(File file) throws IOException, Exception ;
	/**
	 * �ϴ��ļ��ӿڡ��Ѹ������ļ�����ָ������չ���ϴ���ָ���ķ�����
	 * @param file  �ļ�
	 * @param suffix  �ļ���չ������ (jpg,txt)
	 * @return  �ļ��洢·��
	 * @throws IOException
	 * @throws Exception
	 */
	public String uploadFile(File file, String suffix) throws IOException, Exception ;
	/**
	 * �ϴ��ļ��ӿڡ�ͨ�����������ϴ�
	 * @param fileBuff ����������
	 * @param suffix  �ļ���չ�� ����(jpg,txt)
	 * @return
	 */ 
	public String uploadFile(byte[] fileBuff,String suffix) throws IOException, Exception;
	
	/**
	 * �õ�������IP
	 * @return
	 */
	public String getConnnectString() ;
	/**
	 * �õ����Ӷ˿�
	 * @return
	 */
	public int getPort() ;
	
	/**
	 *ɾ���ļ�
	 * @param fileId   �ļ�id,����group���ļ�Ŀ¼��������Ϣ
	 * @return true ɾ���ɹ���falseɾ��ʧ��
	 * @throws IOException
	 * @throws Exception
	 */
	public boolean deleteFile(String fileId) throws IOException,Exception;
	/**
	 * �����ļ�����
	 * @param fileId
	 * @return  �ļ����ݵĶ�������
	 * @throws IOException
	 * @throws Exception
	 */
	public byte[] getFileByID(String fileId)throws IOException,Exception;
	
	
	public byte[] downFileByID(String fileId)throws IOException,Exception;	

}
