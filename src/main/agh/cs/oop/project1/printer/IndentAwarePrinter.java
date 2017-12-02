package agh.cs.oop.project1.printer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A text printer that support indents and text reflow
 */
abstract class IndentAwarePrinter {
    private final int outputWidth;
    private final List<Line> lines = new ArrayList<>();
    private Line currentLine = null;
    private int indent = 0;

    public IndentAwarePrinter(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Line line : lines) {
            sb.append(makeIndentString(line.indent));
            sb.append(line.text);
            sb.append('\n');
        }
        return sb.toString();
    }

    protected void write(String text, Object... args) {
        write(String.format(text, args));
    }

    protected void write(String text) {
        while (!text.isEmpty()) {
            if (currentLine == null) {
                currentLine = new Line(this.indent);
            }

            int charsLeft = outputWidth - 3 * currentLine.indent - currentLine.text.length();

            int newLinePos = text.indexOf('\n');

            if (newLinePos != -1 && newLinePos <= charsLeft) {
                currentLine.text += text.substring(0, newLinePos);
                lines.add(currentLine);
                currentLine = null;
                text = text.substring(newLinePos + 1);
                continue;
            }

            if (text.length() <= charsLeft) {
                currentLine.text += text;
                break;
            }

            int spacePos = stringLastIndexOf(text, ' ', 0, charsLeft + 1);

            if (spacePos == -1) {
                if (currentLine.text.length() > 0) {
                    lines.add(currentLine);
                    currentLine = null;
                    continue;
                } else {
                    currentLine.text += text.substring(0, charsLeft);
                    lines.add(currentLine);
                    currentLine = null;
                    text = text.substring(charsLeft);
                    continue;
                }
            }

            currentLine.text += text.substring(0, spacePos);
            lines.add(currentLine);
            currentLine = null;
            text = text.substring(spacePos + 1);
        }
    }

    private static int stringLastIndexOf(String str, char ch, int start, int end) {
        for (int i = end - 1; i >= start; i--) {
            if (str.charAt(i) == ch)
                return i;
        }
        return -1;
    }

    protected void indented(Runnable runnable) {
        indent++;
        runnable.run();
        indent--;
    }

    private String makeIndentString(int indent) {
        return String.join("", Collections.nCopies(indent, "   "));
    }

    private static class Line {
        final int indent;
        String text = "";

        Line(int indent) {
            this.indent = indent;
        }
    }

}
