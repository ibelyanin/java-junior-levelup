package com.levelup.juniorjava.dao;

import com.levelup.juniorjava.entities.Account;

import javax.persistence.EntityManager;

public class AccountsDAO {

    private final EntityManager manager;
    public  AccountsDAO(EntityManager manager) {
        this.manager = manager;
    }

    public void create(Account account) {
        manager.persist(account);
    }

    public  Account findByLogin(String login) {
        return manager.createQuery("from Account where login = :p", Account.class)
                //устанавливаме значение параметра
                .setParameter("p", login)
                //вернуть один результат
                .getSingleResult();
    }

    public  Account findByLoginAndPassword(String login, String password) {
        return manager.createQuery("from Account where login = :login AND password = :password", Account.class)
                //устанавливаме значение параметра
                .setParameter("login", login)
                .setParameter("password", password)
                //вернуть один результат
                .getSingleResult();
    }



}
