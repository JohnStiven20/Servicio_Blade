package com.example.blade.controlador.dao;

import java.sql.SQLException;
import java.util.List;
import com.example.blade.modelo.Coche;

/**
 * Interfaz que define las operaciones básicas de acceso a datos para la entidad {@link Coche}.
 */
public interface CocheDao {

    /**
     * Obtiene una lista con todos los coches almacenados en la base de datos.
     *
     * @return Una lista de objetos {@link Coche}.
     * @throws Exception Si ocurre un error al recuperar los datos.
     */
    List<Coche> getAllCoches() throws Exception;

    /**
     * Actualiza un coche existente en la base de datos.
     *
     * @param coche El objeto {@link Coche} con la información actualizada.
     * @throws SQLException Si ocurre un error relacionado con la base de datos.
     * @throws Exception Si ocurre cualquier otro error durante la actualización.
     */
    void updateCoche(Coche coche) throws SQLException, Exception;

    /**
     * Agrega un nuevo coche a la base de datos.
     *
     * @param coche El objeto {@link Coche} que se desea agregar.
     * @throws SQLException Si ocurre un error al insertar el coche en la base de datos.
     */
    void addCoche(Coche coche) throws SQLException;

    /**
     * Elimina un coche de la base de datos.
     *
     * @param coche El objeto {@link Coche} que se desea eliminar.
     * @throws Exception Si ocurre un error durante la eliminación.
     */
    void delete(Coche coche) throws Exception;
}
