package br.com.estudo.saulocn;

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
}
