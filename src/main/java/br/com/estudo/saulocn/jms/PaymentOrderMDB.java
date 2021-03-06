package br.com.estudo.saulocn.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import br.com.estudo.saulocn.dao.OrderDao;
import br.com.estudo.saulocn.model.Order;


@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "queue/PaymentQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "user", propertyValue = "user123"),
        @ActivationConfigProperty(propertyName = "password", propertyValue = "Password123"),
        @ActivationConfigProperty(propertyName = "connectionParameters", propertyValue = "host=queue-jee;port=5445"),
        @ActivationConfigProperty(propertyName = "connectorClassName", propertyValue = "org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory")
}, mappedName = "PaymentQueue")
public class PaymentOrderMDB implements MessageListener {

    @Inject
    private OrderDao orderDao;

    @Override public void onMessage(final Message message) {
        try {
            final String jsonOrder = message.getBody(String.class);
            final Jsonb jsonb = JsonbBuilder.create();
            final Order order = jsonb.fromJson(jsonOrder, Order.class);
            System.out.println("Pagando o pedido:"+ order.getId());
            orderDao.pay(order);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }


}
