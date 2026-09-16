package com.swanand.razorpay.payment.service.impl;


import com.swanand.razorpay.common.enums.OrderStatus;
import com.swanand.razorpay.common.enums.PaymentEvent;
import com.swanand.razorpay.common.enums.PaymentStatus;
import com.swanand.razorpay.common.exception.BusinessRuleViolationException;
import com.swanand.razorpay.common.exception.ResourceNotFoundException;
import com.swanand.razorpay.payment.dto.request.PaymentInitRequestDto;
import com.swanand.razorpay.payment.dto.response.PaymentResponse;
import com.swanand.razorpay.payment.entity.OrderRecord;
import com.swanand.razorpay.payment.entity.Payment;
import com.swanand.razorpay.payment.gateway.PaymentGatewayRouter;
import com.swanand.razorpay.payment.gateway.dto.PaymentRequest;
import com.swanand.razorpay.payment.gateway.dto.PaymentResult;
import com.swanand.razorpay.payment.mapper.PaymentMapper;
import com.swanand.razorpay.payment.repository.OrderRepository;
import com.swanand.razorpay.payment.repository.PaymentRepository;
import com.swanand.razorpay.payment.service.PaymentService;
import com.swanand.razorpay.payment.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;
    private final PaymentTransitionService paymentTransitionService;

    @Override
    @Transactional
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequestDto request) {
        OrderRecord order = orderRepository.findById(request.orderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", request.orderId()));

        if(order.getStatus() == OrderStatus.CANCELLED || order.getStatus().equals(OrderStatus.PAID)){
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE", "Order can't accept payment in status: "+order.getStatus());
        }

        order.setStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts() + 1);

        Payment payment = Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .build();

        payment = paymentRepository.save(payment);

        PaymentRequest paymentRequest = new PaymentRequest(payment.getId(),
                request.orderId(), merchantId, order.getAmount(),
                request.method(), request.methodDetails());

        PaymentResult paymentResult =  paymentGatewayRouter.initiate(paymentRequest);

        // can use switch case
        if(paymentResult instanceof PaymentResult.Pending pending){
            payment.setProcessorReference(pending.registrationRef());
        }
        else if(paymentResult instanceof PaymentResult.Failure failure){
//            payment.setStatus(PaymentStatus.FAILED);
            paymentTransitionService.apply(payment, PaymentEvent.AUTHORIZE_FAIL);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
        }
        else if(paymentResult instanceof PaymentResult.Success success){

        }


            // FIXME : can't use patterns in switch, address this problem
//        switch (paymentRequest){
//            case PaymentResult.Pending pending -> payment.setProcessorReference(pending.registrationRef());
//
//        }

        payment = paymentRepository.save(payment);
        order = orderRepository.save(order);

        return paymentMapper.toResponse(payment);
    }

    /*
        capture process is actually deducting and taking money from customers locked amount
     */
    @Override
    @Transactional
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {
        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        payment.setStatus(PaymentStatus.CAPTURING);     // TODO : state machines

        PaymentResult paymentResult = paymentGatewayRouter.capture(payment.getMethod(), paymentId);

        if(paymentResult instanceof PaymentResult.Success success){
//            payment.setStatus(PaymentStatus.CAPTURED);
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());
            log.warn("Payment captured, paymentID: {}", paymentId);
        }
        else if(paymentResult instanceof PaymentResult.Failure failure){
//            payment.setStatus(PaymentStatus.AUTHORIZED);
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
            payment.setFailedAt(LocalDateTime.now()); // check
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
            log.warn("Payment captured, paymentID: {}", paymentId);
        }

        payment = paymentRepository.save(payment);

        // send outbox kafka event

        return paymentMapper.toResponse(payment);
    }
}
