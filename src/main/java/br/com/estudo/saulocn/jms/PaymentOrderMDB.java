package br.com.estudo.saulocn.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import br.com.estudo.saulocn.dao.OrderDao;
import br.com.estudo.saulocn.model.Order;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup",
                propertyValue = Order.JMS_ORDER_PAYMENT_QUEUE),
        @ActivationConfigProperty(propertyName = "destinationType",
                propertyValue = "javax.jms.Queue")
})
public class PaymentOrderMDB implements MessageListener {

    @Inject
    private OrderDao orderDao;

    @Override public void onMessage(final Message message) {
        try {
            final Order order = message.getBody(Order.class);
            System.out.println("Pagando o pedido:"+ order.getId());
            orderDao.pay(order);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


}
