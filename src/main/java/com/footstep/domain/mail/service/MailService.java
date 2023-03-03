package com.footstep.domain.mail.service;

import com.footstep.domain.base.BaseException;
import com.footstep.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@RequiredArgsConstructor
@Transactional
@Service
@PropertySource("classpath:mail/email.properties")
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;
    private final UsersRepository usersRepository;

    @Value("${spring.mail.username}")
    private String email;

    public void sendMail(String to) throws BaseException, MessagingException , UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        String text = "";
        text += "<div style='margin:20px;'>";
        text += "<h1> 당신의 발자취 계정 정지안내입니다.</h1>";
        text += "<br>";
        text += "신고 접수가 3회 누적되어 계정이 한 달간 정지되었습니다.";
        text += "<br>";
        text += "해당 계정의 게시글은 모두 삭제 처리 예정입니다.";
        text += "<br>";
        text += "문의할 내용이 있다면 <b>footstepdangbal@gmail.com</b>으로 문의부탁드립니다.";
        text += "<br>";
        text += "감사합니다.";

        messageHelper.setFrom("footstepdangbal@gmail.com", "당신의 발자취");
        messageHelper.setTo(to);
        messageHelper.setSubject("당신의 발자취 계정 정지 안내드립니다.");
        messageHelper.setText(text, true);

        javaMailSender.send(message);
    }
}
