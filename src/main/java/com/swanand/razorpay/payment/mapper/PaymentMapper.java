package com.swanand.razorpay.payment.mapper;

import com.swanand.razorpay.payment.dto.response.PaymentResponse;
import com.swanand.razorpay.payment.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PaymentMapper {

    /*
        as PaymentResponse has orderId as field but payment has orderRecord order
        so we map it and go inside of it order.id using . operator
        . operator
     */
    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toResponse(Payment payment);

    @Mapping(target = "orderId", source = "order.id")
    List<PaymentResponse> toResponseList(List<Payment> paymentList);
}
