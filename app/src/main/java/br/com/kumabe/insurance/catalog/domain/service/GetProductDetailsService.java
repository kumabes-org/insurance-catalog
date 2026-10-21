package br.com.kumabe.insurance.catalog.domain.service;

import br.com.kumabe.insurance.catalog.domain.model.InsuranceProduct;
import br.com.kumabe.insurance.catalog.domain.model.ProductId;
import br.com.kumabe.insurance.catalog.domain.ports.in.GetProductDetailsQuery;
import br.com.kumabe.insurance.catalog.domain.ports.out.ProductRepositoryPort;

import java.util.Optional;

public class GetProductDetailsService implements GetProductDetailsQuery {

    private final ProductRepositoryPort productRepository;

    public GetProductDetailsService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Optional<InsuranceProduct> findById(ProductId id) {
        return productRepository.findById(id);
    }
}
