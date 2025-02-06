package com.example.blade.controlador.dao;

import java.net.SocketException;
import java.sql.SQLException;
import java.util.List;

import com.example.blade.modelo.Coche;

public interface CocheDao {

    List<Coche> getAllCoches() throws SQLException , SocketException;
    Coche updateCoche(Coche coche)  throws Exception;
    boolean addCoche(Coche coche);
    boolean delete(Coche coche)  throws Exception;

}
