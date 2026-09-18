package com.swanand.razorpay.merchant.mapper;

import com.swanand.razorpay.merchant.dto.response.ApiKeyResponse;
import com.swanand.razorpay.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {

    List<ApiKeyResponse> toResponseList(List<ApiKey> apiKeys);
}
