package edu.ijse.gamingLounge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StationType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String typeName;
    private Double hourlyRate;
    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean active = true;
}
