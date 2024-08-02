package me.foroauth2.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final VerificationService verificationService;
    private String createCode(){
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)  // leftLimit ~ rightLimit 사이의 난수 생성
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 | i >= 97)) // 유효한 문자,숫자 제외하고 filtering 한다
                .limit(targetStringLength) // 길이 제한 
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append) // 객체 생성, 코드 포인트 StringBuilder에 추가, StringBuilder 객체 합치기
                .toString();
    }

    public String sendVerificationCode(String toEmail) throws MessagingException {
        String verificationCode = createCode();

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true,"utf-8");

        String msgg="";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> SnapTime 입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원 탈퇴 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += verificationCode + "</strong><div><br/> ";
        msgg += "</div>";

        helper.setTo(toEmail);
        helper.setSubject("회원 탈퇴 인증 코드");
        helper.setText(msgg,true);

        verificationService.saveVerificationCode(toEmail,verificationCode);

        javaMailSender.send(message);

        return verificationCode;
    }



}
