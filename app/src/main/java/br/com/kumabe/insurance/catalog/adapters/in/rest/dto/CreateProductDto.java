package br.com.kumabe.insurance.catalog.adapters.in.rest.dto;

import br.com.kumabe.insurance.catalog.adapters.in.rest.ProductCatalogApiController;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateProductDto(
        @NotBlank String code,
        @NotBlank String name,
        @NotBlank String lineOfBusiness,
        @NotEmpty List<CoverageDto> coverages
) {}
