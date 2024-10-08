package com.moldi_sams.se_project.service.implementation;

import com.moldi_sams.se_project.entity.Order;
import com.moldi_sams.se_project.entity.User;
import com.moldi_sams.se_project.enumeration.OrderStatus;
import com.moldi_sams.se_project.exception.BadRequestException;
import com.moldi_sams.se_project.exception.ResourceAlreadyExistsException;
import com.moldi_sams.se_project.exception.ResourceNotFoundException;
import com.moldi_sams.se_project.mapper.OrderMapper;
import com.moldi_sams.se_project.repository.OrderRepository;
import com.moldi_sams.se_project.repository.UserRepository;
import com.moldi_sams.se_project.response.OrderLineProductResponse;
import com.moldi_sams.se_project.response.OrderResponse;
import com.moldi_sams.se_project.response.ProductResponse;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentService {
    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public String handleStripeWebhook(String payload, String sigHeader) {
        Stripe.apiKey = stripeSecretKey;

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        }

        catch (SignatureVerificationException e) {
            throw new BadRequestException("Invalid payment signature");
        }

        if (event.getType().equalsIgnoreCase("checkout.session.completed")) {
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
            Optional<StripeObject> dataObject = deserializer.getObject();

            if (dataObject.isPresent()) {
                Session session = (Session) dataObject.get();
                Long orderId = Long.parseLong(session.getMetadata().get("order_id"));
                handlePaymentSuccess(orderId);
            }

            else {
                throw new RuntimeException("An unexpected error has occured");
            }
        }

        return "Success";
    }

    public String createPaymentLink(OrderResponse order) {
        Stripe.apiKey = stripeSecretKey;

        List<SessionCreateParams.LineItem> stripeLineItems = order.orderLineProducts().stream()
                .map(this::mapOrderLineProductToStripeLineItem)
                .toList();

        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendUrl + "/payment-success")
                    .setCancelUrl(frontendUrl + "/payment-failure")
                    .addAllLineItem(stripeLineItems)
                    .putMetadata("order_id", order.orderId().toString())
                    .build();

            Session session = Session.create(params);

            return session.getUrl();
        }

        catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    private SessionCreateParams.LineItem mapOrderLineProductToStripeLineItem(OrderLineProductResponse orderLineProduct) {
        ProductResponse product = orderLineProduct.product();
        Long quantity = orderLineProduct.quantity();

        return SessionCreateParams.LineItem.builder()
                .setQuantity(quantity)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(product.price().multiply(BigDecimal.valueOf(100)).longValue())
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(product.name())
                                                .setDescription(product.description())
                                                .build()
                                )
                                .build()
                )
                .build();
    }

    private void handlePaymentSuccess(Long orderId) {
        Order searchedOrder = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("The order by the provided id couldn't be found"));

        if (searchedOrder.getOrderStatus().equals(OrderStatus.PAID) || searchedOrder.getOrderStatus().equals(OrderStatus.DELIVERED)) {
            throw new ResourceAlreadyExistsException("This order has already been paid");
        }

        User searchedUser = userRepository
                .findByPersonalInformationUserPersonalInformationId(searchedOrder.getUserPersonalInformation().getUserPersonalInformationId())
                .orElseThrow(() -> new ResourceNotFoundException("The user by the provided user personal information couldn't be found"));

        searchedOrder.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(searchedOrder);

        emailService.sendInvoiceEmail(searchedUser.getEmail(), orderMapper.toOrderResponse(searchedOrder));
    }
}
