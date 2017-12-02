package agh.cs.oop.project1;

import org.junit.jupiter.api.Test;

import java.util.OptionalInt;

import static org.junit.jupiter.api.Assertions.*;

class NumberParserTest {

    @Test
    void tryParse() {
        assertEquals(OptionalInt.of(2), NumberParser.tryParse("II"));
        assertEquals(OptionalInt.of(1000), NumberParser.tryParse("M"));
        assertEquals(OptionalInt.of(3489), NumberParser.tryParse("MMMCDLXXXIX"));

        assertEquals(OptionalInt.of(123456789), NumberParser.tryParse("123456789"));

        assertEquals(OptionalInt.empty(), NumberParser.tryParse(""));
        assertEquals(OptionalInt.empty(), NumberParser.tryParse("-5"));
    }

}