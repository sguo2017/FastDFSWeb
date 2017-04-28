package com.tispr.file;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

public class SMSSendServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -339637276212550853L;
	public void destroy() {
		super.destroy();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("sms send work");
    	HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.addHeader("Access-Control-Allow-Origin", "*");
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		HashMap imgUrls = new HashMap();
		String num = request.getParameter("num");
		String param = request.getParameter("param");
		try {
			String url = FileConfigSetting.SMS_SEND_URL;
			String appkey = FileConfigSetting.SMS_SEND_APPKEY;
			String secret = FileConfigSetting.SMS_SEND_SECRET;
			TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
			String extend = "sguo08";//�����ش��������ڡ���Ϣ���ء��л�͸���ظò������������û����Դ����Լ��¼��Ļ�ԱID������Ϣ����ʱ���û�ԱID��������ڣ��û����Ը��ݸû�ԱIDʶ������λ��Աʹ�������Ӧ��
			String smsType = "normal";//�������ͣ�����ֵ����дnormal
			String smsFreeSignName = FileConfigSetting.SMS_SEND_FREE_SIGNNAME;//����ǩ��������Ķ���ǩ���������ڰ�����ڡ���������-����ǩ�������еĿ���ǩ�����硰������ڡ����ڶ���ǩ��������ͨ����ˣ���ɴ��롱������ڡ�������ʱȥ�����ţ���Ϊ����ǩ��������Ч��ʾ������������ڡ���ӭʹ�ð�����ڷ���
			String smsParamString = "{\"smscode\":\""+param+"\"}";//����ģ����������ι���{"key":"value"}��key�������������ģ���еı�����һ�£��������֮���Զ��Ÿ�����ʾ�������ģ�塰��֤��${code}�������ڽ���${product}�����֤��������Ҫ���߱���Ŷ����������ʱ�贫��{"code":"1234","product":"alidayu"}
			String recNum = num;//���Ž��պ��롣֧�ֵ��������ֻ����룬�������Ϊ11λ�ֻ����룬���ܼ�0��+86��Ⱥ�������贫�������룬��Ӣ�Ķ��ŷָ���һ�ε�����ഫ��200�����롣ʾ����18600000000,13911111111,13322222222
			String smsTemplateCode = FileConfigSetting.SMS_SEND_TEMPLATE_CODE;;//����ģ��ID�������ģ��������ڰ�����ڡ���������-����ģ������еĿ���ģ�塣ʾ����SMS_585014
			AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
			req.setExtend(extend);
			req.setSmsType(smsType);
			req.setSmsFreeSignName(smsFreeSignName);
			req.setSmsParamString(smsParamString);
			req.setRecNum(recNum);
			req.setSmsTemplateCode(smsTemplateCode);
			String sms_send_switch = FileConfigSetting.SMS_SEND_SWITCH;
			if("on".equals(sms_send_switch)){
				AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
				System.out.println(rsp.getBody());
			}
			imgUrls.put("result", "0");
			imgUrls.put("message", "���ŷ��ͳɹ�");
			String msg = Utils.mapToJson(imgUrls);
			System.out.println(msg);
			out.write(msg);
		} catch (Exception e) {
			e.printStackTrace();
			imgUrls.put("result", "-1");
			imgUrls.put("message", "���ŷ���ʧ�ܣ�" + e.getMessage());
			String msg = Utils.mapToJson(imgUrls);
			out.write(msg);
		}     
	}
}
