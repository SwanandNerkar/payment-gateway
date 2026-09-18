package com.swanand.razorpay.merchant.service.impl;

import com.swanand.razorpay.common.enums.MerchantStatus;
import com.swanand.razorpay.common.enums.UserRole;
import com.swanand.razorpay.common.exception.DuplicateResourceException;
import com.swanand.razorpay.merchant.dto.request.MerchantSignupRequest;
import com.swanand.razorpay.merchant.dto.response.MerchantResponse;
import com.swanand.razorpay.merchant.entity.AppUser;
import com.swanand.razorpay.merchant.entity.Merchant;
import com.swanand.razorpay.merchant.mapper.MerchantMapper;
import com.swanand.razorpay.merchant.repository.AppUserRepository;
import com.swanand.razorpay.merchant.repository.MerchantRepository;
import com.swanand.razorpay.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final MerchantRepository merchantRepository;
    private final AppUserRepository appUserRepository;
    private final MerchantMapper merchantMapper;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {

        if(merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL",
                    "Merchant with provided email is already exists: "+ request.email());
        }

        Merchant merchant = merchantMapper.toEntityFromSignUpRequest(request);
        merchant.setStatus((MerchantStatus.PENDING_KYC));
        merchant = merchantRepository.save(merchant);

        AppUser appUser = AppUser.builder()
                .merchant(merchant)
                .email(request.email())
                .passwordHash(request.password())     // TODO :  encrypt the password
                .role(UserRole.OWNER)
                .build();

        appUserRepository.save(appUser);

        return merchantMapper.toResponse(merchant);
    }
}
