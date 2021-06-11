package com.timi.common.util;

import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.misc.ExchangeVersion;
import microsoft.exchange.webservices.data.core.enumeration.property.BodyType;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.credential.ExchangeCredentials;
import microsoft.exchange.webservices.data.credential.WebCredentials;
import microsoft.exchange.webservices.data.property.complex.AttachmentCollection;
import microsoft.exchange.webservices.data.property.complex.EmailAddressCollection;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * @description 邮件工具类
 * hhh
 **/
@Component
public class TimiEmailSend {

	private static final Logger LOGGER = LoggerFactory.getLogger(TimiEmailSend.class);

	/**
	 * ews 发送邮件
	 * @param emailServer		邮箱服务器 @Value("${email.server.address}")
	 * @param sender    		发送邮箱
	 * @param senderPwd 		发送邮箱密码
	 * @param receivers 		收件人集合
	 * @param cc        		抄送人集合
	 * @param subject   		标题
	 * @param body      		内容
	 * @param attachments 		附件。key：附件名称；value:附件
	 */
	public void sendMail(String emailServer, String sender, String senderPwd, List<String> receivers, List<String> cc, String subject,
						 String body, Map<String, InputStream> attachments) {
		LOGGER.info("发邮件,参数：发件邮箱{}, 发件密码{}, 接收人{}, 邮件标题{}", sender, senderPwd, receivers, subject);
		ExchangeService service = new ExchangeService(ExchangeVersion.Exchange2010);
		ExchangeCredentials credentials = new WebCredentials(sender, senderPwd);
		service.setCredentials(credentials);
		try {
			service.setUrl(new URI(emailServer));
		} catch (Exception e) {
			LOGGER.info("转发邮件异常,参数：发件邮箱{}, 发件密码{}, 接收人{}, 邮件标题{}, {}", sender, senderPwd, receivers, subject, e);
//            throw new ServiceException("发信邮箱服务器连接失败", e);
		}

		EmailMessage msg = null;
		try {
			msg = new EmailMessage(service);
			msg.setSubject(subject);
			MessageBody messageBody = MessageBody.getMessageBodyFromText(body);
			messageBody.setBodyType(BodyType.HTML);
			msg.setBody(messageBody);
			EmailAddressCollection toRecipients = msg.getToRecipients();
			for (String receiver : receivers) {
				toRecipients.add(receiver);
			}
			EmailAddressCollection ccRecipients = msg.getCcRecipients();
			if (cc != null) {
				for (String c : cc) {
					ccRecipients.add(c);
				}
			}

			if (!CollectionUtils.isEmpty(attachments)) {
				AttachmentCollection  attachmentCollection= msg.getAttachments();
				attachments.entrySet().forEach(attachment -> {
					attachmentCollection.addFileAttachment(attachment.getKey(), attachment.getValue());
				});
			}

			msg.send();
		} catch (Exception e) {
			LOGGER.error("发信失败,发信标题：{},发信内容：{}", subject, body, e);
//            throw new ServiceException("发信失败", e);
		}


	}

	public void sendMail(String emailServer, String sender, String senderPwd, List<String> receivers, List<String> cc, String subject,
						 String body) {
		this.sendMail(emailServer, sender, senderPwd, receivers, cc, subject, body, null);
	}


}
