package com.tispr.file;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImgKitServlet extends HttpServlet {

	private static final long serialVersionUID = -4723709356639716643L;

	public void destroy() {
		super.destroy();
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] llist = java.awt.Toolkit.getDefaultToolkit().getFontList();
		for(int i=0;i<llist.length;i++){
		System.out.println(llist[i]); 
		}
		
		System.out.println("ImgKit generator work2");
    	HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		HashMap retrun = new HashMap();
		String frist_name = request.getParameter("frist_name");
		frist_name = new String(frist_name.getBytes("ISO-8859-1"),"utf-8");
		String temp_path = "E:\\tmp";
		if(!Utils.isEmpty(temp_path)) {
			try {
				File file = new File(temp_path);
				if (!file.exists() && !file.isDirectory()) {
					file.mkdir();
				}
				String imgName = temp_path + "\\" + System.currentTimeMillis() + ".png";
				File imgFile = new File(imgName);
				createImage(frist_name, new Font("΢���ź�", Font.PLAIN, 32), imgFile, 64, 64);
				DFSProcesser proc = DFSProcesser.getInst();
				String name = proc.upload(imgFile, imgName, request.getContextPath());
				imgFile.delete();
				System.out.println("ImgKitServlet gen file :" + name);
				retrun.put("image_url", name);
				retrun.put("result", "0");
				retrun.put("message", "�ɹ�:"+frist_name);
				String msg = Utils.mapToJson(retrun);
				out.write(msg);
			} catch (Exception e) {
				e.printStackTrace();
				retrun.put("result", "-1");
				retrun.put("message", "ʧ��");
				String msg = Utils.mapToJson(retrun);
				out.write(msg);
			}
		}
	}
	
    // ����str,font����ʽ�Լ�����ļ�Ŀ¼    
    private void createImage(String str, Font font, File outFile,    
            Integer width, Integer height) throws Exception {    
        // ����ͼƬ    
        BufferedImage image = new BufferedImage(width, height,    
                BufferedImage.TYPE_INT_BGR);    
        Graphics g = image.getGraphics();    
        g.setClip(0, 0, width, height);    
        Random random = new Random();
        int r = random.nextInt(255) + 1;// ����[0,10)�����е�������ע�ⲻ����10  +1��[0,10)���ϱ�Ϊ[1,11)���ϣ�����Ҫ��
        int green = random.nextInt(255) + 1;
        int u = random.nextInt(255) + 1;
        Color clr = new Color(r, green, u); 
        g.setColor(clr);    
        g.fillRect(0, 0, width, height);
        g.setColor(Color.white);
        g.setFont(font);// ���û�������    
        /** ���ڻ�ô�ֱ����y */    
        Rectangle clip = g.getClipBounds();    
        FontMetrics fm = g.getFontMetrics(font);    
        int ascent = fm.getAscent();    
        int descent = fm.getDescent();    
        int stringWidth = fm.stringWidth(str);
        int x = (clip.width - stringWidth) / 2 ; 
        int y = (clip.height - (ascent + descent)) / 2 + ascent;    
        g.drawString(str, x, y);// �����ַ���     
        g.dispose();    
        ImageIO.write(image, "png", outFile);// ���pngͼƬ    
    }    
}
