package agh.cs.oop.project1.printer;

import agh.cs.oop.project1.Identifier;
import agh.cs.oop.project1.Node;
import agh.cs.oop.project1.query.Query;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public final class TreePrinter extends IndentAwarePrinter {

    public TreePrinter(int outputWidth) {
        super(outputWidth);
    }

    public void printNode(Node node, boolean tableOfContentMode) {
        if (tableOfContentMode) {
            printTOC(node);
        } else {
            printFully(node);
        }
    }

    public void printQueryResults(List<Query.Result> results, boolean tableOfContentMode) {
        for (Query.Result result : results) {
            String parents = Stream.concat(result.parents.stream(), Stream.of(result.node))
                    .map(this::makeNodeDescription)
                    .filter(Objects::nonNull)
                    .collect(joining(" "));

            write("• %s\n", parents);
            indented(() -> {
                if (result.node.children != null) {
                    if (tableOfContentMode) {
                        printTOCChildren(result.node);
                    } else {
                        printFullyChildren(result.node);
                    }
                }
            });
            write("\n");
        }
    }

    private void printFully(Node node) {
        switch (node.type) {
            case Document:
                write("%s\n", removeLineBreaks(node.text));
                printFullyChildren(node);
                break;
            case Part:
                write("Dział %s\n%s\n", node.identifier, removeLineBreaks(node.text));
                printFullyChildren(node);
                break;
            case Chapter:
                write("Rozdział %s\n%s\n", node.identifier, removeLineBreaks(node.text));
                printFullyChildren(node);
                break;
            case Title:
                write("%s\n", removeLineBreaks(node.text));
                printFullyChildren(node);
                break;
            case Article:
                write("Art. %s.\n", node.identifier);
                printFullyChildren(node);
                break;
            case Paragraph:
                write("%s. ", node.identifier);
                printFullyChildren(node);
                break;
            case Point:
                write("%s) ", node.identifier);
                printFullyChildren(node);
                break;
            case Letter:
                write("%s) ", node.identifier);
                printFullyChildren(node);
                break;
            case PlainText:
                write("%s\n", removeLineBreaks(node.text));
                break;
        }
    }

    private void printFullyChildren(Node node) {
        indented(() -> node.children.forEach(this::printFully));
    }

    private void printTOC(Node node) {
        switch (node.type) {
            case Document:
                write("%s\n", removeLineBreaks(node.text));
                printTOCChildren(node);
                break;
            case Part:
                write("Dział %s - %s\n", node.identifier, removeLineBreaks(node.text));
                printTOCChildren(node);
                break;
            case Chapter:
                write("Rozdział %s - %s\n", node.identifier, removeLineBreaks(node.text));
                printTOCChildren(node);
                break;
            case Title:
                write("%s\n", removeLineBreaks(node.text));
                printTOCChildren(node);
                break;
        }
    }

    private void printTOCChildren(Node node) {
        indented(() -> {
            Identifier articleRangeStart = null;
            Identifier articleRangeEnd = null;

            for (Node n : node.children) {
                if (n.type == Node.Type.Article) {
                    if (articleRangeStart == null) {
                        articleRangeStart = n.identifier;
                    }
                    articleRangeEnd = n.identifier;
                } else {
                    if (articleRangeStart != null) {
                        printArticleRange(articleRangeStart, articleRangeEnd);
                        articleRangeStart = null;
                        articleRangeEnd = null;
                    }
                    printTOC(n);
                }
            }
            if (articleRangeStart != null) {
                printArticleRange(articleRangeStart, articleRangeEnd);
            }
        });
    }

    private void printArticleRange(Identifier start, Identifier end) {
        if (Objects.equals(start, end)) {
            write("art. %s\n", start);
        } else {
            write("art. %s-%s\n", start, end);
        }
    }

    private @Nullable String makeNodeDescription(Node node) {
        switch (node.type) {
            case Part:
                return String.format("dział %s", node.identifier);
            case Chapter:
                return String.format("rozdział %s", node.identifier);
            case Article:
                return String.format("art. %s", node.identifier);
            case Paragraph:
                return String.format("ust. %s", node.identifier);
            case Point:
                return String.format("pkt %s", node.identifier);
            case Letter:
                return String.format("lit. %s", node.identifier);
            default:
                return null;
        }
    }

    private static String removeLineBreaks(String str) {
        return str.replace('\n', ' ');
    }
}
