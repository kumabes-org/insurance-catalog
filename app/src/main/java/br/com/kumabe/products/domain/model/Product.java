package br.com.kumabe.catalogs.domain.model;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString(of = {"id"})
@EqualsAndHashCode
public class Product {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String size;
    private Double weight;
    private String color;
}
