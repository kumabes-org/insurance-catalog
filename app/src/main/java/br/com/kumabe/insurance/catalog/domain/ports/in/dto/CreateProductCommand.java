package br.com.kumabe.insurance.catalog.domain.ports.in.dto;

import br.com.kumabe.insurance.catalog.domain.ports.in.CreateProductUseCase;

import java.util.List;

public record CreateProductCommand(
        String code,
        String name,
        String lineOfBusiness,
        List<CoverageCommand> initialCoverages
) {}