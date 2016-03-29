package org.javaee7.jaxrs.angularjs;

import org.jboss.arquillian.graphene.angular.findby.FindByNg;
import org.jboss.arquillian.graphene.page.Location;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@Location("")
public class TodoPage {

    @FindByNg(action = "addNote()")
    private WebElement addNote;

    @FindByNg(action = "save()")
    private WebElement save;

    @FindByNg(model = "selectedNote.summary")
    private WebElement summaryInput;

    @FindByNg(model = "selectedNote.title")
    private WebElement tileInput;

    @FindByNg(repeat= "note in notes")
    private List<NoteItem> todos;

    public List<NoteItem> getTodos()
    {
        return todos;
    }

    public void addNote()
    {
        addNote.click();
    }

    public void save()
    {
        save.submit();
    }

    public void typeSummary(String text)
    {
        summaryInput.clear();
        summaryInput.sendKeys(text);
    }

    public void typeTitle(String text)
    {
        tileInput.clear();
        tileInput.sendKeys(text);
    }

    public boolean modalIsShown() {
        return tileInput.isDisplayed() && summaryInput.isDisplayed();
    }

    public static class NoteItem {
        @FindBy(className = "btn-danger")
        private WebElement removeButton;

        @FindBy(className = "summary")
        private WebElement summary;

        @FindBy(className = "title")
        private WebElement title;

        public String getSummary()
        {
            return summary.getText();
        }

        public String getTitle()
        {
            return title.getText();
        }

        public void remove()
        {
            removeButton.click();
        }
    }
}
