package com.swanand.razorpay.operation;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "dlp_event")
public class DlqEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID merchant;

    /*
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn
        it's also right, based on implementation
     */
    @OneToOne(fetch = FetchType.LAZY)
    private WebhookEvent webhookEvent;

    @Column(length = 1000)
    private String finalError;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Objects> payload;

    private LocalDateTime movedAt;

    private LocalDateTime replayedAt;
}
