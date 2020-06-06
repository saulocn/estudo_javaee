package br.com.estudo.saulocn.jms;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;

import br.com.estudo.saulocn.dao.OrderDao;
import br.com.estudo.saulocn.model.Order;

@Singleton
public class OrderPaymentProducer {
    @Inject
    @JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
    private JMSContext context;

    @Resource(mappedName = Order.JMS_ORDER_PAYMENT_QUEUE)
    private Queue queue;

    @Inject
    private OrderDao orderDao;

    public void sendToPayment(final Order order){
        final JMSProducer producer = context.createProducer();
        orderDao.sendToPayment(order);
        System.out.println("Enviando pedido para o pagamento:"+order.getId());
        producer.send(queue, order);
    }

}
