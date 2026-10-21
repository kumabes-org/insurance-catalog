package br.com.kumabe.insurance.catalog.domain.model;

import java.util.Objects;
import java.util.UUID;

public class Coverage {

    private final UUID id;
    private final String code;
    private final String name;
    private final boolean mandatory;
    private final Money minLimit;
    private final Money maxLimit;

    // Construtor simplificado para cadastro básico
    public Coverage(String code, String name, boolean mandatory) {
        this(UUID.randomUUID(), code, name, mandatory, Money.zero(), Money.of(1_000_000.00));
    }

    // Construtor completo com regras de limites de indenização
    public Coverage(UUID id, String code, String name, boolean mandatory, Money minLimit, Money maxLimit) {
        this.id = Objects.requireNonNull(id, "ID da cobertura é obrigatório");
        this.code = validateCode(code);
        this.name = Objects.requireNonNull(name, "Nome da cobertura é obrigatório");
        this.mandatory = mandatory;
        this.minLimit = Objects.requireNonNull(minLimit, "Limite mínimo é obrigatório");
        this.maxLimit = Objects.requireNonNull(maxLimit, "Limite máximo é obrigatório");

        if (maxLimit.compareTo(minLimit) < 0) {
            throw new IllegalArgumentException("O limite máximo deve ser maior ou igual ao limite mínimo de indenização.");
        }
    }

    private String validateCode(String code) {
        if (code == null || !code.matches("^[A-Z0-9_]{3,30}$")) {
            throw new IllegalArgumentException("Código da cobertura deve ter entre 3 e 30 caracteres alfanuméricos em caixa alta.");
        }
        return code;
    }

    public boolean isWithinLimits(Money requestedAmount) {
        return requestedAmount.compareTo(minLimit) >= 0 && requestedAmount.compareTo(maxLimit) <= 0;
    }

    // Identidade da cobertura no catálogo baseada no código único (ex: COV_COLLISION, COV_FIRE)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coverage coverage = (Coverage) o;
        return Objects.equals(code, coverage.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public UUID getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public boolean isMandatory() { return mandatory; }
    public Money getMinLimit() { return minLimit; }
    public Money getMaxLimit() { return maxLimit; }
}