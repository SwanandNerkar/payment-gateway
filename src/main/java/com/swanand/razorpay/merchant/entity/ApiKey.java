package com.swanand.razorpay.merchant.entity;

import com.swanand.razorpay.common.enums.Environment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_key")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiKey {

    /*
    what : creating API key as merchant,
    api key into backend and put keysecrethash in server & keyID
    call razorpay server
    like digital signature to make sure , came from valid merchant and not other
     */

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "merchant_id", nullable = false)
    private Merchant merchant;

    @Column(name = "key_id", unique = true, nullable = false, length = 50)
    private String keyId;

    @Column(name ="key_secret" ,nullable = false, length = 200)
    private String keySecret;

    @Column(nullable = false, length = 50)
    private String webhook_secret_hash;

    // provide support for development and testing environment
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Environment environment;

    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    private LocalDateTime lastUsedAt;
    private LocalDateTime rotatedAt;
    private LocalDateTime gracePeriodExpiresAt;
}
