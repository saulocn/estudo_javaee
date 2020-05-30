package br.com.estudo.saulocn.resource;

import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import br.com.estudo.saulocn.dao.BookDao;
import br.com.estudo.saulocn.model.Book;

@Path("books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BooksResource {

    @Inject
    private BookDao bookDao;

    @GET
    public List<Book> list() {
        final List<Book> list = bookDao.list();
        return list;
    }

    @POST
    public Response salvar(Book book, @Context UriInfo uriInfo) {
        bookDao.save(book);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(book.getId()));
        return Response.created(builder.build()).build();
    }

    @GET
    @Path("/{id}")
    public Book get(@PathParam("id") int id) {
        return bookDao.getById(id);
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") int id, Book book,  @Context UriInfo uriInfo) {
        bookDao.update(id, book);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Integer.toString(book.getId()));
        return Response.status(204).build();
    }

    @DELETE
    @Path("/{id}")
    public Response update(@PathParam("id") int id, @Context UriInfo uriInfo) {
        bookDao.delete(id);
        return Response.status(204).build();
    }


}
