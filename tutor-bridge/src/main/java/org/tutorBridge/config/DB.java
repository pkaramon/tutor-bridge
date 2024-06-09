package org.tutorBridge.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.function.Consumer;
import java.util.function.Function;

public class DB {
    private static final EntityManagerFactory entityManagerFactory = buildEntityManagerFactory();

    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            return Persistence.createEntityManagerFactory("my-persistence-unit");
        } catch (Throwable ex) {
            System.err.println("Initial EntityManagerFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }
    public static void close() {
        entityManagerFactory.close();
    }

    static public void inTransaction(Consumer<EntityManager> action) {
        EntityManager entityManager = DB.getEntityManager();
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();
            action.accept(entityManager);
            tx.commit();
        } catch (RuntimeException e) {
            tx.rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    static public <R> R withEntityManger(Function<EntityManager, R> action) {
        EntityManager entityManager = DB.getEntityManager();
        try {
            return action.apply(entityManager);
        } finally {
            entityManager.close();
        }
    }
}
