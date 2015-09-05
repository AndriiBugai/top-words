package com.example.jersey;

import com.sun.org.apache.xpath.internal.SourceTree;
import parseBook.Book;
import parseBook.EnglishLexicon;
import parseBook.FB2Book;
import parseBook.TXTBook;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.HashSet;


@Path("/")
public class HelloWorld {

    static String text;
    static String level;
    static String format;


    @GET
//    @Produces(MediaType.TEXT_PLAIN )
    @Path("/hello")
    @Produces("text/html")
    public String getMessage() {

        HashSet<String> easyWords;
        HashSet<String> canonicalEnglish;
        HashMap<String, String> dict;

        EnglishLexicon lexicon = new EnglishLexicon(level);

        Book book;
        if(format.equals("fb2")) {
            book = new FB2Book(text, lexicon);
        } else {
            book = new TXTBook(text, lexicon);
        }
        String json = book.getJsonTranslation();
        return json;
    }

    @POST
    @Path("/level/")
//    @Consumes(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response level(String level) {
        this.level = level;
        return Response.status(201).entity("hello").build();
    }

    @POST
    @Path("/format/")
//    @Consumes(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response format(String format) {
        this.format = format;
        return Response.status(201).entity("hello").build();
    }

    @POST
    @Path("/send/")
//    @Consumes(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response send(String text) {
        this.text = text;
        return Response.status(201).entity("hello").build();
    }


    @POST
    @Path("/sendtest/")
//    @Consumes(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendtest(String text) {
    //    System.out.println(text);
        return Response.status(201).entity("hello").build();
    }

//    @Path("/{param}")
//    public Response getMessage(@PathParam("param") String message) {
//        String output = "Jersey says " + message;
//        return Response.status(200).entity(output).build();
//    }
}