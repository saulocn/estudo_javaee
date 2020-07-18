package br.com.estudo.saulocn.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "book_order")
public class Order implements Serializable {

    public static final String JMS_ORDER_PAYMENT_QUEUE = "java:jboss/exported/jms/queue/PaymentQueue";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_books_order")
    @SequenceGenerator(name = "sq_books_order", sequenceName = "sq_books_order")
    private int id;

    @OneToMany
    private List<Book> books = new ArrayList<>();

    private boolean paid;

    private boolean sentToPayment;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(final List<Book> books) {
        this.books = books;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(final boolean paid) {
        this.paid = paid;
    }

    public boolean isSentToPayment() {
        return sentToPayment;
    }

    public void setSentToPayment(final boolean sentToPayment) {
        this.sentToPayment = sentToPayment;
    }
}
