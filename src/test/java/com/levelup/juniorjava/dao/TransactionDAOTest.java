package com.levelup.juniorjava.dao;


import com.levelup.juniorjava.entities.Account;
import com.levelup.juniorjava.entities.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import java.util.Date;


public class TransactionDAOTest {

    private EntityManagerFactory factory;
    private EntityManager manager;
    private TransactionDAO dao;
    private AccountsDAO daoAc;

    @Before
    public void setup() {
        factory = Persistence.createEntityManagerFactory("TestPersistenceUnit");
        manager = factory.createEntityManager();
        dao = new TransactionDAO(manager);
    }

    @After
    public void cleanup() {
        if (manager != null) {
            manager.close();
        }

        if (factory != null) {
            factory.close();
        }
    }

    @Test
    public void create() {

        Account origin = new Account("123","123123");
        Account receiver = new Account("321","32131");
        Transaction ts = new Transaction(new Date(),10,origin,receiver);
        manager.getTransaction().begin();
        try {
            manager.persist(origin);
            manager.persist(receiver);
            dao.create(ts);
            manager.getTransaction().commit();
        }
        catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

    }

    @Test
    public void findByOrigin() {
        Account origin = new Account("123","123123");
        Account receiver = new Account("321","32131");
        Transaction ts = new Transaction(new Date(),10,origin,receiver);
        manager.getTransaction().begin();
        try {
            manager.persist(origin);
            manager.persist(receiver);
            dao.create(ts);
            manager.getTransaction().commit();
        }
        catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

        Assert.assertEquals(ts.getId(),dao.findByOrigin(origin).get(0).getId());
        Assert.assertEquals(0,dao.findByOrigin(receiver).size());

    }

    @Test
    public void findByReceiver() {
        Account origin = new Account("123","123123");
        Account receiver = new Account("321","32131");
        Transaction ts = new Transaction(new Date(),10,origin,receiver);
        manager.getTransaction().begin();
        try {
            manager.persist(origin);
            manager.persist(receiver);
            dao.create(ts);
            manager.getTransaction().commit();
        }
        catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

        Assert.assertEquals(ts.getId(),dao.findByReceiver(receiver).get(0).getId());
        Assert.assertEquals(0,dao.findByReceiver(origin).size());

    }

    @Test
    public void findByAccount() {
        Account origin = new Account("123","123123");
        Account receiver = new Account("321","32131");
        Transaction ts = new Transaction(new Date(),10,origin,receiver);
        manager.getTransaction().begin();
        try {
            manager.persist(origin);
            manager.persist(receiver);
            dao.create(ts);
            manager.getTransaction().commit();
        }
        catch (Exception e) {
            manager.getTransaction().rollback();
            throw e;
        }

        Assert.assertEquals(ts.getId(),dao.findByAccount(origin).get(0).getId());
        Assert.assertEquals(ts.getId(),dao.findByAccount(receiver).get(0).getId());

    }
}
