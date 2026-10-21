package br.com.kumabe.insurance.catalog.adapters.out.persistence.entity;

import br.com.kumabe.insurance.catalog.domain.model.Coverage;
import br.com.kumabe.insurance.catalog.domain.model.Money;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Entity
@Table(
        name = "coverages",
        indexes = {
                @Index(name = "idx_coverages_code", columnList = "code"),
                @Index(name = "idx_coverages_product_id", columnList = "product_id")
        }
)
public class CoverageJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, length = 32)
    private String code;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "mandatory", nullable = false)
    private boolean mandatory;

    @Column(name = "min_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal minLimit;

    @Column(name = "max_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal maxLimit;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductJpaEntity product;

    public CoverageJpaEntity() {
        // Construtor sem argumentos requerido pelo JPA/Hibernate
    }

    public static CoverageJpaEntity fromDomain(Coverage domain, ProductJpaEntity product) {
        CoverageJpaEntity entity = new CoverageJpaEntity();
        entity.id = domain.getId();
        entity.code = domain.getCode();
        entity.name = domain.getName();
        entity.mandatory = domain.isMandatory();
        entity.minLimit = domain.getMinLimit().amount();
        entity.maxLimit = domain.getMaxLimit().amount();
        entity.currency = domain.getMinLimit().currency().getCurrencyCode();
        entity.product = product;
        return entity;
    }

    public Coverage toDomain() {
        Currency curr = Currency.getInstance(this.currency);
        return new Coverage(
                this.id,
                this.code,
                this.name,
                this.mandatory,
                new Money(this.minLimit, curr),
                new Money(this.maxLimit, curr)
        );
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public BigDecimal getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(BigDecimal minLimit) {
        this.minLimit = minLimit;
    }

    public BigDecimal getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(BigDecimal maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public ProductJpaEntity getProduct() {
        return product;
    }

    public void setProduct(ProductJpaEntity product) {
        this.product = product;
    }
}