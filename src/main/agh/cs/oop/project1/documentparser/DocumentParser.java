package agh.cs.oop.project1.documentparser;

import agh.cs.oop.project1.InvalidFormatException;
import agh.cs.oop.project1.Node;

import java.util.List;
import java.util.regex.Pattern;

public final class DocumentParser {

    /**
     * @throws InvalidFormatException if a document is in an invalid/unsupported format
     */
    public Node parse(String text) {
        // Remove garbage lines
        text = text.replaceAll("©Kancelaria Sejmu(.*)\n\\d{4}-\\d{2}-\\d{2}\n", "")
                .replace("O\ns\nr\n2\n3\np\nN\n", "");

        // Detect a type of the document
        String constitutionStart = "KONSTYTUCJA\nRZECZYPOSPOLITEJ POLSKIEJ\nz dnia 2 kwietnia 1997 r.\n";
        String uokikStart = "Dz.U. 2007 Nr 50 poz. 331\nUSTAWA\nz dnia 16 lutego 2007 r.\no ochronie konkurencji i konsumentów\n";

        String documentName;
        if (text.startsWith(constitutionStart)) {
            documentName = "KONSTYTUCJA\nRZECZYPOSPOLITEJ POLSKIEJ\nz dnia 2 kwietnia 1997 r.";
            text = text.substring(constitutionStart.length());
        } else if (text.startsWith(uokikStart)) {
            documentName = "USTAWA\nz dnia 16 lutego 2007 r.\no ochronie konkurencji i konsumentów";
            text = text.substring(uokikStart.length());
        } else {
            throw new InvalidFormatException();
        }

        /*
         * Examples:
         *   Document (Dokument) - KONSTYTUCJA
         *   Part (Dział)        - DZIAŁ IIIA
         *   Chapter (Rozdział)  - Rozdział IIIA
         *   Title (Tytuł)       - NAJWYŻSZA IZBA KONTROLI
         *   Article (Artykuł)   - Art. 3a
         *   Paragraph (Ustęp)   - 3a.
         *   Point (Punkt)       - 3a)
         *   Letter (Litera)     - a)
         */

        Splitter splitter = new SplitterChainBuilder()
                .splitBy(Node.Type.Part, Pattern.compile("(?m)^DZIAŁ (?<identifier>[IVXLCDM]+[A-Z]*)\n(?<text>.*)\n"))
                .splitBy(Node.Type.Chapter, Pattern.compile("(?m)^Rozdział (?<identifier>[IVXLCDM\\d]+[a-z]*)\n(?<text>.*(?:\n\\p{Ll}.*)?)\n"))
                .splitBy(Node.Type.Title, Pattern.compile("(?m)^(?<text>[\\p{Lu}| ]+)\n"))
                .splitBy(Node.Type.Article, Pattern.compile("(?m)^Art\\. (?<identifier>\\d+[a-z]*)\\.[\n| ]"))
                .splitBy(Node.Type.Paragraph, Pattern.compile("(?m)^(?<identifier>\\d+[a-z]*)\\. "))
                .splitBy(Node.Type.Point, Pattern.compile("(?m)^(?<identifier>\\d+[a-z]*)\\) "))
                .splitBy(Node.Type.Letter, Pattern.compile("(?m)^(?<identifier>[a-z]+)\\) "))
                .endWith(Node.Type.PlainText);

        List<Node> nodes = splitter.split(text);

        return new Node.Builder()
                .setType(Node.Type.Document)
                .setText(documentName)
                .setChildren(nodes)
                .build();
    }

}
