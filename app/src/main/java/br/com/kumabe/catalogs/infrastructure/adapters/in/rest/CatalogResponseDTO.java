package br.com.kumabe.catalogs.infrastructure.adapters.in.rest;

import java.time.LocalDateTime;
import java.util.Set;

public record CatalogResponseDTO(
    String id,
    String name,
    LocalDateTime createdAt,
    Boolean active,
    Set<String> offers) {}
