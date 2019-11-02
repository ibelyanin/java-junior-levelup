package com.levelup.juniorjava.dao;

import com.levelup.juniorjava.entities.Account;
import com.levelup.juniorjava.entities.Transaction;

import javax.persistence.EntityManager;
import java.util.List;

public class TransactionDAO {
    private EntityManager manager;
    public TransactionDAO(EntityManager manager) {
        this.manager = manager;
    }

    public void create(Transaction transaction) {
        if(transaction.getOrigin() == transaction.getReceiver()) {
            throw new IllegalArgumentException("Transaction receiver and origin accounts are equal");
        }
        if(transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Transaction amount is less than zero");
        }
        manager.persist(transaction);
    }

    public List<Transaction> findByOrigin(Account origin) {
        return manager.createQuery("from Transaction where origin.id = :p")
                .setParameter("p", origin.getId())
                .getResultList();
    }
    public List<Transaction> findByReceiver(Account receiver) {
        return manager.createQuery("from Transaction where origin.id = :p")
                .setParameter("p", receiver.getId())
                .getResultList();
    }
    public List<Transaction> findByAccount(Account account) {
        return manager.createQuery("from Transaction where origin.id = :p or receiver.id = :p")
                .setParameter("p", account.getId())
                .getResultList();
    }

}
