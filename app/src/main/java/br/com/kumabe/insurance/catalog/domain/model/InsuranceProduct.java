package br.com.kumabe.insurance.catalog.domain.model;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class InsuranceProduct {

    private final ProductId id;
    private final String code;
    private final String name;
    private final String lineOfBusiness; // ex: AUTO, LIFE, HOME
    private ProductStatus status;
    private final Set<Coverage> coverages;
    private final Set<Assistance> assistances;
    private final Instant createdAt;
    private Instant updatedAt;

    public InsuranceProduct(ProductId id, String code, String name, String lineOfBusiness) {
        this.id = Objects.requireNonNull(id, "ID é obrigatório");
        this.code = validateCode(code);
        this.name = Objects.requireNonNull(name, "Nome é obrigatório");
        this.lineOfBusiness = Objects.requireNonNull(lineOfBusiness, "Ramo é obrigatório");
        this.status = ProductStatus.DRAFT;
        this.coverages = new HashSet<>();
        this.assistances = new HashSet<>();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void addCoverage(Coverage coverage) {
        if (this.status == ProductStatus.ARCHIVED) {
            throw new IllegalStateException("Não é possível alterar produtos arquivados.");
        }
        this.coverages.add(Objects.requireNonNull(coverage, "Cobertura inválida"));
        this.updatedAt = Instant.now();
    }

    public void activate() {
        if (this.coverages.isEmpty()) {
            throw new IllegalStateException("Um produto precisa ter ao menos uma cobertura para ser ativado.");
        }
        if (this.status == ProductStatus.ACTIVE) {
            throw new IllegalStateException("O produto já está ativo.");
        }
        this.status = ProductStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    private String validateCode(String code) {
        if (code == null || !code.matches("^[A-Z0-9_]{3,20}$")) {
            throw new IllegalArgumentException("Código do produto deve conter entre 3 e 20 caracteres alfanuméricos em caixa alta.");
        }
        return code;
    }

    public void deprecate() {
        validateTransition(ProductStatus.DEPRECATED);
        this.status = ProductStatus.DEPRECATED;
        this.updatedAt = Instant.now();
    }

    public void archive() {
        validateTransition(ProductStatus.ARCHIVED);
        this.status = ProductStatus.ARCHIVED;
        this.updatedAt = Instant.now();
    }

    private void validateTransition(ProductStatus targetStatus) {
        if (!this.status.canTransitionTo(targetStatus)) {
            throw new IllegalStateException(
                    "Transição de status inválida: não é permitido mudar de " + this.status + " para " + targetStatus
            );
        }
    }

    public ProductId getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getLineOfBusiness() { return lineOfBusiness; }
    public ProductStatus getStatus() { return status; }
    public Set<Coverage> getCoverages() { return Collections.unmodifiableSet(coverages); }
    public Set<Assistance> getAssistances() { return Collections.unmodifiableSet(assistances); }
}