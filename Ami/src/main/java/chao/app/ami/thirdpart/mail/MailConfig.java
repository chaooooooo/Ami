package chao.app.ami.thirdpart.mail;

/**
 * @author qinchao
 * @since 2018/9/14
 */
public class MailConfig {

    /**
     * 发送服务器地址
     */
    private Object smtpHost;

    /**
     * 开启smtp认证
     */
    private boolean smtpAuth;
    private String userName;
    private String password;
    private String to;

    public Object getSmtpHost() {
        return smtpHost;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getTo() {
        return to;
    }
}
