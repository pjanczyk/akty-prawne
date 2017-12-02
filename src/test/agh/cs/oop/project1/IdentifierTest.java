package agh.cs.oop.project1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdentifierTest {

    @Test
    void tryParse_invalid() {
        assertThrows(InvalidFormatException.class, () -> Identifier.parse("5a9b"));
    }

    @Test
    void compareTo() {
        assertTrue(Identifier.parse("VIb").compareTo(Identifier.parse("9a")) < 0);
        assertTrue(Identifier.parse("c").compareTo(Identifier.parse("a")) > 0);
    }

    @Test
    void isInRange() {
        assertTrue(Identifier.parse("5").isInRange(Identifier.parse("5"), Identifier.parse("6")));
    }

    @Test
    void toString_test() {
        assertEquals("XIa", Identifier.parse("XIa").toString());
    }

}