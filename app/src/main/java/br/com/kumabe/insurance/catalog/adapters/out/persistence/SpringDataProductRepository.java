package br.com.kumabe.insurance.catalog.adapters.out.persistence;

import br.com.kumabe.insurance.catalog.adapters.out.persistence.entity.ProductJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SpringDataProductRepository extends JpaRepository<ProductJpaEntity, UUID> {

    Optional<ProductJpaEntity> findByCode(String code);

    List<ProductJpaEntity> findByLineOfBusiness(String lineOfBusiness);

    List<ProductJpaEntity> findByStatus(String status);
}