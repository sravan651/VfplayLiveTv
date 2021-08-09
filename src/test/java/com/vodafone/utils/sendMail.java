package com.vodafone.utils;

import java.io.FileInputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * The Class sendMail.
 */
public class sendMail {

	public static String serverReportFolderPrefix = null;

	/** The user name. */
	public static String userName = null;

	/** The pass word. */
	public static String passWord = null;

	/** The email to. */
	public static String emailTo = null;

	/** The email to CC. */
	public static String emailToCC = null;

	/** The email to BCC. */
	public static String emailToBCC = null;

	/** The starttls. */
	public static String starttls = null;

	/** The host. */
	public static String host = null;

	/** The port. */
	public static String port = null;

	/** The prop. */
	public static Properties prop = new Properties();

	/** The socket factory class. */
	public static String socketFactoryClass = null;

	/** The fallback. */
	public static String fallback = null;

	/** The path. */
	public static String path = null;

	/** The module name. */
	public static String moduleName = null;

	/** The index of comma. */
	public static int indexOfComma = 0;

	/** The user full name. */
	public static String userFullName = null;

	/** The email regex. */
	public static String EMAIL_REGEX = "[a-z0-9\\_\\-\\.]+@[a-z0-9\\_\\-\\.]+\\.[a-z]+";

	/**
	 * Send email to client.
	 *
	 * @throws Exception
	 *             the exception
	 */
	public static void sendEmailToClient(String body, boolean attachmentFlag, boolean urgentFlag) throws Exception {

		String mailPropertiesFile = System.getProperty("user.dir") + "/src/main/resources/Config/mail.properties";
		prop.load(new FileInputStream(mailPropertiesFile));
		userName = prop.getProperty("userName");
		passWord = prop.getProperty("passWord");

		emailTo = prop.getProperty("emailTo");
		emailToCC = prop.getProperty("emailToCC");
		emailToBCC = prop.getProperty("emailToBCC");

		host = prop.getProperty("host");
		port = prop.getProperty("port");
		starttls = prop.getProperty("starttls");
		socketFactoryClass = prop.getProperty("socketFactoryClass");
		fallback = prop.getProperty("fallback");

		Properties props = System.getProperties();
		props.put("mail.smtp.user", userName);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");

		if (!"".equals(port)) {
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.socketFactory.port", port);
		}

		if (!"".equals(starttls))
			props.put("mail.smtp.starttls.enable", starttls);

		if (!"".equals(socketFactoryClass))
			props.put("mail.smtp.socketFactory.class", socketFactoryClass);

		if (!"".equals(fallback))
			props.put("mail.smtp.socketFactory.fallback", fallback);

		Session session = Session.getDefaultInstance(props, null);
		session.setDebug(false);

		try {

			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(userName, prop.getProperty("userFullName")));
			msg.setSubject(prop.getProperty("subject"));

			if (emailTo.contains(",")) {
				String[] multipleEmailTo = emailTo.split(",");
				for (int j = 0; j < multipleEmailTo.length; j++) {
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(multipleEmailTo[j]));
				}

			} else {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
			}

			if (!"".equals(emailToCC)) {

				if (emailToCC.contains(",")) {
					String[] multipleEmailTo = emailToCC.split(",");
					for (int j = 0; j < multipleEmailTo.length; j++) {
						msg.addRecipient(Message.RecipientType.CC, new InternetAddress(multipleEmailTo[j]));
					}

				} else {
					msg.addRecipient(Message.RecipientType.CC, new InternetAddress(emailToCC));
				}
			}

			if (!"".equals(emailToBCC)) {

				if (emailToBCC.contains(",")) {
					String[] multipleEmailTo = emailToBCC.split(",");
					for (int j = 0; j < multipleEmailTo.length; j++) {
						msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(multipleEmailTo[j]));
					}

				} else {
					msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(emailToBCC));
				}
			}

			BodyPart messageBodyPart = new MimeBodyPart();

			if (body.contains("<html>"))
				messageBodyPart.setContent(body, "text/html");
			else
				messageBodyPart.setText(body);
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if (attachmentFlag) {
				messageBodyPart = new MimeBodyPart();
				String path = System.getProperty("user.dir") + new ConfigReader().getValue("extentReportPath");
				DataSource source = new FileDataSource(path);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName("Execution_Report.html");
				multipart.addBodyPart(messageBodyPart);
			}
			msg.setContent(multipart);

			if (urgentFlag)
				msg.setHeader("X-Priority", "1");

			Transport transport = session.getTransport("smtp");
			transport.connect(host, userName, passWord);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			System.out.println("Mail Sent successfully");
			LogUtil.infoLog(sendMail.class, "Mail Sent" + "-PASS");

		} catch (AuthenticationFailedException e1) {
			JFrame wrongCredentials = new JFrame();
			JOptionPane.showMessageDialog(wrongCredentials, "Wrong Username or Password");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}