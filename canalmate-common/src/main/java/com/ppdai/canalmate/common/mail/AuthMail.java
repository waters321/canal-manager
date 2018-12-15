package com.ppdai.canalmate.common.mail;

import java.util.Date;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.ppdai.canalmate.common.utils.P;
import com.ppdai.canalmate.common.utils.PropertiesUtils;


public class AuthMail {

  public static int doSendMail(String titie, String content, String[] addressStrT) {
    int result = 1;
    try {

      String smtp = PropertiesUtils.getValue("smtp.ip");
      String port = PropertiesUtils.getValue("smtp.port");
      String emaiFrom = PropertiesUtils.getValue("smtp.from");
      String username = PropertiesUtils.getValue("smtp.username");
      String passwd = PropertiesUtils.getValue("smtp.passwd");

      Properties props = new Properties();

      Authenticator auth = new Email_Autherticator(username, passwd);
      props.put("mail.smtp.host", smtp);
      props.put("mail.smtp.port", port);

      Session session = Session.getDefaultInstance(props, auth);
      session.setDebug(false);

      MimeMessage message = new MimeMessage(session);
      message.setHeader("Content-Type", "text/html;charset='UTF-8'");
      message.setContent("Content-Type", "text/html;charset='UTF-8'");
      message.setSubject(titie);
      message.setText(content);
      message.setSentDate(new Date());
      message.saveChanges();
      Address address = new InternetAddress(emaiFrom, "");
      message.setFrom(address);

      String[] addressStr = addressStrT;
      int len = addressStr.length;
      InternetAddress[] addressTo = new InternetAddress[len];
      for (int i = 0; i < len; i++) {
        addressTo[i] = new InternetAddress(addressStr[i]);
      }
      message.addRecipients(Message.RecipientType.TO, addressTo);

      Transport.send(message);
      P.print("Send Mail SUCCESS! content:" + content);
    } catch (Exception e) {
      e.printStackTrace();
      result = 0;
    }
    return result;
  }
}
