package com.swanand.razorpay.merchant.entity;

import com.swanand.razorpay.common.entity.BaseEntity;
import com.swanand.razorpay.common.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_user",
        indexes = {
            @Index(name = "idx_app_user_merchant_id", columnList = "merchant_id")
        })
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

}
