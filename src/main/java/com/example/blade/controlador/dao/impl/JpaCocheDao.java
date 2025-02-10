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
 * Implementación JPA de la interfaz {@link CocheDao} para realizar operaciones
 * CRUD sobre la entidad {@link Coche}
 *
 * Utiliza un {@link EntityManagerFactory} configurado para la unidad de
 * persistencia "default"
 */
public class JpaCocheDao implements CocheDao {

    // Instancia singleton de JpaCocheDao para asegurar que solo exista una única instancia en la aplicación
    private static JpaCocheDao jpaCocheDao = null;

    // Factoría de gestores de entidad, configurada con la unidad de persistencia "default"
    private final EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    /**
     * Constructor público por defecto
     */
    public JpaCocheDao() {
    }

    /**
     * Obtiene todos los coches almacenados en la base de datos
     * <p>
     * Crea un {@link EntityManager} y ejecuta una consulta JPQL para recuperar
     * todos los registros de la entidad {@link Coche}. Si ocurre algún error,
     * se captura y se lanza una excepción con un mensaje descriptivo.
     * </p>
     *
     * @return Una lista con todos los objetos {@link Coche}.
     * @throws Exception Si ocurre algún error durante la consulta o la
     * conexión
     */
    @Override
    public List<Coche> getAllCoches() throws Exception {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            return entityManager.createQuery("SELECT c FROM Coche c", Coche.class).getResultList();
        } catch (Exception e) {
            throw new Exception("Error inesperado al obtener los coches", e);
        }
    }

    /**
     * Actualiza un coche existente en la base de datos
     * <p>
     * Se busca el coche actual en la base de datos utilizando su ID. Si la
     * matrícula ha sido modificada, se verifica que no exista otro coche con la
     * misma matrícula para evitar duplicados. Si la verificación es correcta,
     * se inicia una transacción, se actualiza el coche utilizando el método
     * merge y se confirma la transacción
     * </p>
     *
     * @param coche Objeto {@link Coche} con los datos actualizados
     * @throws Exception Si el coche no existe, si la nueva matrícula ya está en
     * uso o si ocurre un error durante la actualización
     */
    @Override
    public void updateCoche(Coche coche) throws Exception {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            Coche cocheActual = entityManager.find(Coche.class, coche.getId());
            if (cocheActual == null) {
                throw new Exception("Coche no encontrado con ID: " + coche.getId());
            }

            if (!cocheActual.getMatricula().equals(coche.getMatricula())) {
                boolean matriculaDuplicada = !entityManager.createQuery(
                        "SELECT c FROM Coche c WHERE c.matricula = :matricula AND c.id != :id", Coche.class)
                        .setParameter("matricula", coche.getMatricula())
                        .setParameter("id", coche.getId())
                        .getResultList()
                        .isEmpty();

                if (matriculaDuplicada) {
                    throw new Exception("Error: No se puede duplicar la matrícula " + coche.getMatricula());
                }
            }

            entityManager.getTransaction().begin();
            entityManager.merge(coche);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new Exception("Error al actualizar el coche: " + e.getMessage(), e);
        }
    }

    /**
     * Agrega un nuevo coche a la base de datos
     * <p>
     * Antes de persistir el nuevo objeto {@link Coche}, se ejecuta una consulta
     * para verificar que no exista otro coche con la misma matrícula. Si se
     * detecta duplicidad, se lanza una excepción. De lo contrario, se inicia
     * una transacción, se persiste el objeto y se confirma la transacción
     * </p>
     *
     * @param coche Objeto {@link Coche} que se desea agregar.
     * @throws SQLException Si se detecta una matrícula duplicada o si ocurre un
     * error al persistir el coche
     */
    @Override
    public void addCoche(Coche coche) throws SQLException {
        try (EntityManager gestorEntidades = entityManagerFactory.createEntityManager()) {

            boolean matriculaDuplicada = !gestorEntidades.createQuery(
                    "SELECT c FROM Coche c WHERE c.matricula = :matricula", Coche.class)
                    .setParameter("matricula", coche.getMatricula())
                    .getResultList()
                    .isEmpty();

            if (matriculaDuplicada) {
                throw new SQLException("Error: No se puede duplicar la matrícula " + coche.getMatricula());
            }

            gestorEntidades.getTransaction().begin();
            gestorEntidades.persist(coche);
            gestorEntidades.getTransaction().commit();

        } catch (Exception e) {
            throw new SQLException("Error al agregar el coche: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un coche de la base de datos
     * <p>
     * Para eliminar un coche se realiza un merge del objeto recibido para
     * asegurar que esté gestionado por el contexto de persistencia. Luego, se
     * inicia una transacción, se elimina el objeto y se confirma la
     * transacción
     * </p>
     *
     * @param coche Objeto {@link Coche} que se desea eliminar.
     * @throws Exception Si ocurre algún error durante el proceso de
     * eliminación
     */
    @Override
    public void delete(Coche coche) throws Exception {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {

            entityManager.getTransaction().begin();
            Coche cocheConectado = entityManager.merge(coche);
            entityManager.remove(cocheConectado);
            entityManager.getTransaction().commit();

        } catch (Exception e) {
            throw new SQLException("Error al eliminar el coche: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un coche en la base de datos utilizando su matrícula
     * <p>
     * Ejecuta una consulta JPQL que filtra los registros de {@link Coche} por
     * el campo matrícula. Si no se encuentra ningún registro, se lanza una
     * excepción indicando que no existe el coche
     * </p>
     *
     * @param matricula Matrícula del coche a buscar.
     * @return Objeto {@link Coche} que coincide con la matrícula indicada.
     * @throws Exception Si no se encuentra el coche o si ocurre un error
     * durante la consulta
     */
    @Override
    public Coche findByMatricula(String matricula) throws Exception {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            // Ejecuta la consulta y retorna el único resultado.
            return entityManager.createQuery("SELECT c FROM Coche c WHERE c.matricula = :matricula", Coche.class)
                    .setParameter("matricula", matricula)
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new Exception("No existe el coche con la matrícula: " + matricula, e);
        } catch (Exception e) {
            throw new Exception("Error al buscar el coche: " + e.getMessage(), e);
        }
    }

    /**
     * Retorna la instancia singleton de {@link JpaCocheDao}.
     * <p>
     * Este método implementa el patrón singleton para garantizar que solo
     * exista una única instancia de esta clase durante la vida de la
     * aplicación, facilitando el acceso centralizado
     * </p>
     *
     * @return Instancia única de {@link JpaCocheDao}
     */
    public static JpaCocheDao instancia() {
        if (jpaCocheDao == null) {
            jpaCocheDao = new JpaCocheDao();
        }
        return jpaCocheDao;
    }
}
