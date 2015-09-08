package parseBook;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFTextStripper;


import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Created by strapper on 07.09.15.
 */
public class PDFBook extends Book  {

    public PDFBook(byte[] input, EnglishLexicon lexicon) {
        super(input, lexicon);
    }

    @Override
    public void parse(byte[] input) {
        String text = "";
        try {

//            System.out.println(input);

//            InputStream is = new ByteArrayInputStream(Charset.forName("UTF-8").encode(input).array());
            ByteArrayInputStream is = new ByteArrayInputStream(input);


            //     PdfReader reader = new PdfReader("/home/strapper/Desktop/mvnref-pdf.pdf");
            PdfReader reader = new PdfReader(is);
            System.out.println("This PDF has "+reader.getNumberOfPages()+" pages.");
            int n = reader.getNumberOfPages();

            for(int i = 1; i < n; i++) {
                String page = PdfTextExtractor.getTextFromPage(reader, i);
//                System.out.println(page);
                text += page;
            }

            System.out.println("Is this document tampered: "+reader.isTampered());
            System.out.println("Is this document encrypted: "+reader.isEncrypted());

        } catch (Exception e) {
            e.printStackTrace();
        }
        setText(text);
    }

}
