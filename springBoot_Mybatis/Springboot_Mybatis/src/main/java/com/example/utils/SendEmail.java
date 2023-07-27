package com.example.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;


public class SendEmail {




    private static MimeMessage message;



    static {
            try {
                        Properties properties = new Properties();
                        properties.load(SendEmail.class.getResourceAsStream("/Mail.properties"));
               
                        // 发件人邮箱用户名
                        final String userName = properties.getProperty("username");

                        //final String password = AESCodec.decrypt(properties.getProperty("password"));
                        final String password =properties.getProperty("password");


                        Session session = Session.getDefaultInstance(properties, new Authenticator() {
                            public PasswordAuthentication getPasswordAuthentication() {

                                // 发件人邮件用户名、密码
                                return new PasswordAuthentication(userName, password);
                            }
                        });

                        message = new MimeMessage(session);
                        message.setFrom(new InternetAddress(userName));

                        // 设置邮件消息发送的时间
                        message.setSentDate(new Date());
                 } catch (Exception e) {
                        e.printStackTrace();
                 }
    }

    /**
     * 发送纯文本格式邮件
     *
     * @param recipient 收件人
     * @param subject 主题
     * @param text 邮件文本内容
     * @return 邮件是否发送成功
     */
    public static boolean sendTextEmail(String recipient, String subject, String text) {
        try {

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Set Subject: 头部头字段
            message.setSubject(subject);

            // 设置消息体
            message.setText(text);

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sent message failed....");
            return false;
        }
    }

    /**
     * 发送HTML格式邮件
     *
     * @param recipient 收件人
     * @param subject 主题
     * @param html 邮件内容HTML格式
     * @return 邮寄是否发送成功
     */
    public static boolean sendHtmlEmail(String recipient, String subject, String html) {
        try {

            // Set To: 头部头字段
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Set Subject: 头部头字段
            message.setSubject(subject);

            // 创建消息部分
            BodyPart body = new MimeBodyPart();

            // 设置消息
            body.setContent(html, "text/html; charset=utf-8");

            // 创建多重消息
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(body);

            // 将MiniMultipart对象设置为邮件内容
            message.setContent(multipart);

            // 发送消息
            Transport.send(message);
            System.out.println("Sent message successfully....");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sent message failed....");
            return false;
        }
    }



    /**
     * 测试发送HTML格式邮件
     */
    public static void testHtml() {

        // 收件人邮箱
        String recipient = "703864911@qq.com";
        String subject = "生日快乐！";

        StringBuffer content = new StringBuffer();
        content.append("<html>");
        content.append("<body>");
        content.append("<h1>");
        content.append("祝你生日快乐！");
        content.append("</h1>");
        content.append("<img src='http://d.hiphotos.baidu.com/zhidao/wh%3D600%2C800/sign=324d313a233fb80e0c8469d106e10316/21a4462309f79052ab867a350ef3d7ca7bcbd51b.jpg' />");
        content.append("</body>");
        content.append("</html>");

        System.out.println(sendHtmlEmail(recipient, subject, content.toString()));
    }





    public static void main(String[] args) {

        boolean flag = true||true||false;
        System.out.println("flag = " + flag);

    }





}
