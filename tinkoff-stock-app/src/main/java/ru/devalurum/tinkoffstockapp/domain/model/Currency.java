package ru.devalurum.tinkoffstockapp.domain.model;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants(onlyExplicitlyIncluded = true)
public enum Currency {

    @FieldNameConstants.Include RUB,
    @FieldNameConstants.Include USD,
    @FieldNameConstants.Include EUR,
    @FieldNameConstants.Include GBP,
    @FieldNameConstants.Include HKD,
    @FieldNameConstants.Include CHF,
    @FieldNameConstants.Include JPY,
    @FieldNameConstants.Include CNY,
    @FieldNameConstants.Include TRY;

}
