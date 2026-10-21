package br.com.kumabe.insurance.catalog.adapters.in.rest.dto;

import java.util.List;
import java.util.UUID;

public record ProductDetailsDto(
        UUID id,
        String code,
        String name,
        String lineOfBusiness,
        String status,
        List<CoverageResponseDto> coverages
) {
}
