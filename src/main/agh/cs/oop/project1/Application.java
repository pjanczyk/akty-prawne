package agh.cs.oop.project1;

import agh.cs.oop.project1.commandargs.CommandArgs;
import agh.cs.oop.project1.commandargs.CommandArgsParser;
import agh.cs.oop.project1.documentparser.DocumentParser;
import agh.cs.oop.project1.printer.TreePrinter;
import agh.cs.oop.project1.query.Query;
import agh.cs.oop.project1.query.QueryParser;
import agh.cs.oop.project1.util.FileUtils;

import java.io.IOException;
import java.util.List;

import static java.lang.System.*;

public class Application {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        // Parse the command-line arguments
        CommandArgs commandArgs;
        try {
            commandArgs = new CommandArgsParser().parseArguments(List.of(args));
        } catch (InvalidFormatException e) {
            err.println("Błąd: Niepoprawne argumenty");
            err.flush();
            printUsage();
            return;
        }

        // Parse query (optionally)
        Query query = null;
        if (commandArgs.query != null) {
            try {
                query = new QueryParser().parseQuery(commandArgs.query);
            } catch (InvalidFormatException e) {
                err.println(String.format("Błąd: Niepoprawny format zapytania: \"%s\"", commandArgs.query));
                err.flush();
                printUsage();
                return;
            }
        }

        // Read a file
        String fileContent;
        try {
            fileContent = FileUtils.readFile(commandArgs.sourceFilePath);
        } catch (IOException e) {
            err.println(String.format("Błąd wczytywania pliku \"%s\"", commandArgs.sourceFilePath));
            return;
        }

        // Build a tree from the file content
        Node tree;
        try {
            tree = new DocumentParser().parse(fileContent);
        } catch (InvalidFormatException e) {
            err.println("Błąd: Nieobsługiwany lub błędny format dokumentu");
            err.println("      Obecnie wspierane są dokumenty \"konstytucja.txt\" oraz \"uokik.txt\"");
            return;
        }

        TreePrinter printer = new TreePrinter(80);

        // Search the tree (optionally) & print it
        if (query != null) {
            List<Query.Result> results = query.apply(tree);
            if (results.isEmpty()) {
                out.println("Nie znaleziono wyników");
            } else {
                printer.printQueryResults(results, commandArgs.showTableOfContent);
                out.print(printer.toString());
                out.println();
                out.println("-------------------------");
                out.println(String.format("Liczba wyników: %d", results.size()));
            }
        } else {
            printer.printNode(tree, commandArgs.showTableOfContent);
            out.print(printer.toString());
        }
    }

    private static void printUsage() {
        out.println("Użycie:  akty-prawne.jar ŚCIEŻKA [-l] [ZAPYTANIE]");
        out.println("Wyświetla całość lub fragment Konstytucji RP lub Ustawy o ochronie konkurencji i konsumentów.");
        out.println();
        out.println("  -l    wyświetla spis treści");
        out.println();
        out.println("Zapytanie może składać się z następujących części (kropki i polskie znaki są opcjonalne):");
        out.println("  dział 1 rozdział 2 art. 3 ust. 4 pkt 5 lit. a-b");
        out.println();
        out.println("Przykłady:");
        out.println("  akty-prawne.jar konstytucja.txt -l                     - wyświetla spis treści konstytucji");
        out.println("  akty-prawne.jar konstytucja.txt \"rozdział VIII\"        - wyświetla treść rozdziału \"Sądy i Trybunały\"");
        out.println("  akty-prawne.jar konstytucja.txt \"art. 194 ust. 1\"      - wyświetla treść artykułu 194, ustęp 1");
        out.println("  akty-prawne.jar uokik.txt -l \"dział III rozdział 1-2\"  - wyświetla spis treści 2 pierwszych rozdziałów 3 działu");
        out.println();
        out.println("© 2017  Piotr Janczyk.  Licencja MIT.");
    }

}
