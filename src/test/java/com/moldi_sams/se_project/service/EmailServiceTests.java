package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.enumeration.OrderStatus;
import com.moldi_sams.se_project.response.*;
import com.moldi_sams.se_project.service.implementation.EmailService;
import com.moldi_sams.se_project.service.implementation.PaymentService;
import com.moldi_sams.se_project.service.implementation.ReCaptchaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class EmailServiceTests {
    @Autowired
    private EmailService emailService;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private ReCaptchaService reCaptchaService;

    @Test
    public void testResetPasswordEmail() {
        emailService.sendResetPasswordTokenEmail("testemail@domain.com", "test reset password token");
    }

    @Test
    public void testInvoiceEmail() {
        ProductSizeResponse productSizeResponse = new ProductSizeResponse(1L, "XXL");

        ProductResponse productResponse = new ProductResponse(
                1L,
                "T-shirt",
                "Very good T-shirt",
                BigDecimal.valueOf(25.99),
                13L,
                List.of(productSizeResponse),
                new ProductBrandResponse(1L, "Brand A"),
                new ProductGenderResponse(1L, "Unisex"),
                new ProductCategoryResponse(1L, "Clothing"),
                List.of(new ImageResponse(1L, "image1.jpg", BigDecimal.valueOf(100), "image/jpeg", "http://example.com/image1"))
        );

        OrderLineProductResponse orderLineProductResponse = new OrderLineProductResponse(
                1L,
                productResponse,
                2L,
                productSizeResponse
        );

        UserPersonalInformationResponse userPersonalInformationResponse = new UserPersonalInformationResponse(
                1L,
                "John",
                "Doe",
                "123 Street Name, City, Country"
        );

        OrderResponse orderResponse = new OrderResponse(
                1L,
                List.of(orderLineProductResponse),
                BigDecimal.valueOf(51.98),
                OrderStatus.PAID,
                userPersonalInformationResponse,
                LocalDateTime.now()
        );

        emailService.sendInvoiceEmail("testemail@domain.com", orderResponse);
    }
}
