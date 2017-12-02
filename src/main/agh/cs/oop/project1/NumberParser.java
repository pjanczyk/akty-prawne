package agh.cs.oop.project1;

import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberParser {

    private static final List<String> digitsOnes =
            List.of("", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX");

    private static final List<String> digitsTens =
            List.of("", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC");

    private static final List<String> digitsHundreds =
            List.of("", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM");

    private static final List<String> digitsThousands =
            List.of("", "M", "MM", "MMM");

    public static OptionalInt tryParse(String number) {
        OptionalInt a = tryParseArabic(number);
        if (a.isPresent()) return a;
        else return tryParseRoman(number);
    }

    private static OptionalInt tryParseArabic(String arabicNumeral) {
        // number is limited to 9 digits
        Pattern pattern = Pattern.compile("^\\d{1,9}$");
        Matcher matcher = pattern.matcher(arabicNumeral);

        if (!matcher.matches()) return OptionalInt.empty();

        int result = Integer.parseUnsignedInt(arabicNumeral, 10);
        return OptionalInt.of(result);
    }

    private static OptionalInt tryParseRoman(String romanNumeral) {
        if (romanNumeral.isEmpty()) return OptionalInt.empty();

        Pattern pattern = Pattern.compile(
                "^(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$",
                Pattern.CASE_INSENSITIVE);

        Matcher matcher = pattern.matcher(romanNumeral);

        if (!matcher.matches()) return OptionalInt.empty();

        String thousandsRoman = matcher.group(1).toUpperCase();
        String hundredsRoman = matcher.group(2).toUpperCase();
        String tensRoman = matcher.group(3).toUpperCase();
        String onesRoman = matcher.group(4).toUpperCase();

        int thousands = digitsThousands.indexOf(thousandsRoman);
        int hundreds = digitsHundreds.indexOf(hundredsRoman);
        int tens = digitsTens.indexOf(tensRoman);
        int ones = digitsOnes.indexOf(onesRoman);

        int result = 1000 * thousands + 100 * hundreds + 10 * tens + ones;
        return OptionalInt.of(result);
    }
}
