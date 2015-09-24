package com.example.jersey;

import org.json.JSONException;
import org.json.JSONObject;
import parseBook.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.UnsupportedEncodingException;
import static javax.xml.bind.DatatypeConverter.parseBase64Binary;

@Path("/")
public class HelloWorld {

    @POST
    @Path("/test/")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String test(String input) throws JSONException, UnsupportedEncodingException {
        JSONObject jsonObject = new JSONObject(input);
        String format = jsonObject.getString("format");
        String language = jsonObject.getString("language");
        byte[] text = parseBase64Binary(jsonObject.getString("contents"));

        EnglishLexicon lexicon = new EnglishLexicon();

        Book book;
        if(format.equals("fb2")) {
            book = new FB2Book(text, lexicon);
        } else if(format.equals("pdf"))  {
                   book = new PDFBook(text, lexicon);
        } else {
                   book = new TXTBook(text, lexicon);
        }


        String json = book.getJsonTranslation(language);

        System.out.println(json);
        return json;
    }
}