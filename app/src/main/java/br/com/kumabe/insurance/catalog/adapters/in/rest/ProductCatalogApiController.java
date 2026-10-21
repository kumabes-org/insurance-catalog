package br.com.kumabe.insurance.catalog.adapters.in.rest;

import br.com.kumabe.insurance.catalog.adapters.in.rest.dto.CoverageResponseDto;
import br.com.kumabe.insurance.catalog.adapters.in.rest.dto.CreateProductDto;
import br.com.kumabe.insurance.catalog.adapters.in.rest.dto.ProductDetailsDto;
import br.com.kumabe.insurance.catalog.domain.model.ProductId;
import br.com.kumabe.insurance.catalog.domain.ports.in.CreateProductUseCase;
import br.com.kumabe.insurance.catalog.domain.ports.in.GetProductDetailsQuery;
import br.com.kumabe.insurance.catalog.domain.ports.in.dto.CoverageCommand;
import br.com.kumabe.insurance.catalog.domain.ports.in.dto.CreateProductCommand;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Comparator;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductCatalogApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCatalogApiController.class);
    private final CreateProductUseCase createProductUseCase;
    private final GetProductDetailsQuery getProductDetailsQuery;

    public ProductCatalogApiController(
            CreateProductUseCase createProductUseCase,
            GetProductDetailsQuery getProductDetailsQuery
    ) {
        this.createProductUseCase = createProductUseCase;
        this.getProductDetailsQuery = getProductDetailsQuery;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid CreateProductDto body) {
        LOGGER.info("Received request to create product with code: {}", body.code());
        var command = new CreateProductCommand(
                body.code(),
                body.name(),
                body.lineOfBusiness(),
                body.coverages().stream()
                        .map(c -> new CoverageCommand(c.code(), c.name(), c.mandatory()))
                        .toList()
        );

        ProductId createdId = createProductUseCase.createProduct(command);

        return ResponseEntity.created(URI.create("/api/v1/products/" + createdId.value())).build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailsDto> getById(@PathVariable UUID productId) {
        LOGGER.info("Received request to get product details for ID: {}", productId);
        return getProductDetailsQuery.findById(new ProductId(productId))
                .map(product -> {
                    var coverages = product.getCoverages().stream()
                            .sorted(Comparator.comparing(cov -> cov.getCode()))
                            .map(cov -> new CoverageResponseDto(cov.getCode(), cov.getName(), cov.isMandatory()))
                            .toList();

                    return new ProductDetailsDto(
                            product.getId().value(),
                            product.getCode(),
                            product.getName(),
                            product.getLineOfBusiness(),
                            product.getStatus().name(),
                            coverages
                    );
                })
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}