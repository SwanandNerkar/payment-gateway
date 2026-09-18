package com.swanand.razorpay.merchant.entity;

import com.swanand.razorpay.common.entity.BaseEntity;
import com.swanand.razorpay.common.enums.Environment;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_key",
        indexes = {
            @Index(name = "idx_api_key_merchant_env", columnList = "merchant_id, environment, enabled")
        })
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiKey extends BaseEntity {

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

    @Column(name ="key_secret_hash" ,nullable = false, length = 200)
    private String keySecretHash;

    @Column(length = 200)
    private String previousSecretHash;

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
