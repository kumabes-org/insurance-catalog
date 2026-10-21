package br.com.kumabe.insurance.catalog.adapters.in.rest;

import br.com.kumabe.insurance.catalog.domain.model.Coverage;
import br.com.kumabe.insurance.catalog.domain.model.InsuranceProduct;
import br.com.kumabe.insurance.catalog.domain.model.ProductId;
import br.com.kumabe.insurance.catalog.domain.ports.in.CreateProductUseCase;
import br.com.kumabe.insurance.catalog.domain.ports.in.GetProductDetailsQuery;
import br.com.kumabe.insurance.catalog.domain.ports.in.dto.CreateProductCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductCatalogApiController.class)
class ProductCatalogApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateProductUseCase createProductUseCase;

    @MockitoBean
    private GetProductDetailsQuery getProductDetailsQuery;

    @Test
    void shouldReturnProductDetailsWhenProductExists() throws Exception {
        UUID productUuid = UUID.randomUUID();
        ProductId productId = new ProductId(productUuid);
        InsuranceProduct product = new InsuranceProduct(productId, "AUTO_STD", "Auto Standard", "AUTO");
        product.addCoverage(new Coverage("COLLISION", "Collision", true));

        when(getProductDetailsQuery.findById(new ProductId(productUuid)))
                .thenReturn(Optional.of(product));

        mockMvc.perform(get("/api/v1/products/{productId}", productUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productUuid.toString()))
                .andExpect(jsonPath("$.code").value("AUTO_STD"))
                .andExpect(jsonPath("$.name").value("Auto Standard"))
                .andExpect(jsonPath("$.lineOfBusiness").value("AUTO"))
                .andExpect(jsonPath("$.status").value("DRAFT"))
                .andExpect(jsonPath("$.coverages[0].code").value("COLLISION"))
                .andExpect(jsonPath("$.coverages[0].name").value("Collision"))
                .andExpect(jsonPath("$.coverages[0].mandatory").value(true));
    }

    @Test
    void shouldReturnNotFoundWhenProductDoesNotExist() throws Exception {
        UUID productUuid = UUID.randomUUID();
        when(getProductDetailsQuery.findById(new ProductId(productUuid)))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/products/{productId}", productUuid))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve criar um produto com sucesso e retornar 201 com header Location")
    void shouldCreateProductSuccessfully() throws Exception {
        ProductId createdId = ProductId.generate();
        when(createProductUseCase.createProduct(any(CreateProductCommand.class))).thenReturn(createdId);

        String payload = """
            {
              "code": "HOME_PREMIUM",
              "name": "Residencial Premium",
              "lineOfBusiness": "HOME",
              "coverages": [
                {
                  "code": "COV_FIRE_LIGHTNING",
                  "name": "Incêndio, Queda de Raio e Explosão",
                  "mandatory": true
                }
              ]
            }
            """;

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/products/" + createdId.value()))
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Deve falhar com 400 Bad Request se a lista de coberturas for vazia")
    void shouldFailWhenCoveragesEmpty() throws Exception {
        String invalidPayload = """
            {
              "code": "HOME_INVALID",
              "name": "Sem Cobertura",
              "lineOfBusiness": "HOME",
              "coverages": []
            }
            """;

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }
}
