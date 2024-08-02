package me.foroauth2.email.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.foroauth2.email.service.EmailService;
import me.foroauth2.email.service.VerificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/emails")
public class EmailController {

    private final EmailService emailService;
    private final VerificationService verificationService;

    @PostMapping("/send")
    @Operation(summary = "이메일 인증코드 발송", description = "email로 인증코드를 전송합니다.")
    public String mailSend(@RequestParam String email) throws MessagingException {
        log.info("EmailController.mailSend()");
        emailService.sendVerificationCode(email);
        return "인증코드가 발송되었습니다.";
    }

    @PostMapping("/verify")
    @Operation(summary = "인증코드를 확인합니다.", description = "이메일과 인증코드를 통해서, 해당 이메일로 보낸 인증코드의 유효를 검증합니다.")
    public String verify(@RequestParam String email, @RequestParam String code) {
        log.info("EmailController.verify()");
        boolean isVerified = verificationService.verifyCode(email, code);
        return isVerified ? "인증이 완료되었습니다." : "인증 실패하셨습니다.";
    }

}
