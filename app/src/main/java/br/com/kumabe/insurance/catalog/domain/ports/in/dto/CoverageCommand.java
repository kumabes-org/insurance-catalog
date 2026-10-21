package br.com.kumabe.insurance.catalog.domain.ports.in.dto;

public record CoverageCommand(String code, String name, boolean mandatory) {}