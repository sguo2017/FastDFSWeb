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
			String extend = "sguo08";//公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用
			String smsType = "normal";//短信类型，传入值请填写normal
			String smsFreeSignName = FileConfigSetting.SMS_SEND_FREE_SIGNNAME;//短信签名，传入的短信签名必须是在阿里大于“管理中心-短信签名管理”中的可用签名。如“阿里大于”已在短信签名管理中通过审核，则可传入”阿里大于“（传参时去掉引号）作为短信签名。短信效果示例：【阿里大于】欢迎使用阿里大于服务。
			String smsParamString = "{\"smscode\":\""+param+"\"}";//短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
			String recNum = num;//短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能加0或+86。群发短信需传入多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
			String smsTemplateCode = FileConfigSetting.SMS_SEND_TEMPLATE_CODE;;//短信模板ID，传入的模板必须是在阿里大于“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
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
			imgUrls.put("message", "短信发送成功");
			String msg = Utils.mapToJson(imgUrls);
			System.out.println(msg);
			out.write(msg);
		} catch (Exception e) {
			e.printStackTrace();
			imgUrls.put("result", "-1");
			imgUrls.put("message", "短信发送失败：" + e.getMessage());
			String msg = Utils.mapToJson(imgUrls);
			out.write(msg);
		}     
	}
}
