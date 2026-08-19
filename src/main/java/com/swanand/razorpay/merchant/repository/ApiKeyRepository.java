package com.swanand.razorpay.merchant.repository;

import com.swanand.razorpay.merchant.entity.ApiKey;
import com.swanand.razorpay.merchant.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKey, UUID> {
}
