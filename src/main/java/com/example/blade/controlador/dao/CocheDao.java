package com.example.blade.controlador.dao;

import java.sql.SQLException;
import java.util.List;

import com.example.blade.modelo.Coche;

public interface CocheDao {

    List<Coche> getAllCoches() throws Exception;
    Coche updateCoche(Coche coche)  throws SQLException, Exception;
    void  addCoche(Coche coche) throws SQLException;
    void  delete(Coche coche)  throws Exception;

}
