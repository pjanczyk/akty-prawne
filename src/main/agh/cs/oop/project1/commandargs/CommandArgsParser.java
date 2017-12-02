package agh.cs.oop.project1.commandargs;

import agh.cs.oop.project1.InvalidFormatException;

import java.util.List;

import static java.util.stream.Collectors.*;

public final class CommandArgsParser {

    /**
     * @throws InvalidFormatException if there is an invalid number of arguments
     */
    public CommandArgs parseArguments(List<String> args) {
        boolean showTableOfContent = args.stream().anyMatch("-l"::equalsIgnoreCase);

        args = args.stream()
                .filter(arg -> !"-l".equalsIgnoreCase(arg))
                .collect(toList());

        if (args.size() != 1 && args.size() != 2) throw new InvalidFormatException();

        String filePath = args.get(0);
        String query = args.size() == 2 ? args.get(1) : null;

        return new CommandArgs(filePath, showTableOfContent, query);
    }
}
