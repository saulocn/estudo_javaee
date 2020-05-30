package br.com.estudo.saulocn.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="sq_book")
    @SequenceGenerator(name="sq_book", sequenceName="sq_book")
    private int id;
    private String nome;

    public Book() {
    }

    public Book(final int id, final String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(final String nome) {
        this.nome = nome;
    }

    @Override public String toString() {
        final StringBuilder builder = new StringBuilder()//
                .append("Book [")//
                .append("id=")//
                .append(id)//
                .append(",nome=\"")//
                .append(nome + "\"")//
                .append("]");
        return builder.toString();
    }
}
