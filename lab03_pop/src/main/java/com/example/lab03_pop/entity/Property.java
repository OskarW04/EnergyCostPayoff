package com.example.lab03_pop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JsonBackReference
    private Building building;

    @ManyToOne
    @JsonBackReference
    private Tenant tenant;

    private Double readingValue;
    private Double previousReadingValue;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Payment> payments;

    @JsonInclude
    public Map<String, Object> getTenantDetails() {
        if (tenant == null) {
            return null;
        }
        Map<String, Object> details = new HashMap<>();
        details.put("id", tenant.getId());
        details.put("username", tenant.getUsername());
        return details;
    }
}
