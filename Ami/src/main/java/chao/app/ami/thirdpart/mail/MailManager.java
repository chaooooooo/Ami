package chao.app.ami.thirdpart.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author qinchao
 * @since 2018/9/14
 */
public class MailManager {

    private MailConfig config;

    public MailManager() {
        config = new MailConfig();
    }

    /**
     * public void sendMail() throws MessagingException{
     * 23         Properties props = new Properties();
     * 24         //使用smtp代理，且使用网易163邮箱
     * 25         props.put("mail.smtp.host", "smtp.163.cn");
     * 26         //设置验证
     * 27         props.put("mail.smtp.auth", "true");
     * 28         MyAuthenticator myauth = new MyAuthenticator("发件人邮箱@163.com", "密码");
     * 29         Session session = Session.getInstance(props,myauth);
     * 30         //打开调试开关
     * 31         session.setDebug(true);
     * 32         MimeMessage message = new MimeMessage(session);
     * 33         InternetAddress fromAddress = null;
     * 34         //发件人邮箱地址
     * 35         fromAddress = new InternetAddress("发件人邮箱@163.com");
     * 36         message.setFrom(fromAddress);
     * 37
     * 38         InternetAddress toAddress = new InternetAddress("收件人邮箱地址");
     * 39         message.addRecipient(Message.RecipientType.TO, toAddress);
     * 40         message.setSubject("邮件标题");
     * 41         message.setText(mailContext);// 设置邮件内容
     * 42         //message.setFileName("邮件附件");
     * 43         message.saveChanges(); //存储信息
     * 44
     * 45
     * 46         Transport transport = null;
     * 47         transport = session.getTransport("smtp");
     * 48         transport.connect("smtp.163.com", "发件人邮箱@163.com", "密码");
     * 49         transport.sendMessage(message, message.getAllRecipients());
     * 50
     * 51         transport.close();
     * 52     }
     */

    class MyAuthenticator extends javax.mail.Authenticator {
        private String strUser;
        private String strPwd;

        public MyAuthenticator(String user, String password) {
            this.strUser = user;
            this.strPwd = password;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(strUser, strPwd);
        }
    }

    public void sendMail(Mail mail) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getSmtpHost());
        props.put("mail.smtp.auth", config.isSmtpAuth());

        String userName = config.getUserName();
        String passWord = config.getPassword();
        MyAuthenticator authenticator = new MyAuthenticator(userName, passWord);
        Session session = Session.getInstance(props, authenticator);
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);

        String address = config.getTo();
        String[] tos = address.split("\\s+;\\s+");
        for (String to: tos) {
            InternetAddress toAddress = new InternetAddress(to);
            message.addRecipient(Message.RecipientType.TO, toAddress);
        }

//        SMTPTransport.send();
    }
}
