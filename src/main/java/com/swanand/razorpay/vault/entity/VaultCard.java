package com.swanand.razorpay.vault.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "vault_card")
public class VaultCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 4, nullable = false)
    private String lastFour;

    // first 6 digits of the card
    @Column(length = 6, nullable = false)
    private String bin;

    @Column(nullable = false)
    private byte[] encryptedPan;

    /*
        dek = random string or secret to encrypt pan
        encrypted_pan : using dek and pan and storing this
        but according to pci-dss compliant rules can't directly store dek as it's secret
        then storing encrypted_dek using master key (secret, comes form environment variable)
     */
    @Column(nullable = false)
    private byte[] encryptedDek;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String expiryMonth;

    @Column(nullable = false)
    private String expiryYear;

    @Column(nullable = false)
    private String cardHolderName;

    private LocalDateTime deletedAt;
}
