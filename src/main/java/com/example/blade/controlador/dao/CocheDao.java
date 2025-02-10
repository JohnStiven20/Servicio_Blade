package com.example.blade.controlador.dao;

import java.sql.SQLException;
import java.util.List;

import com.example.blade.modelo.Coche;

/**
 * Interfaz para operaciones básicas de acceso a datos de {@link Coche}.
 */
public interface CocheDao {

    /**
     * Retorna todos los coches.
     * @return Lista de coches.
     * @throws Exception Si falla la consulta.
     */
    List<Coche> getAllCoches() throws Exception;

    /**
     * Actualiza un coche existente.
     * @param coche Coche con datos actualizados.
     * @throws SQLException Si hay error en la BD.
     * @throws Exception Si falla la actualización.
     */
    void updateCoche(Coche coche) throws SQLException, Exception;

    /**
     * Agrega un coche nuevo.
     * @param coche Coche a insertar.
     * @throws SQLException Si falla la inserción.
     */
    void addCoche(Coche coche) throws SQLException;

    /**
     * Elimina un coche.
     * @param coche Coche a borrar.
     * @throws Exception Si falla la eliminación.
     */
    void delete(Coche coche) throws Exception;

    /**
     * Busca un coche por matrícula.
     * @param matricula Matrícula a buscar.
     * @return Coche encontrado o null.
     * @throws Exception Si falla la búsqueda.
     */
    public Coche findByMatricula(String matricula) throws Exception;
}
