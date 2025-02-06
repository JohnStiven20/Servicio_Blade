package com.example.blade.controlador.dao.impl;

import java.net.SocketException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.example.blade.controlador.dao.CocheDao;
import com.example.blade.modelo.Coche;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;

public class JpaCocheDao implements CocheDao {

    private static JpaCocheDao jpaCocheDao = null;
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("default");

    public JpaCocheDao() {
    }

    @Override
    public Coche getCocheById(int cod) throws Exception, NoResultException {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();

            Coche cocheEncontrado = entityManager
                    .createQuery("SELECT c FROM Coche c WHERE c.id = :cod", Coche.class)
                    .setParameter("cod", cod)
                    .getSingleResult();

            entityManager.getTransaction().commit();
            return cocheEncontrado;
        } catch (NoResultException e) {
            throw new NoResultException("No se ha encontrado el coche por id");
        } catch (Exception e) {
            throw new Exception("Error en algo", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Iterable<String> getAllNombresMarcas() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllNombresMarcas'");
    }

    @Override
    public List<Coche> getAllCoches() throws SQLException, SocketException {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            return entityManager.createQuery("SELECT c FROM Coche c", Coche.class).getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado", e);
        }
    }

    @Override
    public Coche updateCoche(Coche coche) throws Exception {

        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();

            entityManager.getTransaction().begin();

            Coche cocheEncontrado = entityManager.find(Coche.class, coche.getId());

            cocheEncontrado.setMarca(coche.getMarca());
            cocheEncontrado.setModelo(coche.getModelo());
            cocheEncontrado.setMatricula(coche.getMatricula());

            Coche cocheActulizado = entityManager.merge(cocheEncontrado);
            entityManager.getTransaction().commit();
            entityManager.close();
            return cocheActulizado;
        } catch (NoResultException e) {
            throw new Exception("No se encontrado el coche");
        }
    }

    @Override
    public boolean addCoche(Coche coche) {
        try {
            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            entityManager.persist(coche);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static JpaCocheDao instancia() {
        if (jpaCocheDao == null) {
            jpaCocheDao = new JpaCocheDao();
            return jpaCocheDao;
        }
        return jpaCocheDao;
    }

    @Override
    public void addCoche(int cod, String matricula, String marca, String modelo) {
        Coche coche = Coche.builder()
                .matricula(matricula)
                .marca(marca)
                .modelo(modelo)
                .date(new Date())
                .build();
        this.addCoche(coche);
    }

    @Override
    public boolean delete(Coche coche) throws Exception {
        try {

            EntityManager entityManager = entityManagerFactory.createEntityManager();
            entityManager.getTransaction().begin();
            Coche cocheConectado = entityManager.merge(coche);
            entityManager.remove(cocheConectado);
            entityManager.getTransaction().commit();
            entityManager.close();
            return true;
        } catch (Exception e) {
            throw new Exception("Algo ha ido mal");
        }
    }

}
