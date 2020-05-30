package br.com.estudo.saulocn.dao;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.com.estudo.saulocn.model.Book;

@Stateless
public class BookDao {

    @PersistenceContext(unitName = "books")
    private EntityManager entityManager;

    public List<Book> list(){
        return entityManager.createQuery("select b from Book b", Book.class).getResultList();
    }

    public void save(Book book){
        entityManager.persist(book);
    }

    public Book getById(int id) {
        return entityManager.find(Book.class, id);
    }

    public void update(int id, Book newBook) {
        final Book oldBook = this.getById(id);
        oldBook.setNome(newBook.getNome());
        entityManager.merge(oldBook);
    }
}
