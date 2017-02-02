package com.fundin.service.common.mail;

import java.io.IOException;

import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class MailChecker {

	private final static Logger LOG = LoggerFactory.getLogger(
			MailChecker.class.getName());
	
	public static boolean checkMailExist(String email) {
		  String host = "";
		  String hostName = email.split("@")[1];
		  if ("163.com".equalsIgnoreCase(hostName) 
				  || "126.com".equalsIgnoreCase(hostName)) {
			  return true;
		  }
		  
		  Record[] result = null;
		  SMTPClient client = new SMTPClient();
		  try {
			   // 查找MX记录
			   Lookup lookup = new Lookup(hostName, Type.MX);
			   lookup.run();
			   if (lookup.getResult() != Lookup.SUCCESSFUL) {
				   LOG.warn("找不到MX记录\n");
				   return false;
			   } else {
				   result = lookup.getAnswers();
			   }
			   
			   // 连接到邮箱服务器
			   for (int i = 0; i < result.length; i++) {
				   host = result[i].getAdditionalName().toString();
				   client.connect(host);
				   if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
					   client.disconnect();
					   continue;
				   } else {
					   LOG.warn("MX record about " + hostName + " exists.\n");
					   LOG.warn("Connection succeeded to " + host + "\n");
					   break;
				   }
			   }
			   LOG.warn(client.getReplyString());
			
			   client.login("163.com");
			   LOG.warn(">HELLO 163.com\n");
			   LOG.warn("=" + client.getReplyString());
			
			   client.setSender("fund_in@163.com");
			   LOG.warn(">MAIL FROM: <fund_in@163.com>\n");
			   LOG.warn("=" + client.getReplyString());
			
			   client.addRecipient(email);
			   LOG.warn(">RCPT TO: <" + email + ">\n");
			   LOG.warn("=" + client.getReplyString());
			   
			   if (250 == client.getReplyCode()) {
				   return true;
			   }
		  } catch (Exception e) {
			  LOG.error("checkMailExist error!", e);
		  } finally {
			  try {
				  client.disconnect();
			  } catch (IOException e) {
				  e.printStackTrace();
			  }
		  }
		  return false;
	}
	
	public static void main(String[] args) {
		System.out.println(checkMailExist("842076364@163.com"));
	}
	
}
