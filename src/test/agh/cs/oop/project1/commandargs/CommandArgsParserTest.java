package agh.cs.oop.project1.commandargs;

import agh.cs.oop.project1.InvalidFormatException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CommandArgsParserTest {

    @ParameterizedTest
    @MethodSource("argumentsProvider")
    void parseArguments(List<String> args, CommandArgs expectedResult) {
        assertEquals(expectedResult, new CommandArgsParser().parseArguments(args));
    }

    static Stream<Arguments> argumentsProvider() {
        return Stream.of(
                Arguments.of(List.of("kontytucja.txt", "-l"),
                        new CommandArgs("kontytucja.txt", true, null)),
                Arguments.of(List.of("uokik.txt"),
                        new CommandArgs("uokik.txt", false, null)),
                Arguments.of(List.of("uokik.txt", "-l", "dzial 1"),
                        new CommandArgs("uokik.txt", true, "dzial 1"))
        );
    }

    @Test
    void parseArguments_invalidNumberOfArguments() {
        List<String> args = List.of("a", "b", "c");
        assertThrows(InvalidFormatException.class,
                () -> new CommandArgsParser().parseArguments(args));
    }
}