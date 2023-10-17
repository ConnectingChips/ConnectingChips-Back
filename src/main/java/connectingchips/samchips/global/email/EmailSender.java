package connectingchips.samchips.global.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendTemplateEmail(String toEmail, String subject, String template, HashMap<String, String> values) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(toEmail);
        helper.setSubject(subject);

        // 템플릿에 전달할 데이터 설정
        Context context = new Context();
        values.forEach((key, value)->{
            context.setVariable(key, value);
        });

        // 메일 내용 설정 : 템플릿 프로세스
        String html = templateEngine.process(template, context);
        helper.setText(html, true);

        mailSender.send(message);
    }
}
