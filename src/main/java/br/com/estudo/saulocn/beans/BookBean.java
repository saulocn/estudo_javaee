package br.com.estudo.saulocn.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.estudo.saulocn.dao.BookDao;
import br.com.estudo.saulocn.model.Book;

@Named
@RequestScoped
public class BookBean {

    @Inject
    private BookDao bookDao;

    private Book book = new Book();

    public void save() {
        bookDao.save(book);
        book = new Book();
    }

    public Book getBook() {
        return book;
    }

    public void setBook(final Book book) {
        this.book = book;
    }
}
