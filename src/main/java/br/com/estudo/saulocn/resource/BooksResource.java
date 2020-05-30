package br.com.estudo.saulocn.resource;

import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.estudo.saulocn.BookDao;
import br.com.estudo.saulocn.model.Book;

@Path("books")
@Produces(MediaType.APPLICATION_JSON)
public class BooksResource {
    @Inject
    private BookDao bookDao;

    @GET
    public List<Book> list(){
        Book b1 = new Book(1, "Teste 1");
        Book b2 = new Book(2, "Teste 2");
        Book b3 = new Book(3, "Teste 3");
        Book b4 = new Book(4, "Teste 4");
        Book b5 = new Book(5, "Teste 5");
        Book b6 = new Book(6, "Teste 6");
        List<Book> lista = Arrays.asList(b1, b2, b3, b4, b5, b6);
        final List<Book> list = bookDao.list();
        return list;
    }
}
