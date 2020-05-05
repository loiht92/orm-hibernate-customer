package com.codegym.service;
import com.codegym.model.Customer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerServiceImpl implements ICustomerService {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;

    static {
        try {
            sessionFactory = new Configuration().configure("./hibernate.conf.xml").buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        }catch (HibernateException e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> findAll() {
        String query = "SELECT c FROM Customer AS c";
        TypedQuery<Customer> customerTypedQuery = entityManager.createQuery(query, Customer.class);
        return customerTypedQuery.getResultList();
    }

    @Override
    public Customer findById(Long id) {
        String query = "SELECT c FROM Customer AS c WHERE c.id =: id";
        TypedQuery<Customer> customerTypedQuery = entityManager.createQuery(query, Customer.class);
        customerTypedQuery.setParameter("id", id);
        return customerTypedQuery.getSingleResult();
    }

    @Override
    public Customer save(Customer customer) {

        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            if (customer.getId() == null){
                session.save(customer);
                transaction.commit();
                return customer;
            }
            Customer newCustomer = this.findById(customer.getId());
            newCustomer.setName(customer.getName());
            newCustomer.setEmail(customer.getEmail());
            newCustomer.setAddress(customer.getAddress());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        Customer customer = findById(id);
        entityManager.getTransaction().begin();
        entityManager.remove(customer);
        entityManager.getTransaction().commit();

    }
}
