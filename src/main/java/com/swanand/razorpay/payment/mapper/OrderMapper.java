package com.swanand.razorpay.payment.mapper;

import com.swanand.razorpay.payment.dto.response.OrderResponse;
import com.swanand.razorpay.payment.entity.OrderRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {

    OrderResponse toResponse(OrderRecord order);
}
