package br.com.kumabe.insurance.catalog.domain.ports.out;


import br.com.kumabe.insurance.catalog.domain.model.InsuranceProduct;
import br.com.kumabe.insurance.catalog.domain.model.ProductId;

import java.util.Optional;

public interface ProductRepositoryPort {
    void save(InsuranceProduct product);
    Optional<InsuranceProduct> findById(ProductId id);
    Optional<InsuranceProduct> findByCode(String code);
}