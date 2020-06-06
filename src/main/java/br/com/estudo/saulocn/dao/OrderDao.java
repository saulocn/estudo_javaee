package br.com.estudo.saulocn.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.estudo.saulocn.model.Order;

@Stateless
public class OrderDao {

    @PersistenceContext(unitName = "books")
    private EntityManager entityManager;

    public List<Order> list() {
        return entityManager.createQuery("select b from Order b", Order.class).getResultList();
    }

    public void save(final Order order) {
        entityManager.persist(order);
    }

    public Order getById(final int id) {
        return entityManager.find(Order.class, id);
    }

    public void update(final Order order) {
        entityManager.merge(order);
    }

    public void sendToPayment(final Order order) {
        order.setSentToPayment(true);
        update(order);
    }

    public void delete(final int id) {
        final Order order = this.getById(id);
        entityManager.remove(order);
    }

    public void pay(final Order order) {
        order.setPaid(true);
        update(order);
    }
}
