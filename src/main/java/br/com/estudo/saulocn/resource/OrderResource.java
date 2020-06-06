package br.com.estudo.saulocn.resource;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import br.com.estudo.saulocn.jms.OrderPaymentProducer;
import br.com.estudo.saulocn.dao.BookDao;
import br.com.estudo.saulocn.dao.OrderDao;
import br.com.estudo.saulocn.model.Order;

@Path("order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    private BookDao bookDao;

    @Inject
    private OrderDao orderDao;

    @Inject
    OrderPaymentProducer orderPaymentProducer;

    @GET
    public List<Order> list() {
        return orderDao.list();
    }

    @POST
    public Response buy(final Order order, @Context final UriInfo uriInfo) {
        order.setPaid(false);
        orderDao.save(order);
        final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(order.getId()));
        return Response.created(builder.build()).build();
    }

    @POST
    @Path("send-to-payment/{id}")
    public Response sendToPayment(@PathParam("id") final int id) {
        final Order order = orderDao.getById(id);
        orderPaymentProducer.sendToPayment(order);
        return Response.ok().build();
    }

}
