package com.swanand.razorpay.payment.entity;

import com.swanand.razorpay.common.entity.BaseEntity;
import com.swanand.razorpay.common.enums.PaymentActor;
import com.swanand.razorpay.common.enums.PaymentEvent;
import com.swanand.razorpay.common.enums.PaymentMethod;
import com.swanand.razorpay.common.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payment_transition_log",
        indexes = {
                @Index(name = "idx_payment_transition_log_payment_id", columnList = "payment_id")
        })
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTransitionLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(nullable = false, name = "payment_id")
    private Payment payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "from_status", nullable = false, length = 50)
    private PaymentStatus fromStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "event", nullable = false, length = 50)
    private PaymentEvent event;

    @Enumerated(EnumType.STRING)
    @Column(name = "to_status", nullable = false, length = 50)
    private PaymentStatus toStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor", length = 100)
    private PaymentActor actor;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt;
}
