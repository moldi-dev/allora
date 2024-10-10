package com.moldi_sams.se_project.service;

import com.moldi_sams.se_project.enumeration.OrderStatus;
import com.moldi_sams.se_project.response.*;
import com.moldi_sams.se_project.service.implementation.MailService;
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
public class MailServiceTests {
    @Autowired
    private MailService mailService;

    @MockBean
    private PaymentService paymentService;

    @MockBean
    private ReCaptchaService reCaptchaService;

    @Test
    public void testResetPasswordEmail() {
        mailService.sendResetPasswordTokenEmail("testemail@domain.com", "test reset password token");
    }

    @Test
    public void testInvoiceEmail() {
        ProductSizeResponse productSizeResponse = new ProductSizeResponse(1L, "XXL");
        ProductSizeResponse productSizeResponse2 = new ProductSizeResponse(2L, "M");

        ProductResponse productResponse = new ProductResponse(
                1L,
                "Jacket",
                "Very good jacket",
                BigDecimal.valueOf(25.99),
                13L,
                List.of(productSizeResponse),
                new ProductBrandResponse(1L, "Brand A"),
                new ProductGenderResponse(1L, "Unisex"),
                new ProductCategoryResponse(1L, "Jackets"),
                List.of(new ImageResponse(1L, "jacket", BigDecimal.valueOf(100), "image/jpeg", "http://localhost:9000/images/jacket.jpeg"))
        );

        ProductResponse productResponse2 = new ProductResponse(
                2L,
                "T-Shirt",
                "Very good t-shirt",
                BigDecimal.valueOf(5.99),
                10L,
                List.of(productSizeResponse, productSizeResponse2),
                new ProductBrandResponse(2L, "Brand B"),
                new ProductGenderResponse(2L, "Men"),
                new ProductCategoryResponse(2L, "T-Shirts"),
                List.of(new ImageResponse(2L, "t-shirt", BigDecimal.valueOf(100), "image/jpeg", "http://localhost:9000/images/t-shirt.jpeg"))
        );

        OrderLineProductResponse orderLineProductResponse = new OrderLineProductResponse(
                1L,
                productResponse,
                2L,
                productSizeResponse
        );

        OrderLineProductResponse orderLineProductResponse2 = new OrderLineProductResponse(
                2L,
                productResponse2,
                3L,
                productSizeResponse2
        );

        UserPersonalInformationResponse userPersonalInformationResponse = new UserPersonalInformationResponse(
                1L,
                "John",
                "Doe",
                "123 Street Name, City, Country"
        );

        OrderResponse orderResponse = new OrderResponse(
                1L,
                List.of(orderLineProductResponse, orderLineProductResponse2),
                BigDecimal.valueOf(69.95),
                OrderStatus.PAID,
                userPersonalInformationResponse,
                LocalDateTime.now()
        );

        mailService.sendInvoiceEmail("testemail@domain.com", orderResponse);
    }
}
