package com.example.timesheets2.persistance.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.math.BigDecimal;

@Converter
public class MonetaryValueConverter implements AttributeConverter<BigDecimal, Integer> {
    private final static int MONETARY_VALUE_SCALE = 2;

    @Override
    public Integer convertToDatabaseColumn(BigDecimal monetaryValue) {
        return monetaryValue.scaleByPowerOfTen(MONETARY_VALUE_SCALE).intValue();
    }

    @Override
    public BigDecimal convertToEntityAttribute(Integer monetaryValueInCents) {
        return BigDecimal.valueOf(monetaryValueInCents).scaleByPowerOfTen(-MONETARY_VALUE_SCALE);
    }
}
