package br.com.kumabe.insurance.catalog.domain.service;

import br.com.kumabe.insurance.catalog.domain.model.InsuranceProduct;
import br.com.kumabe.insurance.catalog.domain.model.ProductId;
import br.com.kumabe.insurance.catalog.domain.ports.out.ProductRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetProductDetailsServiceTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @InjectMocks
    private GetProductDetailsService getProductDetailsService;

    @Test
    void shouldReturnProductWhenRepositoryFindsById() {
        ProductId productId = ProductId.generate();
        InsuranceProduct expectedProduct = new InsuranceProduct(productId, "AUTO_STD", "Auto Standard", "AUTO");
        when(productRepository.findById(productId)).thenReturn(Optional.of(expectedProduct));

        Optional<InsuranceProduct> result = getProductDetailsService.findById(productId);

        assertThat(result).contains(expectedProduct);
        verify(productRepository).findById(productId);
    }

    @Test
    void shouldReturnEmptyWhenRepositoryDoesNotFindById() {
        ProductId productId = ProductId.generate();
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        Optional<InsuranceProduct> result = getProductDetailsService.findById(productId);

        assertThat(result).isEmpty();
        verify(productRepository).findById(productId);
    }
}
