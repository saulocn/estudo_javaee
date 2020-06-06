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

import br.com.estudo.saulocn.exceptions.PaymentErrorException;
import br.com.estudo.saulocn.model.Order;
import br.com.estudo.saulocn.service.OrderService;

@Path("order")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    private OrderService orderService;

    @GET
    public List<Order> list() {
        return orderService.list();
    }

    @POST
    public Response buy(final Order order, @Context final UriInfo uriInfo) {
        orderService.buy(order);
        final UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(order.getId()));
        return Response.created(builder.build()).build();
    }

    @POST
    @Path("pay/{id}")
    public Response sendToPayment(@PathParam("id") final int id) throws PaymentErrorException {
        orderService.sendToPayment(id);
        return Response.ok().build();
    }

}
