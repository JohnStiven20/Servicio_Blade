package com.example.blade.controlador.dao.impl;

import java.sql.SQLException;
import java.util.List;

import com.example.blade.controlador.dao.CocheDao;
import com.example.blade.modelo.Coche;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

/**
 * Implementación de la interfaz {@link CocheDao} utilizando JPA.
 * <p>
 * Esta clase se encarga de realizar operaciones CRUD (crear, leer, actualizar y
 * eliminar) sobre la entidad {@link Coche} mediante el uso de un
 * {@link EntityManagerFactory}. Se utiliza la unidad de persistencia nombrada
 * "default".
 * </p>
 * 
 * @author Jonh Stiven Solano Macas
 *        
 */
public class JpaCocheDao implements CocheDao {

    /**
     * Instancia única (singleton) de {@code JpaCocheDao}.
     */
    private static JpaCocheDao jpaCocheDao = null;

    /**
     * Factoría de gestores de entidad (EntityManagerFactory) configurada para
     * la unidad de persistencia "default".
     */
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    /**
     * Constructor público por defecto.
     */
    public JpaCocheDao() {
    }

    /**
     * Recupera todos los coches almacenados en la base de datos.
     * <p>
     * Este método utiliza un {@link EntityManager} para ejecutar una consulta
     * JPQL que obtiene todos los objetos {@link Coche} existentes.
     * </p>
     *
     * @return una lista de todos los objetos {@link Coche}.
     * @throws Exception si ocurre algún error inesperado al obtener los coches.
     */
    @Override
    public List<Coche> getAllCoches() throws Exception {

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            List<Coche> coches = entityManager.createQuery("SELECT c FROM Coche c", Coche.class).getResultList();
            return coches;
            
        } catch (Exception e) {
            throw new Exception("Error inesperado al obtener los coches", e);
        }
    }

    /**
     * Actualiza un coche existente en la base de datos.
     * <p>
     * Se busca el coche por su identificador y se actualizan sus campos (marca,
     * modelo y matrícula) con los valores proporcionados en el objeto pasado
     * como parámetro. Además, si se intenta cambiar la matrícula, se verifica que no
     * exista otro coche con la misma matrícula.
     * </p>
     *
     * @param coche el objeto {@link Coche} que contiene la información actualizada.
     * @return el objeto {@link Coche} actualizado.
     * @throws Exception si ocurre un error al actualizar el coche.
     */
    @Override
    public Coche updateCoche(Coche coche) throws Exception {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            Coche currentCar = entityManager.find(Coche.class, coche.getId());
            if (currentCar == null) {
                throw new Exception("El coche con ID " + coche.getId() + " no existe");
            }

            if (!currentCar.getMatricula().equals(coche.getMatricula())) {
                try {

                    entityManager.createQuery(
                            "SELECT c FROM Coche c WHERE c.matricula = :matricula AND c.id != :id", Coche.class)
                            .setParameter("matricula", coche.getMatricula())
                            .setParameter("id", coche.getId())
                            .getSingleResult();

                    throw new Exception("Error: No se puede duplicar la matrícula " + coche.getMatricula());
                } catch (NoResultException e) {
                    // No se encontró duplicado, se puede continuar con la actualización
                }
            }

            entityManager.getTransaction().begin();
            Coche cocheActualizado = entityManager.merge(coche);
            entityManager.getTransaction().commit();
            return cocheActualizado;
            
        } catch (SQLException e) {
            throw new Exception(e.getMessage(), e);
        }
    }

    /**
     * Agrega un nuevo coche a la base de datos.
     * <p>
     * Antes de persistir el nuevo objeto {@link Coche}, se verifica que no
     * exista ya otro coche con la misma matrícula. Si se encuentra un coche con
     * la misma matrícula, se lanza una {@link SQLException} indicando que no se
     * puede duplicar la matrícula.
     * </p>
     *
     * @param coche el objeto {@link Coche} que se desea agregar.
     * @throws SQLException si se detecta una matrícula duplicada o si ocurre
     * algún error al persistir el coche.
     */
    @Override
    public void addCoche(Coche coche) throws SQLException {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            Coche cocheExistente = null;
            try {

                cocheExistente = entityManager.createQuery(
                        "SELECT c FROM Coche c WHERE c.matricula = :matricula", Coche.class)
                        .setParameter("matricula", coche.getMatricula())
                        .getSingleResult();
            } catch (NoResultException e) {

            }

            if (cocheExistente != null) {
                throw new SQLException("Error: No se puede duplicar la matrícula " + coche.getMatricula());
            }

            entityManager.getTransaction().begin();
            entityManager.persist(coche);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    /**
     * Elimina un coche de la base de datos.
     * <p>
     * Para eliminar el coche se realiza un merge del objeto proporcionado para
     * asegurarse de que esté gestionado por el contexto de persistencia y, a
     * continuación, se procede a su eliminación.
     * </p>
     *
     * @param coche el objeto {@link Coche} que se desea eliminar.
     * @throws Exception si ocurre algún error durante la eliminación.
     */
    @Override
    public void delete(Coche coche) throws Exception {
        try {
            try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

                entityManager.getTransaction().begin();
                Coche cocheConectado = entityManager.merge(coche);
                entityManager.remove(cocheConectado);
                entityManager.getTransaction().commit();
            }
        } catch (Exception e) {
            throw new SQLException("Error al eliminar el coche");
        }
    }

    /**
     * Retorna una instancia singleton de {@link JpaCocheDao}.
     * <p>
     * Este método implementa el patrón singleton para garantizar que solo
     * exista una única instancia de {@code JpaCocheDao} durante la vida de la
     * aplicación.
     * </p>
     *
     * @return la instancia única de {@link JpaCocheDao}.
     */
    public static JpaCocheDao instancia() {
        if (jpaCocheDao == null) {
            jpaCocheDao = new JpaCocheDao();
            return jpaCocheDao;
        }
        return jpaCocheDao;
    }
}
