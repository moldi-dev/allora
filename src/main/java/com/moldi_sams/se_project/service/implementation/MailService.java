package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.response.OrderResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Service
@Transactional
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final PDFGeneratorService pdfGeneratorService;

    @Value("${spring.mail.from}")
    private String from;

    @Async
    public void sendInvoiceEmail(String to, OrderResponse order) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy h:mm a");

            String formattedDate = order.orderDate().format(formatter);

            Context invoiceContext = new Context();
            Context emailContext = new Context();

            invoiceContext.setVariable("firstName", order.userPersonalInformation().firstName());
            invoiceContext.setVariable("lastName", order.userPersonalInformation().lastName());

            emailContext.setVariable("firstName", order.userPersonalInformation().firstName());
            emailContext.setVariable("lastName", order.userPersonalInformation().lastName());

            invoiceContext.setVariable("orderDate", formattedDate);
            invoiceContext.setVariable("totalPrice", order.totalPrice());
            invoiceContext.setVariable("orderLineProducts", order.orderLineProducts());
            invoiceContext.setVariable("userPersonalInformation", order.userPersonalInformation());

            String emailContent = templateEngine.process("invoice-email", emailContext);
            String pdfHtmlContent = templateEngine.process("invoice-pdf", invoiceContext);

            byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml(pdfHtmlContent);

            helper.setTo(to);
            helper.setFrom(from);
            helper.setSubject("Order Invoice");
            helper.setText(emailContent, true);

            ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");
            helper.addAttachment("invoice.pdf", dataSource);

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
