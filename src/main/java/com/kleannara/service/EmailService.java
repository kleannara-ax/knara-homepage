package com.kleannara.service;

import com.kleannara.model.MailModel;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@AllArgsConstructor
public class EmailService {

    private JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmail(MailModel mailDto) throws MessagingException {

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        //메일 제목 설정
        helper.setSubject(mailDto.getTitle());

        //수신자 설정
        helper.setTo(mailDto.getAddress());

        //발신자 설정
        helper.setFrom(mailDto.getFrom());

        //참조자 설정
        //helper.setCc(mailDto.getCcAddress());

        //템플릿에 전달할 데이터 설정
        Context context = new Context();
        context.setVariables(mailDto.getVariables());

        //메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process(mailDto.getTemplate(), context);
        helper.setText(html, true);

        //템플릿에 들어가는 이미지 cid로 삽입
        helper.addInline("email_top_logo.jpg", new ClassPathResource("static/email/email_top_logo.jpg"));
        helper.addInline("email_bottom_arrow.jpg", new ClassPathResource("static/email/email_bottom_arrow.jpg"));
        helper.addInline("email_top_image.jpg", new ClassPathResource("static/email/email_top_image.jpg"));
        if("email/voc.html".equals(mailDto.getTemplate())){
            helper.addInline("email_top_title.jpg", new ClassPathResource("static/email/email_top_title.jpg"));
            helper.addInline("email_q.jpg", new ClassPathResource("static/email/email_q.jpg"));
            helper.addInline("email_a.jpg", new ClassPathResource("static/email/email_a.jpg"));
        }
        else if("email/voc2.html".equals(mailDto.getTemplate())){
            helper.addInline("email_top_title_02.jpg", new ClassPathResource("static/email/email_top_title_02.jpg"));
        }
        //메일 보내기
        emailSender.send(message);
    }
}
