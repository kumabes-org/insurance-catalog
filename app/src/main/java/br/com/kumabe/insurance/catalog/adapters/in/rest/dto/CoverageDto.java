package br.com.kumabe.insurance.catalog.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record CoverageDto(@NotBlank String code, @NotBlank String name, boolean mandatory) {}
