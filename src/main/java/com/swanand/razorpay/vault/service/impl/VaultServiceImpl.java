package com.swanand.razorpay.vault.service.impl;

import com.swanand.razorpay.common.entity.Money;
import com.swanand.razorpay.common.enums.CardBrand;
import com.swanand.razorpay.common.exception.ResourceNotFoundException;
import com.swanand.razorpay.common.util.RandomizerUtil;
import com.swanand.razorpay.payment.processor.PaymentProcessorRouter;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.swanand.razorpay.payment.processor.dto.PaymentProcessorResponse;
import com.swanand.razorpay.vault.config.VaultEncryptionConfig;
import com.swanand.razorpay.vault.dto.request.TokenizeRequest;
import com.swanand.razorpay.vault.dto.response.TokenizeResponse;
import com.swanand.razorpay.vault.entity.CardToken;
import com.swanand.razorpay.vault.entity.VaultCard;
import com.swanand.razorpay.vault.repository.CardTokenRepository;
import com.swanand.razorpay.vault.repository.VaultCardRepository;
import com.swanand.razorpay.vault.service.VaultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.Month;
import java.time.Year;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VaultServiceImpl implements VaultService {

    private final CardTokenRepository cardTokenRepository;
    private final VaultCardRepository vaultCardRepository;
    private final BytesEncryptor dekEncryptor;
    private final PaymentProcessorRouter paymentProcessorRouter;

    @Override
    @Transactional
    public TokenizeResponse tokenize(TokenizeRequest request, UUID merchantId) {

        String lastFour = request.pan().substring(request.pan().length()-4);
        String bin = request.pan().substring(0,6);
        CardBrand cardBrand = detectBrand(request.pan());

        byte[] dek = KeyGenerators.secureRandom(32).generateKey();
        byte[] encryptedPan = VaultEncryptionConfig.panEncryptor(dek)
                .encrypt(request.pan().getBytes(StandardCharsets.UTF_8));
        byte[] encryptedDek = dekEncryptor.encrypt(dek);

        VaultCard vaultCard = VaultCard.builder()
                .bin(bin)
                .brand(cardBrand.name())
                .lastFour(lastFour)
                .encryptedPan(encryptedPan)
                .encryptedDek(encryptedDek)
                .expiryMonth(Month.of(request.expiryMonth()).name())    //FIXME : not sure what to store month name or value
                .expiryYear(Year.of(request.expiryYear()).toString())
                .cardHolderName(request.cardHolderName())
                .build();

        String token = "tok" + RandomizerUtil.randomBase64(32);

        cardTokenRepository.save(CardToken.builder()
                        .token(token)
                        .vaultCard(vaultCard)
                        .customer(request.customerId())
                        .merchant(merchantId)
                .build());

        return new TokenizeResponse(token, lastFour, cardBrand, request.expiryMonth(), request.expiryYear());
    }

    @Override
    public PaymentProcessorResponse charge(UUID paymentId, String token, Money amount, Map<String, Object> methodDetails) {
        CardToken cardToken = cardTokenRepository.findByTokenAndRevokeAtIsNull(token)
                .orElseThrow(() -> new ResourceNotFoundException("CardToken", token));

        VaultCard vaultCard = cardToken.getVaultCard();
        byte[] panBytes = null;

        try {

            byte[] dek = dekEncryptor.decrypt(vaultCard.getEncryptedDek());
            panBytes = VaultEncryptionConfig.panEncryptor(dek).decrypt(vaultCard.getEncryptedPan());

            String pan = new String(panBytes, StandardCharsets.UTF_8);  // don't log it anywhere, keep it safe/secure while working wit it
            String expiry = vaultCard.getExpiryMonth() + "/" + vaultCard.getExpiryYear();

            PaymentProcessorRequest paymentProcessorRequest = PaymentProcessorRequest
                    .card(paymentId, amount, pan, expiry, methodDetails);

            PaymentProcessorResponse paymentProcessorResponse = paymentProcessorRouter.charge(paymentProcessorRequest);

            log.info("Vault charge registered, token={}****", token.substring(0, 4));

    //        panBytes = null;

            return paymentProcessorResponse;
        }
        catch (Exception e){
            log.warn("Vault charge failed, token={}****", token.substring(0,4));
            return new PaymentProcessorResponse.Failure("VAULT_CHARGE_FAILED", e.getMessage());
        }
        finally {
            if(panBytes != null) Arrays.fill(panBytes, (byte) 0);
        }
    }

    private CardBrand detectBrand(String pan) {
        if(pan.startsWith("4")) return CardBrand.VISA;
        if(pan.startsWith("5") || pan.startsWith("2")) return CardBrand.MASTERCARD;
        if(pan.startsWith("37") || pan.startsWith("34")) return CardBrand.AMEX;
        return CardBrand.RUPAY;
    }
}
