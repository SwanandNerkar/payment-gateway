package com.swanand.razorpay.common.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Money {
    // smallest unit of any currency, like paise for INR, cents for Doller
    private int amountUnits;
    private String currency;

//    private Money(int amountUnits, String currency) {
//        this.amountUnits = amountUnits;
//        this.currency = currency;
//    }

    public static Money of(int amountUnits, String currency){
        return new Money(amountUnits, currency);
    }

    public static Money inr(int amountUnits, String currency){
        return new Money(amountUnits, "INR");
    }

    public Money add(Money other){
        if(!this.currency.equals(other.currency)){
            throw new IllegalArgumentException("Can't add money with different currencies");
        }
        return new Money(this.amountUnits + other.amountUnits, this.currency);
    }

    public Money subtract(Money other){
        if(!this.currency.equals(other.currency)){
            throw new IllegalArgumentException("Can't add money with different currencies");
        }
        return new Money(this.amountUnits - other.amountUnits, this.currency);
    }
}
