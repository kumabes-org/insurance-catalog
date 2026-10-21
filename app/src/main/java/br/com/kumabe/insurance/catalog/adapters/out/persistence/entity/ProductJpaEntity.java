package br.com.kumabe.insurance.catalog.adapters.out.persistence.entity;

import br.com.kumabe.insurance.catalog.domain.model.Coverage;
import br.com.kumabe.insurance.catalog.domain.model.InsuranceProduct;
import br.com.kumabe.insurance.catalog.domain.model.Money;
import br.com.kumabe.insurance.catalog.domain.model.ProductId;
import br.com.kumabe.insurance.catalog.domain.model.ProductStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
        name = "products",
        indexes = {
                @Index(name = "idx_products_code", columnList = "code", unique = true),
                @Index(name = "idx_products_lob", columnList = "line_of_business")
        }
)
public class ProductJpaEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true, length = 32)
    private String code;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Column(name = "line_of_business", nullable = false, length = 64)
    private String lineOfBusiness;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private ProductStatus status;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<CoverageJpaEntity> coverages = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public ProductJpaEntity() {
        // Construtor padrão exigido pela especificação JPA
    }

    public static ProductJpaEntity fromDomain(InsuranceProduct product) {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.id = product.getId().value();
        entity.code = product.getCode();
        entity.name = product.getName();
        entity.lineOfBusiness = product.getLineOfBusiness();
        entity.status = product.getStatus();
        entity.createdAt = Instant.now();
        entity.updatedAt = Instant.now();

        if (product.getCoverages() != null) {
            for (Coverage coverage : product.getCoverages()) {
                entity.addCoverage(CoverageJpaEntity.fromDomain(coverage, entity));
            }
        }

        return entity;
    }

    public InsuranceProduct toDomain() {
        InsuranceProduct product = new InsuranceProduct(
                new ProductId(this.id),
                this.code,
                this.name,
                this.lineOfBusiness
        );

        if (this.coverages != null) {
            for (CoverageJpaEntity covEntity : this.coverages) {
                product.addCoverage(covEntity.toDomain());
            }
        }

        // Restaura o status caso tenha avançado além de DRAFT
        if (this.status == ProductStatus.ACTIVE) {
            product.activate();
        } else if (this.status == ProductStatus.DEPRECATED) {
            product.activate();
            product.deprecate();
        } else if (this.status == ProductStatus.ARCHIVED) {
            product.archive();
        }

        return product;
    }

    public void addCoverage(CoverageJpaEntity coverage) {
        this.coverages.add(coverage);
        coverage.setProduct(this);
    }

    public void removeCoverage(CoverageJpaEntity coverage) {
        this.coverages.remove(coverage);
        coverage.setProduct(null);
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLineOfBusiness() { return lineOfBusiness; }
    public void setLineOfBusiness(String lineOfBusiness) { this.lineOfBusiness = lineOfBusiness; }

    public ProductStatus getStatus() { return status; }
    public void setStatus(ProductStatus status) { this.status = status; }

    public List<CoverageJpaEntity> getCoverages() { return coverages; }
    public void setCoverages(List<CoverageJpaEntity> coverages) { this.coverages = coverages; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}