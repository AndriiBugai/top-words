package com.example.jersey;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.json.JSONException;
import org.json.JSONObject;
import parseBook.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;


@Path("/")
public class HelloWorld {

    static String text;
    static byte[] input;
    static String level;
    static String format;
    static String language;
    static String bookLanguage;


    @POST
    @Path("/test/")
//    @Consumes(MediaType.APPLICATION_XML)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String test(String input) throws JSONException, UnsupportedEncodingException {
//        this.input = input;
//        System.out.println("send - input text");
        System.out.println(input);

        JSONObject jsonObject = new JSONObject(input);
//        System.out.println(jsonObject.toString());

        System.out.println(jsonObject.getString("format"));
        System.out.println(jsonObject.getString("language"));
        String format = jsonObject.getString("format");
        String language = jsonObject.getString("language");

        System.out.println(format);


        byte[] text = parseBase64Binary(jsonObject.getString("contents"));
//        String decoded = new String(text, "UTF-16");
//        System.out.println(decoded);
 //       String decoded = jsonObject.getString("contents");

 //     System.out.println(decoded);


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


    //    book = new FB2Book(decoded, lexicon);
        if(format.equals("fb2")) {
            book = new FB2Book(text, lexicon);
        } else if(format.equals("pdf"))  {
                   book = new PDFBook(text, lexicon);
        } else {
                   book = new TXTBook(text, lexicon);
        }

        String json = book.getJsonTranslation(language);
        bookLanguage = book.getTextLanguage();

        System.out.println(json);


       return json;
    }


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


//        book = new FB2Book(input, lexicon);
//        if(format.equals("fb2")) {
//            book = new FB2Book(input, lexicon);
//        } else if(format.equals("pdf"))  {
//     //       book = new PDFBook(input, lexicon);
//        } else {
//     //       book = new TXTBook(input, lexicon);
//        }
//        String json = book.getJsonTranslation(language);
//        bookLanguage = book.getTextLanguage();

        String json = " sasfd";
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
    public String send(byte[] input) {
        this.input = input;
        System.out.println("send - input text");
        return  "moto g" ; //Response.status(201).entity("bliatj").build();
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