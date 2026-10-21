package br.com.kumabe.insurance.catalog.domain.ports.out;

import br.com.kumabe.insurance.catalog.domain.model.InsuranceProduct;


public interface CatalogEventPublisherPort {
    void publishProductActivated(InsuranceProduct product);
}