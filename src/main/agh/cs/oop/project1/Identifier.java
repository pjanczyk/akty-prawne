package agh.cs.oop.project1;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an identifier that consists of digits and letters, e.g. '3', 'IXa', '7az', 'c'
 */
public final class Identifier implements Comparable<Identifier> {
    private final String rawText;
    private final int number;
    private final String letters;

    public static Optional<Identifier> tryParse(String text) {
        if (text.isEmpty()) return Optional.empty();

        Pattern pattern = Pattern.compile("^([IVXLCDM]*|\\d*)([a-z]*)$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (!matcher.matches()) return Optional.empty();

        String numberText = matcher.group(1);

        int number = 0;
        if (!numberText.isEmpty()) {
            OptionalInt numberOptional = NumberParser.tryParse(numberText);
            if (!numberOptional.isPresent()) return Optional.empty();
            number = numberOptional.getAsInt();
        }

        String letters = matcher.group(2);

        return Optional.of(new Identifier(text, number, letters));
    }

    public static Identifier parse(String text) {
        return tryParse(text).orElseThrow(InvalidFormatException::new);
    }

    public Identifier(String rawText, int number, String letters) {
        this.rawText = rawText;
        this.number = number;
        this.letters = letters;
    }

    @Override
    public int compareTo(@NotNull Identifier other) {
        if (this.number != other.number) {
            return Integer.compare(this.number, other.number);
        } else {
            return this.letters.compareToIgnoreCase(other.letters);
        }
    }

    public boolean isInRange(Identifier start, Identifier end) {
        return this.compareTo(start) >= 0 && this.compareTo(end) <= 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return number == that.number &&
                Objects.equals(letters, that.letters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, letters);
    }

    @Override
    public String toString() {
        return rawText;
    }
}
