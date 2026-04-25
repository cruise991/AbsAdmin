package com.abs.system.util;

import java.util.Map;
import java.util.Properties;

import org.dom4j.Element;

import com.alibaba.fastjson.JSONObject;

import jakarta.mail.Authenticator;
import jakarta.mail.Message.RecipientType;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

	public static JSONObject publishEmail(String mailTo, String mailTitle, String mailContent) {
		try {
			
			
			Element e = XMLUtils.getElementByName("mail");
			Map<String, String> map = XMLUtils.ElementToMap(e);

			Properties props = new Properties();
			props.setProperty("mail.transport.protocol",map.get("transport_protocol"));
			props.setProperty("mail.host", map.get("host"));
			props.setProperty("mail.smtp.auth", map.get("smtp_auth"));
			props.setProperty("mail.smtp.port", map.get("port"));
			
			Authenticator auth = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(map.get("auth"),map.get("pass"));
				}
			};
			Session session = Session.getInstance(props, auth);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(map.get("auth")));
			message.setRecipient(RecipientType.TO, new InternetAddress(mailTo)); 
			message.setSubject(mailTitle);
			message.setContent(mailContent, "text/html;charset=utf-8");
			Transport.send(message);
			return BuildJsonOfObject.getJsonString(MSG.SendSuccess, MSG.SUCCESSCODE);
		} catch (Exception e) {
			e.printStackTrace();
			return BuildJsonOfObject.getJsonString(e.getMessage(), MSG.FAILCODE);

		}
	}
	
	
	
	
	public static void main(String[] args) {
		publishEmail("250242100@qq.com", "验证码", "654");
	}
}
