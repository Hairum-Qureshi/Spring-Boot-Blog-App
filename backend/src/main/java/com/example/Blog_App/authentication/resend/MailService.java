package com.example.Blog_App.authentication.resend;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class MailService {
    private final Resend resend;
    private final String senderEmail;

    public MailService(Resend resend, @Value("${resend.api.sender-email}") String senderEmail) {
        this.resend = resend;
        this.senderEmail = senderEmail;
    }

    public void sendEmail(String toEmail, String verificationCode) throws ResendException, IOException {
        final String HTML_EMAIL_TEMPLATE = new ClassPathResource(
                "templates/verification-email.html"
        ).getContentAsString(StandardCharsets.UTF_8);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("Java Blogs <%s>".formatted(senderEmail))
                .to(toEmail)
                .subject("Your Java Blogs Verification Code")
                .html(HTML_EMAIL_TEMPLATE.replace("{{CODE}}", verificationCode))
                .build();

        try {
            resend.emails().send(params);
        } catch (ResendException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
