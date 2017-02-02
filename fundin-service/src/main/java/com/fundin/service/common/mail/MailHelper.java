package com.fundin.service.common.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailHelper {

	private final static Logger LOG = LoggerFactory.getLogger(
			MailHelper.class.getName());
	
	private static ExecutorService mailExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());
	
	private static class MailThread implements Runnable {
		private MailSenderInfo mailInfo;
		public MailThread(MailSenderInfo mailInfo) {
			this.mailInfo = mailInfo;
		}
		@Override
		public void run() {
			sendMail(mailInfo);
		}
	}
	
	public static boolean asyncSendMail(MailSenderInfo mailInfo) {
		mailExecutor.execute(new MailThread(mailInfo));
		return true;
	}
	
	public static boolean sendMail(MailSenderInfo mailInfo) {
		int retry = mailInfo.getRetryTime();
		while (retry > 0) {
			try {
				sendMailOriginal(mailInfo);
				Thread.sleep(100);
				break;
			} catch (Exception ex) {
				LOG.error("MailHelper sendMail error --- mailInfo: {}", 
						mailInfo.toString(), ex);
			}
			retry --;
		}
		return true;
	}
	
	private static boolean sendMailOriginal(MailSenderInfo mailInfo) 
			throws UnsupportedEncodingException {
		MyAuthenticator authenticator = null;
		if (mailInfo.isValidate()) {
			authenticator = new MyAuthenticator(
					mailInfo.getUserName(), mailInfo.getPassword());
		}
		
		Properties properties = getProperties(mailInfo);
		Session sendMailSession = Session.getDefaultInstance(
				properties, authenticator);
		
		try {
			// 根据session创建一个邮件消息
			Message mailMessage = new MimeMessage(sendMailSession);
			// 创建邮件发送者地址
			Address from = new InternetAddress(mailInfo.getFromAddress());
			// 设置邮件消息的发送者
			mailMessage.setFrom(from);
			// 将所有接收者地址都添加到邮件接收者属性中
			mailMessage.setRecipients(Message.RecipientType.TO, getAllToAddress(mailInfo));
			// 设置邮件消息的主题
			mailMessage.setSubject(mailInfo.getSubject());
			// 设置邮件消息发送的时间
			mailMessage.setSentDate(new Date());
			
			// MimeMultipart类是一个容器类，包含MimeBodyPart类型的对象
			Multipart mainPart = new MimeMultipart();
			// 设置邮件消息的主要内容
			String mailContent = mailInfo.getContent();
			MimeBodyPart contentPart = new MimeBodyPart();
			contentPart.setText(mailContent);
			mainPart.addBodyPart(contentPart);
			mailMessage.setContent(mainPart);
			Transport.send(mailMessage);
			return true;
		} catch (MessagingException ex) {
			LOG.error("MailHelper sendMailOriginal error --- mailInfo: {}", 
					mailInfo.toString(), ex);
		}
		return false;
	}
	
	private static Properties getProperties(MailSenderInfo mailInfo){    
	      Properties properties = new Properties();    
	      properties.put("mail.smtp.host", mailInfo.getMailServerHost());    
	      properties.put("mail.smtp.port", mailInfo.getMailServerPort());
	      properties.put("mail.smtp.connectiontimeout", mailInfo.getConnectTimeOut());
	      properties.put("mail.smtp.timeout", mailInfo.getTimeOut());
	      properties.put("mail.smtp.auth", mailInfo.isValidate() ? "true" : "false"); 
	      return properties;
	}
	
	private static Address[] getAllToAddress(MailSenderInfo mailInfo) 
			throws AddressException {
		String[] receivers = mailInfo.getToAddress().split(";");
		Address[] tos = new InternetAddress[receivers.length];
		for (int i = 0; i < receivers.length; i++) {
			tos[i] = new InternetAddress(receivers[i]);
		}
		return tos;
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
//		MailSenderInfo mailInfo = new MailSenderInfo();
//		mailInfo.setValidate(true);
//		mailInfo.setUserName(MailConstants.MAIL_USER_NAME);
//		mailInfo.setPassword(MailConstants.MAIL_USER_PASSWORD);
//		mailInfo.setFromAddress(MailConstants.MAIL_USER_NAME);
//		mailInfo.setToAddress("18782947250@163.com");
//		mailInfo.setSubject("欢迎来到Fundin.cn");
//		mailInfo.setContent("欢迎来到Fundin.cn");
//		sendMail(mailInfo);
		
		String link = "http://www.sojump.com/jq/8026576.aspx";
		
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setValidate(true);
		mailInfo.setUserName(MailConstants.MAIL_USER_NAME);
		mailInfo.setPassword(MailConstants.MAIL_USER_PASSWORD);
		mailInfo.setFromAddress(MailConstants.MAIL_USER_NAME);
		mailInfo.setToAddress("18782947250@qq.com");
		mailInfo.setSubject("Fundin--欢迎新用户");
		mailInfo.setContent("您好，欢迎您注册Fundin.cn，请点击链接" + link 
				+ "，写下您宝贵的意见吧，我们将不胜感激！");
		sendMail(mailInfo);
	}
	
}
