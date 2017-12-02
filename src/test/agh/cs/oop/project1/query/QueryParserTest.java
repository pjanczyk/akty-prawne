package agh.cs.oop.project1.query;

import agh.cs.oop.project1.Identifier;
import agh.cs.oop.project1.InvalidFormatException;
import agh.cs.oop.project1.Node;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class QueryParserTest {

    @ParameterizedTest
    @MethodSource("queriesProvider")
    void parseQuery(String queryText, Query expectedResult) {
        assertEquals(expectedResult, new QueryParser().parseQuery(queryText));
    }

    static Stream<Arguments> queriesProvider() {
        return Stream.of(
                Arguments.of("art. 2b-5", new Query(List.of(
                        new Query.Segment(Node.Type.Article, Identifier.parse("2b"), Identifier.parse("5"))
                ))),
                Arguments.of("dzial IXa", new Query(List.of(
                        new Query.Segment(Node.Type.Part, Identifier.parse("9a"))
                ))),
                Arguments.of("rozdzial 1", new Query(List.of(
                        new Query.Segment(Node.Type.Chapter, Identifier.parse("1"))
                ))),
                Arguments.of("art. 1 ust. 2 pkt 3 lit. a-c", new Query(List.of(
                        new Query.Segment(Node.Type.Article, Identifier.parse("1")),
                        new Query.Segment(Node.Type.Paragraph, Identifier.parse("2")),
                        new Query.Segment(Node.Type.Point, Identifier.parse("3")),
                        new Query.Segment(Node.Type.Letter, Identifier.parse("a"), Identifier.parse("c"))
                )))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "fragment 1",
            "art. 1-2 ust. 3",
            "ust. 1 art. 2",
    })
    void parseQuery_invalid(String queryText) {
        assertThrows(InvalidFormatException.class, () -> new QueryParser().parseQuery(queryText));
    }

}