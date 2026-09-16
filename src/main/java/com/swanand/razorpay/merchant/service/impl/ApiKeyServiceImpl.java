package com.swanand.razorpay.merchant.service.impl;

import com.swanand.razorpay.common.exception.ResourceNotFoundException;
import com.swanand.razorpay.common.util.RandomizerUtil;
import com.swanand.razorpay.merchant.dto.request.CreateApiKeyRequest;
import com.swanand.razorpay.merchant.dto.response.ApiKeyCreateResponse;
import com.swanand.razorpay.merchant.dto.response.ApiKeyResponse;
import com.swanand.razorpay.merchant.entity.ApiKey;
import com.swanand.razorpay.merchant.entity.Merchant;
import com.swanand.razorpay.merchant.mapper.ApiKeyMapper;
import com.swanand.razorpay.merchant.repository.ApiKeyRepository;
import com.swanand.razorpay.merchant.repository.MerchantRepository;
import com.swanand.razorpay.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiKeyServiceImpl implements ApiKeyService {

    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;

    @Override
    @Transactional
    public ApiKeyCreateResponse create(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("merchant", merchantId));

        String keyId = "rzp_"+request.environment().name().toLowerCase()+ "_" + RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40);

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret)   // TODO : use Bcrypt Password Encoder
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);
        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());
    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
//        return apiKeyRepository.findByMerchant_Id(merchantId)
//                .stream()
//                .map(apiKey -> new ApiKeyResponse(apiKey.getId(), apiKey.getKeyId(),
//                        apiKey.getEnvironment(), apiKey.isEnabled(),
//                        apiKey.getLastUsedAt(), null))
//                .toList();

    return apiKeyMapper.toResponseList(apiKeyRepository.findByMerchant_Id(merchantId));
    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(key -> key.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("api-key", keyId));

        apiKey.setEnabled(false);
        // no need to explicitly save it as JPA/Hibernate check dirty checking and update it
        apiKeyRepository.save(apiKey);
    }

    @Override
    @Transactional
    public ApiKeyCreateResponse rotateKey(UUID merchantId, UUID keyId) {
        ApiKey apiKey = apiKeyRepository.findById(keyId)
                .filter(k -> k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("api-key", keyId));

        if(!apiKey.isEnabled())
            throw new RuntimeException("Can't rotate a disabled key");

        String newRawSecret = RandomizerUtil.randomBase64(40);
        apiKey.setPreviousSecretHash(apiKey.getKeySecretHash());
        apiKey.setKeySecretHash(newRawSecret);
        apiKey.setRotatedAt(LocalDateTime.now());
        apiKey.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));

        apiKeyRepository.save(apiKey);
        return new ApiKeyCreateResponse(apiKey.getId(), apiKey.getKeyId(), newRawSecret, apiKey.getEnvironment());
    }
}
