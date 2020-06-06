package br.com.estudo.saulocn.service;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import br.com.estudo.saulocn.dao.OrderDao;
import br.com.estudo.saulocn.exceptions.PaymentErrorException;
import br.com.estudo.saulocn.jms.OrderPaymentProducer;
import br.com.estudo.saulocn.model.Order;

@Stateless
public class OrderService {

    @Inject
    private OrderDao orderDao;

    @Inject
    OrderPaymentProducer orderPaymentProducer;


    public void buy(final Order order) {
        order.setPaid(false);
        orderDao.save(order);
    }

    public List<Order> list() {
        return orderDao.list();
    }

    /**
     * Estudo de utilização de transactions
     *
     * O TransactionAttributeType.REQUIRED utiliza uma transação controlada pelo Bean e, quando há uma Exception,
     * a transação é revertida
     *
     * Checked Exceptions não geram Rollback, apenas unchecked.
     *
     * Para que checked exceptions gerem rollback, devemos adicionar algumas anotações:
     *
     * @ApplicationException(rollback = true)
     *
     * O mesmo vale para unchecked exceptions, para que não gerem rollback, devemos adicionar a anotação
     *
     * @ApplicationException(rollback = false)
     *
     * Verificar em {@link PaymentErrorException}
     *
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void sendToPayment(final int id) throws PaymentErrorException {
        final Order order = orderDao.getById(id);
        orderDao.sendToPayment(order);
        orderPaymentProducer.sendToPayment(order);
    }

    /**
     * Estudo de utilização de transactions
     *
     * O TransactionAttributeType.NOT_SUPPORTED é definido quando não se tem a intenção de utilizar uma transação
     *
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void sendToPaymentWithoutTransaction(final int id) throws PaymentErrorException {
        final Order order = orderDao.getById(id);
        orderDao.sendToPayment(order);
        orderPaymentProducer.sendToPayment(order);
    }
}
