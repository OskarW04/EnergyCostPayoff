package com.example.lab03_pop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double amount;
    private LocalDate paymentDate;
    private boolean payed;

    @ManyToOne
    @JsonIgnore
    private Tenant tenant;
    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;
}
