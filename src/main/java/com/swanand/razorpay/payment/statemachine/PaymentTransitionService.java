package com.swanand.razorpay.payment.statemachine;

import com.swanand.razorpay.common.enums.PaymentActor;
import com.swanand.razorpay.common.enums.PaymentEvent;
import com.swanand.razorpay.common.enums.PaymentStatus;
import com.swanand.razorpay.payment.entity.Payment;
import com.swanand.razorpay.payment.entity.PaymentTransitionLog;
import com.swanand.razorpay.payment.repository.PaymentTransitionLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentTransitionService {

    private final PaymentTransitionLogRepository paymentTransitionLogRepository;
    private final PaymentStateMachine paymentStateMachine;

    public PaymentStatus apply(Payment payment, PaymentEvent event){
        PaymentStatus next = paymentStateMachine.transition(payment.getStatus(), event);
        payment.setStatus(next);
        PaymentTransitionLog log = PaymentTransitionLog.builder()
                .payment(payment)
                .fromStatus(payment.getStatus())
                .toStatus(next)
                .occurredAt(LocalDateTime.now())
                .event(event)
                .actor(PaymentActor.SYSTEM)    // TODO : fetch merchant context to identity merchant
                .build();

        paymentTransitionLogRepository.save(log);
        return next;
    }
}
