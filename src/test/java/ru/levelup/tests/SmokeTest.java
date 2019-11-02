package ru.levelup.tests;

import org.junit.*;
import com.levelup.juniorjava.entities.Account;
import com.levelup.juniorjava.entities.Transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

public class SmokeTest {
    private EntityManagerFactory factory;
    private EntityManager manager;

    @Before
    public void setup() {
        factory = Persistence.createEntityManagerFactory("TestPersistenceUnit");
        manager = factory.createEntityManager();
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
    public void testCreateAccount() {
        Account account = new Account();
        account.setLogin("test");
        account.setPassword("qweerty");

        manager.getTransaction().begin();
        try {
            manager.persist(account);
            manager.getTransaction().commit();
        }
        catch (Exception e) {
            manager.getTransaction().rollback();
            //throw e;
        }
        //достать объект по ключу
        Assert.assertNotNull(manager.find(Account.class, account.getId()));

    }

    @Test
    public void queryAccount() {
        Account account = new Account();
        account.setLogin("test");
        account.setPassword("qwerty");

        manager.getTransaction().begin();
        try {
            manager.persist(account);
            manager.getTransaction().commit();
        }
        catch (Exception e) {
            manager.getTransaction().rollback();
            //throw e;
        }
        //В качестве второго параметра можно указать возвращаемый класс, чтобы ретурнился нужный тип
        Account found = manager.createQuery("from Account where login = :p", Account.class)
                //устанавливаме значение параметра
                .setParameter("p", "test")
                //вернуть один результат
                .getSingleResult();

        Assert.assertEquals("qwerty", found.getPassword());
        Assert.assertEquals(account.getId(), found.getId());
    }

    @Test
    public void queryTransaction() {
        Account account = new Account();
        account.setLogin("test");
        account.setPassword("qwerty");

        Account receiver = new Account();
        account.setLogin("test");
        account.setPassword("qwerty");

        Transaction ts = new Transaction();
        ts.setAmount(10);
        ts.setOrigin(account);
        ts.setReceiver(receiver);
        ts.setTime(new Date());

        manager.getTransaction().begin();
        try {
            manager.persist(account);
            manager.persist(receiver);
            manager.persist(ts);
            manager.getTransaction().commit();
        }
        catch (Exception e) {
            manager.getTransaction().rollback();
            //throw e;
        }
        //В качестве второго параметра можно указать возвращаемый класс, чтобы ретурнился нужный тип
                List<Transaction> found = manager.createQuery("from Transaction t where t.origin.login = :p", Transaction.class)
                //устанавливаме значение параметра
                .setParameter("p", "test")
                //вернуть result set
                .getResultList();

        Assert.assertEquals(1, found.size());
        Assert.assertEquals(ts.getId(), found.get(0).getId());
    }

    @Test
    @Ignore
    public void criteriaBuilder() {
        Account account = new Account();
        account.setLogin("test");
        account.setPassword("qweerty");

        manager.getTransaction().begin();
        try {
            manager.persist(account);
            manager.getTransaction().commit();
        }
        catch (Exception e) {
            manager.getTransaction().rollback();
            //throw e;
        }

        CriteriaBuilder builder = factory.getCriteriaBuilder();
        CriteriaQuery<Account> query =  builder.createQuery(Account.class);
        Root<Account> root = query.from(Account.class);
        query.select(root);
        query.where(
                builder.equal(root.get("login"), "test")
        );

        Account found = manager.createQuery(query).getSingleResult();

        Assert.assertEquals(account.getId(), found.getId());
    }

}
