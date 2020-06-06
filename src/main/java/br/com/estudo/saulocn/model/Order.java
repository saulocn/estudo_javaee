package br.com.estudo.saulocn.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "book_order")
public class Order implements Serializable {

    public static final String JMS_ORDER_PAYMENT_QUEUE = "java:/jms/queue/PaymentQueue";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sq_books_order")
    @SequenceGenerator(name = "sq_books_order", sequenceName = "sq_books_order")
    private int id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private boolean paid;

    private boolean sentToPayment;

    public Order(final Book book, final boolean paid) {
        this.book = book;
        this.paid = paid;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(final Book book) {
        this.book = book;
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

    @Override public String toString() {
        final StringBuilder builder = new StringBuilder()//
                .append("Order [")//
                .append("id=")//
                .append(id)//
                .append(",book=")//
                .append(book)//
                .append(",paid=")//
                .append(paid)//
                .append("]");
        return builder.toString();
    }
}
