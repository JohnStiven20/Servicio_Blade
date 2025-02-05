package com.example.blade.modelo;

import java.util.Date;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Coche {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    int id;
    String matricula;
    String marca;
    String modelo;
    Date date;
     
}
