package com.example.blade.controlador.dao;

import java.sql.SQLException;
import java.util.List;

import com.example.blade.modelo.Coche;

import jakarta.persistence.NoResultException;


public interface CocheDao {

    Coche getCocheById(int cod) throws Exception, NoResultException;
    Iterable<String> getAllNombresMarcas();
    List<Coche> getAllCoches() throws SQLException;
    Coche updateCoche(Coche coche)  throws Exception;
    void addCoche(int cod, String matricula, String marca, String modelo);
    boolean addCoche(Coche coche);
    boolean delete(Coche coche)  throws Exception;

}
