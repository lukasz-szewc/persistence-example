package org.luksze;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.luksze.config.Configuration;

import javax.persistence.*;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class TransactionRollbackAfterFlushTest {
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("test-pu", new Configuration());
        entityManager = entityManager();
    }

    @Test
    public void transactionCanRollBackDespiteManualFlushes() throws Exception {
        //given active transaction with manual flush into database
        activeTransactionAlreadyFlushedIntoDatabase();

        //when transaction rolls back
        transactionRollsBack();

        //then
        thereIsNoPersistedObjectInDatabase();
    }

    @After
    public void cleanup() throws Exception {
        System.out.println("Closing:");

        EntityTransaction transaction = entityManager.getTransaction();

        System.out.println("Transaction state: " + transaction.isActive());
        entityManager.close();
        System.out.println("Transaction state: " + transaction.isActive());
        entityManagerFactory.close();
    }

    private void transactionRollsBack() {
        EntityTransaction transaction = entityManager.getTransaction();
        presentTransaction(transaction);
        transaction.rollback();
        presentTransaction(transaction);
    }

    private EntityManager entityManager() {
        return entityManagerFactory.createEntityManager();
    }

    private void activeTransactionAlreadyFlushedIntoDatabase() {
        EntityTransaction transaction = entityManager.getTransaction();
        presentTransaction(transaction);
        transaction.begin();
        entityManager.persist(new VersionedPerson("John", "Smith"));
        entityManager.persist(new VersionedPerson("David", "Connor"));
        entityManager.flush();
    }

    private void presentTransaction(EntityTransaction transaction) {
        System.out.println("transaction state, active: " + transaction.isActive() + " id: " + transaction.hashCode() );
    }

    private void thereIsNoPersistedObjectInDatabase() {
        entityManager.clear();
        assertTrue(result(entityManager).isEmpty());
    }

    private List<VersionedPerson> result(EntityManager entityManager) {
        return entityManager.createQuery(query(), VersionedPerson.class).getResultList();
    }

    private String query() {
        return "select p from VersionedPerson p";
    }

}
