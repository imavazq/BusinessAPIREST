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
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"article", "size"}))
//Relaciona con tabla de BD - UNIQUE(articulo,talle)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//SERIAL en pgsql/autoincrement
    private Long id;

    @Column(nullable = false, length = 10)//NOT NULL - VARCHAR(10)
    private String article;

    @Column(nullable = false, length = 5)//NOT NULL - VARCHAR(5)
    private String size;

    @Column(length = 100)//NULLABLE y VARCHAR(100)
    private String description;

    @Column(nullable = false)//NOT NULL
    private Float price;

    @Column(name = "stock_available", nullable = false)//NOT NULL
    private Integer stockAvailable;

    @Column(name = "additional_notes")//NULLABLE y VARCHAR(255) (default)
    private String additionalNotes;

    //Si no especifico CASCADE, no afecta a ProductTypeEntity
    @OneToMany//Un Product tiene un sólo ProductType / Un Supplier tiene o no varios ProductType)
    @JoinColumn(name = "product_type_id", nullable = false)//FK - Nombre de campo en la BD - NOT NULL
    private ProductTypeEntity productTypeId;

    //Si no especifico CASCADE, no afecta a SupplierEntity
    @OneToMany//Un Product tiene un sólo Supplier / Un Supplier tiene o no varios Products)
    @JoinColumn(name = "supplier_id", nullable = false)//FK - Nombre de campo en la BD - NOT NULL
    private SupplierEntity supplier;
}
