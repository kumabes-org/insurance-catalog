package br.com.kumabe.insurance.catalog.domain.ports.in;

import br.com.kumabe.insurance.catalog.domain.model.InsuranceProduct;
import br.com.kumabe.insurance.catalog.domain.model.ProductId;

import java.util.Optional;

public interface GetProductDetailsQuery {
    Optional<InsuranceProduct> findById(ProductId id);
}
