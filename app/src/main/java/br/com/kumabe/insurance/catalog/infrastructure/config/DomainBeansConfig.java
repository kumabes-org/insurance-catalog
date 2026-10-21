package br.com.kumabe.insurance.catalog.infrastructure.config;

import br.com.kumabe.insurance.catalog.domain.ports.in.CreateProductUseCase;
import br.com.kumabe.insurance.catalog.domain.ports.in.GetProductDetailsQuery;
import br.com.kumabe.insurance.catalog.domain.ports.out.ProductRepositoryPort;
import br.com.kumabe.insurance.catalog.domain.service.CreateProductService;
import br.com.kumabe.insurance.catalog.domain.service.GetProductDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainBeansConfig {

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepositoryPort productRepository) {
        return new CreateProductService(productRepository);
    }

    @Bean
    public GetProductDetailsQuery getProductDetailsQuery(ProductRepositoryPort productRepository) {
        return new GetProductDetailsService(productRepository);
    }
}