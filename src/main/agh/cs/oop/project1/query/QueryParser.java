package agh.cs.oop.project1.query;

import agh.cs.oop.project1.Identifier;
import agh.cs.oop.project1.InvalidFormatException;
import agh.cs.oop.project1.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public final class QueryParser {
    /*
     * Grammar:
     *   type             ::= "dział" | "dzial" | "rozdział" | "rozdzial" | "art" | "ust" | "pkt" | "lit"
     *   identifier       ::= ( roman_number | decimal_number ) letters?
     *   identifier_range ::= identifier "-" identifier
     *   segment          ::= type ( identifier | identifier_range )
     *   query            ::= segment+
     */

    private static final Map<String, Node.Type> types = Map.of(
            "dział", Node.Type.Part,
            "dzial", Node.Type.Part,
            "rozdział", Node.Type.Chapter,
            "rozdzial", Node.Type.Chapter,
            "art", Node.Type.Article,
            "ust", Node.Type.Paragraph,
            "pkt", Node.Type.Point,
            "lit", Node.Type.Letter
    );

    public Query parseQuery(String text) {
        text = text.toLowerCase()
                .replaceAll("\\s*-\\s*", "-")  // remove spaces before/after dashes
                .replaceAll("[.,]", "");       // remove all dots and commas

        String[] elements = text.split("\\s+"); // split by whitespaces

        if (elements.length % 2 != 0) throw new InvalidFormatException();

        List<Query.Segment> segments = new ArrayList<>();

        for (int i = 0; i < elements.length; i += 2) {
            String typeQuery = elements[i];
            String identifierQuery = elements[i + 1];

            Node.Type type = types.get(typeQuery.toLowerCase());
            if (type == null) throw new InvalidFormatException();

            if (!segments.isEmpty() && type.level <= segments.get(segments.size() - 1).type.level)
                throw new InvalidFormatException();

            Identifier[] identifiers = parseIdentifierRange(identifierQuery);
            if (identifiers.length == 2) {
                segments.add(new Query.Segment(type, identifiers[0], identifiers[1]));
            } else {
                segments.add(new Query.Segment(type, identifiers[0]));
            }
        }

        return new Query(segments);
    }

    private Identifier parseIdentifier(String text) {
        return Identifier.tryParse(text).orElseThrow(InvalidFormatException::new);
    }

    private Identifier[] parseIdentifierRange(String text) {
        return Stream.of(text.split("-", 2))
                .map(this::parseIdentifier)
                .toArray(Identifier[]::new);
    }

}
