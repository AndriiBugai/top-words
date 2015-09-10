package com.example.jersey;

import com.sun.org.apache.xpath.internal.SourceTree;
import parseBook.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.HashSet;


@Path("/")
public class HelloWorld {

    static String text;
    static byte[] input;
    static String level;
    static String format;
    static String language;
    static String bookLanguage;


    @GET
//    @Produces(MediaType.TEXT_PLAIN )
    @Path("/hello")
    @Produces("text/html")
    public String getMessage() {
        System.out.println("main - hello");

        HashSet<String> easyWords;
        HashSet<String> canonicalEnglish;
        HashMap<String, String> dict;

        EnglishLexicon lexicon = new EnglishLexicon();

        Book book;

//        switch (format) {
//            case "fb2": book =  new FB2Book(text, lexicon);
//                        break;
//            case "pdf": book = new PDFBook(text, lexicon);
//                        break;
//            case "txt": book = new TXTBook(text, lexicon);
//                        break;
//            default:    book = new TXTBook(text, lexicon);
//        }


        if(format.equals("fb2")) {
            book = new FB2Book(input, lexicon);
        } else if(format.equals("pdf"))  {
            book = new PDFBook(input, lexicon);
        } else {
            book = new TXTBook(input, lexicon);
        }
        String json = book.getJsonTranslation(language);
        bookLanguage = book.getTextLanguage();

        return json;

    }

    @GET
    @Path("/bookLanguage")
    @Produces("text/html")
    public String getLanguage() {
        System.out.println("getLanguage");
        return bookLanguage;

    }


    @POST
    @Path("/format/")
//    @Consumes(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response format(String format) {
        this.format = format;
        System.out.println("format");
        return Response.status(201).entity("hello").build();
    }

    @POST
    @Path("/send/")
//    @Consumes(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response send(byte[] input) {
        this.input = input;
        System.out.println("send - input text");
        return Response.status(201).entity("hello").build();
    }


    @POST
    @Path("/lang/")
//    @Consumes(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response language(String text) {
    //    System.out.println(text);
        language = text;
        System.out.println("set language");
        return Response.status(201).entity("hello").build();
    }

//    @Path("/{param}")
//    public Response getMessage(@PathParam("param") String message) {
//        String output = "Jersey says " + message;
//        return Response.status(200).entity(output).build();
//    }
}