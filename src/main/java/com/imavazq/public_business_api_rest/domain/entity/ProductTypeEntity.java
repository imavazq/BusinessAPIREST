package com.imavazq.public_business_api_rest.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data//Crea getter,setters,equals,hashcodes
@NoArgsConstructor
@AllArgsConstructor
@Builder//Para instanciar rápido una clase
@Entity//Permite que se pueda trabajar la clase con JPA
@Table(name = "product_types")//Relaciona con tabla de BD
public class ProductTypeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//SERIAL en pgsql/autoincrement
    private Long id;

    @Column(length = 100, nullable = false, unique = true)//VARCHAR(100) - NOT NULL - UNIQUE
    private String description;
}
