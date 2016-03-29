package org.javaee7.jaxrs.angularjs;

import com.example.domain.Note;
import com.example.rest.NoteApp;
import com.example.rest.NoteResource;
import com.example.rest.NoteResourceImpl;
import com.google.common.base.Predicate;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.importer.ExplodedImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.jboss.arquillian.graphene.Graphene.waitGui;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class NoteResourceImplTest {
    @Deployment(testable = false)
    public static Archive createDeployment() {
        final GenericArchive webResources = ShrinkWrap.create(GenericArchive.class)
                .as(ExplodedImporter.class)
                .importDirectory("src/main/webapp")
                .as(GenericArchive.class);
        return ShrinkWrap.create(WebArchive.class, NoteResourceImplTest.class.getSimpleName() + ".war")
                .addClasses(Note.class, NoteApp.class, NoteResource.class, NoteResourceImpl.class)
                .addClass(Data.class) // test data
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource("enforce-beans.xml", "jboss-all.xml") // TODO: really needed?
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .merge(webResources);
    }

    @Drone // activate graphene
    private WebDriver driver;

    @Test
    @InSequence(1)
    public void onEnterContainsNotesFromDB(@InitialPage TodoPage page) {
        //        Given
        //        When
        //        Then
        assertEquals(3, page.getTodos().size());
        assertEquals("Note A", page.getTodos().get(0).getTitle());
        assertEquals("Note B", page.getTodos().get(1).getTitle());
        assertEquals("Note C", page.getTodos().get(2).getTitle());
    }

    @Test
    @InSequence(2)
    public void addNewNote(@InitialPage final TodoPage page) throws Exception {
        //        Given
        assertEquals(3, page.getTodos().size());
        //        When
        page.addNote();
        waitGui().withTimeout(1, TimeUnit.MINUTES).until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(final WebDriver webDriver) {
                return page.modalIsShown();
            }
        });
        page.typeTitle("New title");
        page.typeSummary("New summary");
        page.save();
        waitAjax().withTimeout(1, TimeUnit.MINUTES).until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(final WebDriver webDriver) {
                return page.getTodos().size() == 4;
            }
        });
        //        Then
        assertEquals(4, page.getTodos().size());
        assertEquals("New title", page.getTodos().get(3).getTitle());
        assertEquals("New summary", page.getTodos().get(3).getSummary());
    }

    @Test
    @InSequence(3)
    public void removeNote(@InitialPage final TodoPage page) throws Exception {
        //        Given
        assertEquals(4, page.getTodos().size());
        //        When
        page.getTodos().get(0).remove();
        waitAjax().withTimeout(1, TimeUnit.MINUTES).until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(final WebDriver webDriver) {
                return page.getTodos().size() == 3;
            }
        });
        //        Then
        assertEquals(3, page.getTodos().size());
        assertEquals("Note B", page.getTodos().get(0).getTitle());
        assertEquals("Note C", page.getTodos().get(1).getTitle());
        assertEquals("New title", page.getTodos().get(2).getTitle());
    }
}
