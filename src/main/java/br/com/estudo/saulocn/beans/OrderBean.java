package br.com.estudo.saulocn.beans;

import java.io.Serializable;
import java.util.List;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.estudo.saulocn.dao.BookDao;
import br.com.estudo.saulocn.dao.OrderDao;
import br.com.estudo.saulocn.model.Book;
import br.com.estudo.saulocn.model.Order;

@Named
@ViewScoped
public class OrderBean implements Serializable {

    private Order order = new Order();

    private Integer bookId;

    @Inject
    private BookDao bookDao;

    @Inject
    private OrderDao orderDao;

    public List<Book> getBooks() {
        return bookDao.list();
    }

    public List<Book> getCurrentBooks() {
        return this.order.getBooks();
    }

    public void saveBook() {
        order.getBooks().add(bookDao.getById(this.bookId));
    }

    public void save() {
        orderDao.save(this.order);
        order = new Order();
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(final Integer bookId) {
        this.bookId = bookId;
    }

}
