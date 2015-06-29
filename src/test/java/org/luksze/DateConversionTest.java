package org.luksze;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DateConversionTest {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("date-pu");
    }

    @Test
    public void testName() throws Exception {
        //given
        DateAndTimeEntity persisted = persistedDateAndTimeObject();

        //when
        DateAndTimeEntity fetched = objectIsFetchedAgainFromDatabase(persisted);

        //when
        Assert.assertEquals(fetched.id(), persisted.id());
        Assert.assertEquals(fetched.localDate(), persisted.localDate());
    }

    @After
    public void cleanUp() throws Exception {
        entityManagerFactory.close();
    }

    private DateAndTimeEntity objectIsFetchedAgainFromDatabase(DateAndTimeEntity dateAndTimeEntity) {
        return entityManagerFactory.createEntityManager().
                find(DateAndTimeEntity.class, dateAndTimeEntity.id());

    }

    private DateAndTimeEntity persistedDateAndTimeObject() {
        DateAndTimeEntity dateAndTimeEntity = new DateAndTimeEntity();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(dateAndTimeEntity);
        entityManager.getTransaction().commit();
        return dateAndTimeEntity;
    }
}