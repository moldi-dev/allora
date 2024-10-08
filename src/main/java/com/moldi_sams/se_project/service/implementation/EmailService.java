package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.response.OrderResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.from}")
    private String from;

    @Async
    public void sendInvoiceEmail(String to, OrderResponse order) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();

            context.setVariable("lastName", order.userPersonalInformation().lastName());
            context.setVariable("firstName", order.userPersonalInformation().firstName());
            context.setVariable("orderDate", order.orderDate().toString());
            context.setVariable("totalPrice", order.totalPrice());
            context.setVariable("orderLineProducts", order.orderLineProducts());
            context.setVariable("userPersonalInformation", order.userPersonalInformation());

            String htmlContent = templateEngine.process("invoice-email", context);

            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject("Order Invoice");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        }

        catch (MessagingException e) {
            throw new RuntimeException("Failed to send order confirmation email", e);
        }
    }

    @Async
    public void sendResetPasswordTokenEmail(String to, String resetPasswordToken) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();

            context.setVariable("resetPasswordToken", resetPasswordToken);

            String htmlContent = templateEngine.process("reset-password-email", context);

            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject("Reset your password");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        }

        catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
