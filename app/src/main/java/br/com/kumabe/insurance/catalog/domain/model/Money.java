package br.com.kumabe.insurance.catalog.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public record Money(BigDecimal amount, Currency currency) implements Comparable<Money> {

    public static final Currency BRL = Currency.getInstance("BRL");

    // Construtor compacto: valida e normaliza as casas decimais na criação
    public Money {
        Objects.requireNonNull(amount, "Amount não pode ser nulo");
        Objects.requireNonNull(currency, "Currency não pode ser nula");
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount, BRL);
    }

    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount), BRL);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO, BRL);
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    private void validateSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException(
                    "Incompatibilidade de moedas: " + this.currency + " vs " + other.currency
            );
        }
    }

    @Override
    public int compareTo(Money o) {
        validateSameCurrency(o);
        return this.amount.compareTo(o.amount);
    }
}