package com.tispr.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ImageUploadServlet extends HttpServlet {

	private static final long serialVersionUID = -8642000911520011534L;

	public void destroy() {
		super.destroy();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
    	System.out.println("image upload work");
		String temp_path = "E:\\tmp";
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		DiskFileItemFactory factory = null;
		ServletFileUpload upload = null;
		PrintWriter out = response.getWriter();
		Map<String, String> imgUrls = new HashMap<String, String>();
		if (!Utils.isEmpty(temp_path)) {
			try {
				File file = new File(temp_path);
				if (!file.exists() && !file.isDirectory()) {
					file.mkdir();
				}
				factory = new DiskFileItemFactory();
				factory.setRepository(file);
				factory.setSizeThreshold(1024 * 1024 * 5);
				upload = new ServletFileUpload(factory);

				List<FileItem> list = upload.parseRequest(request);
				for (FileItem item : list) {
					// 获取表单的属性名字
					String fieldName = item.getFieldName();
					String fileName = item.getName();
					// 如果获取的 表单信息是普通的 文本 信息
					if (!item.isFormField()) {
						InputStream in = item.getInputStream();

						File tmpfile = File.createTempFile(
								"upload_"
										+ Utils.toString(
												new Date(System
														.currentTimeMillis()),
												"yyyyMMddhhmmss")
										+ Utils.getRandStr(4)
										+ fileName.substring(0,
												fileName.indexOf(".")), ".tmp",
								file);
						FileOutputStream o = new FileOutputStream(tmpfile);
						byte b[] = new byte[1024];
						int n;
						while ((n = in.read(b)) != -1) {
							o.write(b, 0, n);
						}
						o.close();
						in.close();
						DFSProcesser proc = DFSProcesser.getInst();
						String names = proc.upload(tmpfile, fileName, request.getContextPath());
						imgUrls.put(fieldName, names);
					}
				}
				imgUrls.put("result", "0");
				imgUrls.put("message", "图片上传成功");
				String msg = Utils.mapToJson(imgUrls);
				out.write(msg);
			} catch (Exception e) {
				e.printStackTrace();
				imgUrls.put("result", "-1");
				imgUrls.put("message", "图片上传失败：" + e.getMessage());
				String msg = Utils.mapToJson(imgUrls);
				out.write(msg);
			}
		} else {
			imgUrls.put("result", "-1");
			imgUrls.put("message",
					"请检查es_config_info表是否配置【GOODS_IMAGE_TEMP_PATH】临时文件存放目录");
			String msg = Utils.mapToJson(imgUrls);
			out.write(msg);
		}

	}
}
