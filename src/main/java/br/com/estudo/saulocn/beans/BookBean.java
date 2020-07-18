package br.com.estudo.saulocn.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import br.com.estudo.saulocn.model.Book;

@Named
@RequestScoped
public class BookBean {

    private Book book = new Book();

    public void save() {
        System.out.println("Salvando... " + book.getNome());
    }

    public Book getBook() {
        return book;
    }

    public void setBook(final Book book) {
        this.book = book;
    }
}
