package com.swanand.razorpay.vault.validation;

import com.swanand.razorpay.vault.dto.request.TokenizeRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;

public class NotExpireValidator implements ConstraintValidator<NotExpire, TokenizeRequest> {

    @Override
    public boolean isValid(TokenizeRequest dto, ConstraintValidatorContext context) {

        if(dto == null || dto.getExpiryYear() == null || dto.getExpiryMonth() == null){
            return false;
        }

         try{
            YearMonth intputYearMonth = YearMonth.of(dto.getExpiryYear(), dto.getExpiryMonth());
            YearMonth currentYearMonth = YearMonth.now();
            return !intputYearMonth.isBefore(currentYearMonth);
        }
        catch (Exception e){
            return false;
        }
    }
}
