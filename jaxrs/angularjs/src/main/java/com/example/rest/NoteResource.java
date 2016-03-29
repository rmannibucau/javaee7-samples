package com.example.rest;

import com.example.domain.Note;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/note")
@Produces(APPLICATION_JSON)
public interface NoteResource {

    @GET
    @Path("/")
    List<Note> getNotes();

    @DELETE
    @Path("/{id}")
    void removeNote(@PathParam("id") Long noteId);

    @POST
    @Path("/")
    @Consumes(APPLICATION_JSON)
    Note saveNote(Note note);
}
