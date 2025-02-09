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

/**
 * Representa la entidad Coche en la base de datos.
 * <p>
 * Esta clase modela un coche con atributos como matrícula, marca, modelo
 * y la fecha de registro. Se gestiona con JPA y Lombok para reducir el código
 * repetitivo.
 * </p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Coche {

    /** 
     * Identificador único del coche. Se genera automáticamente 
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    /** 
     * Matrícula del coche, debe ser única
    */
    private String matricula;

    /** 
     * Marca del coche (ejemplo: Toyota, Ford, etc)
     */
    private String marca;

    /** 
     * Modelo del coche (ejemplo: Corolla, Focus, etc)
     */
    private String modelo;

    /** 
     * Fecha de registro del coche
     */
    private Date date;
}
