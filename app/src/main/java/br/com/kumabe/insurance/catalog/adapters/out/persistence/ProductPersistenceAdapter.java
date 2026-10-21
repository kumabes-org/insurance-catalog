package br.com.kumabe.insurance.catalog.adapters.out.persistence;

import br.com.kumabe.insurance.catalog.adapters.out.persistence.entity.ProductJpaEntity;
import br.com.kumabe.insurance.catalog.domain.model.InsuranceProduct;
import br.com.kumabe.insurance.catalog.domain.model.ProductId;
import br.com.kumabe.insurance.catalog.domain.ports.out.ProductRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductPersistenceAdapter implements ProductRepositoryPort {

    private final SpringDataProductRepository springDataRepository;

    public ProductPersistenceAdapter(SpringDataProductRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public void save(InsuranceProduct product) {
        // Converte o agregado de domínio para entidade JPA e salva
        ProductJpaEntity entity = ProductJpaEntity.fromDomain(product);
        springDataRepository.save(entity);
    }

    @Override
    public Optional<InsuranceProduct> findById(ProductId id) {
        return springDataRepository.findById(id.value())
                .map(ProductJpaEntity::toDomain);
    }

    @Override
    public Optional<InsuranceProduct> findByCode(String code) {
        return springDataRepository.findByCode(code)
                .map(ProductJpaEntity::toDomain);
    }
}