package br.com.estudo.saulocn.jms;

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.json.JsonObjectBuilder;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import br.com.estudo.saulocn.exceptions.PaymentErrorException;
import br.com.estudo.saulocn.model.Order;

@Singleton
public class OrderPaymentProducer {

    @Inject
    @JMSConnectionFactory("java:/jms/PaymentQueueCF")
    private JMSContext context;

    @Resource(mappedName = Order.JMS_ORDER_PAYMENT_QUEUE)
    private Queue queue;

    public void sendToPayment(final Order order) throws PaymentErrorException {
        /**
         * Exceção lançada para testes de transações no {@link br.com.estudo.saulocn.service.OrderService}
         */
        // throw new PaymentErrorException();

        final Jsonb jsonb = JsonbBuilder.create();
        final String jsonOrder = jsonb.toJson(order);

        final JMSProducer producer = context.createProducer();
        System.out.println("Enviando pedido para o pagamento:" + order.getId());
        producer.send(queue, jsonOrder);
    }

}
