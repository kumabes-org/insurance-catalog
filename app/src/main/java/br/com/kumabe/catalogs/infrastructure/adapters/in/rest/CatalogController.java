package br.com.kumabe.catalogs.infrastructure.adapters.in.rest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CatalogController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogController.class.getName());

    @GetMapping("/api/v1/catalogs")
    public ResponseEntity<List<CatalogResponseDTO>> list() {
        LOGGER.info("Listing catalogs");
        List<CatalogResponseDTO> catalogs = List.of(
            new CatalogResponseDTO("1b2da7cc-b367-4196-8a78-9cfeec21f587", "Seguro de Vida", LocalDateTime.now(), true, Set.of("adc56d77-348c-4bf0-908f-22d402ee715c", "bdc56d77-348c-4bf0-908f-22d402ee715c", "cdc56d77-348c-4bf0-908f-22d402ee715c")),
            new CatalogResponseDTO("2b2da7cc-b367-4196-8a78-9cfeec21f587", "Seguro de Automovel", LocalDateTime.now(), false, Set.of("adc56d77-348c-4bf0-908f-22d402ee715c"))
        );
        return ResponseEntity.ok(catalogs);
    }
    
}
