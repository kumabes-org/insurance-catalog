package br.com.kumabe.insurance.catalog.domain.ports.in;

import br.com.kumabe.insurance.catalog.domain.model.ProductId;
import br.com.kumabe.insurance.catalog.domain.ports.in.dto.CreateProductCommand;

import java.util.List;

public interface CreateProductUseCase {


    ProductId createProduct(CreateProductCommand command);
}