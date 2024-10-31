package com.imavazq.public_business_api_rest.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data//Crea getter,setters,equals,hashcodes
@NoArgsConstructor
@AllArgsConstructor
@Builder//Para instanciar rápido una clase
@Entity//Permite que se pueda trabajar la clase con JPA
@Table(name = "entries")//Relaciona con tabla de BD
public class EntryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//SERIAL en pgsql/autoincrement
    private Long id;

    @Column(nullable = false)//NOT NULL
    private Integer amount;

    @Column(name = "unit_cost", nullable = false)//NOT NULL
    private Float unitCost;

    @Column(name = "total_cost", nullable = false)//NOT NULL
    private Float totalCost;

    @Column(columnDefinition = "TIMESTAMP(0)")//especifica que no almacena con milisegundos y microsegundos
    private LocalDateTime date;//Se agrega desde service //Tiene este formato para que coincida con el de la BD

    //Si no especifico CASCADE, no afecta a ProductEntity
    @ManyToOne//Relación [Entry] >O---| [Product] (Un Product tiene o no varias Entry / Un Entry tiene un Product)
    @JoinColumn(name = "product_id", nullable = false)//FK - Nombre de campo en la BD - NOT NULL
    private ProductEntity product;
}
