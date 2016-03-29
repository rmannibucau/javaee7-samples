package org.javaee7.jaxrs.angularjs;

import com.example.domain.Note;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

// arquillian-persistence + graphene = headache so using a simpler solution
//
// simply persist the initial data programmatically
//
// simple alternative could be to expose a rest "test" endpoint to manipulate data or use warp extension
@Singleton
@Startup
public class Data {
    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    private void seed() {
        entityManager.persist(newNote("Note A", "Simple summary"));
        entityManager.persist(newNote("Note B", "Extended summary"));
        entityManager.persist(newNote("Note C", "Advanced summary"));
    }

    private Note newNote(final String title, final String summary) {
        final Note note = new Note();
        note.setTitle(title);
        note.setSummary(summary);
        return note;
    }
}
