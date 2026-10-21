package br.com.kumabe.insurance.catalog.domain.service;

import br.com.kumabe.insurance.catalog.domain.model.Coverage;
import br.com.kumabe.insurance.catalog.domain.model.InsuranceProduct;
import br.com.kumabe.insurance.catalog.domain.model.ProductId;
import br.com.kumabe.insurance.catalog.domain.ports.in.CreateProductUseCase;
import br.com.kumabe.insurance.catalog.domain.ports.in.dto.CreateProductCommand;
import br.com.kumabe.insurance.catalog.domain.ports.out.ProductRepositoryPort;

public class CreateProductService implements CreateProductUseCase {
    private final ProductRepositoryPort productRepository;

    public CreateProductService(ProductRepositoryPort productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductId createProduct(CreateProductCommand command) {
        if (productRepository.findByCode(command.code()).isPresent()) {
            throw new IllegalArgumentException("Produto já cadastrado com o código: " + command.code());
        }

        ProductId newId = ProductId.generate();
        InsuranceProduct product = new InsuranceProduct(
                newId,
                command.code(),
                command.name(),
                command.lineOfBusiness()
        );

        for (var covCmd : command.initialCoverages()) {
            product.addCoverage(new Coverage(covCmd.code(), covCmd.name(), covCmd.mandatory()));
        }

        productRepository.save(product);
        return newId;
    }
}
