package chao.app.ami.thirdpart.mail;

/**
 * @author qinchao
 * @since 2018/9/14
 */

public class Mail {


    private MailBuilder builder;

    public Mail(MailBuilder builder) {
        this.builder = builder;
    }



    public class MailBuilder {

        private String subject;

        private String content;

        private String from;

        private String[] to;

        private String[] cc;

        public MailBuilder() {

        }

        public MailBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailBuilder content(String content) {
            this.content = content;
            return this;
        }

        public MailBuilder from(String from) {
            this.from = from;
            return this;
        }

        public MailBuilder to(String[] to) {
            this.to = to;
            return this;
        }

        public MailBuilder to(String to) {
            this.to = new String[]{to};
            return this;
        }

        public MailBuilder cc(String[] cc) {
            this.cc = cc;
            return this;
        }

        public MailBuilder cc(String cc) {
            this.cc = new String[]{cc};
            return this;
        }

        public Mail build(){
            return new Mail(this);
        }

    }
}