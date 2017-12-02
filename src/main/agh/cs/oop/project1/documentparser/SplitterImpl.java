package agh.cs.oop.project1.documentparser;

import agh.cs.oop.project1.Identifier;
import agh.cs.oop.project1.InvalidFormatException;
import agh.cs.oop.project1.Node;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class SplitterImpl implements Splitter {

    private final Node.Type type;
    private final Pattern headerPattern;
    private final Splitter innerSplitter;

    public SplitterImpl(Node.Type type, Pattern headerPattern, Splitter innerSplitter) {
        this.type = type;
        this.headerPattern = headerPattern;
        this.innerSplitter = innerSplitter;
    }

    public List<Node> split(String text) {
        Matcher matcher = headerPattern.matcher(text);

        List<Node> nodes = new ArrayList<>();

        int start = 0;
        boolean hasHeader = false;
        Identifier headerIdentifier = null;
        String headerText = null;

        while (matcher.find()) { // Match header
            int end = matcher.start();

            // Handle previous part (before the match)
            handlePart(nodes, text.substring(start, end), hasHeader, headerIdentifier, headerText);

            hasHeader = true;
            headerIdentifier = tryGetGroup(matcher, "identifier")
                    .map(t -> Identifier.tryParse(t).orElseThrow(InvalidFormatException::new))
                    .orElse(null);
            headerText = tryGetGroup(matcher, "text").orElse(null);

            start = matcher.end();
        }

        int end = text.length();

        // Handle last part
        handlePart(nodes, text.substring(start, end), hasHeader, headerIdentifier, headerText);

        return nodes;
    }

    private static Optional<String> tryGetGroup(Matcher matcher, String name) {
        try {
            return Optional.ofNullable(matcher.group(name));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private void handlePart(List<Node> nodes,
                            String partText,
                            boolean hasHeader,
                            @Nullable Identifier headerIdentifier,
                            @Nullable String headerText) {

        List<Node> children;
        if (partText.isEmpty()) {
            children = List.of();
        } else {
            children = innerSplitter.split(partText);
        }

        if (hasHeader) {
            nodes.add(new Node.Builder()
                    .setType(type)
                    .setChildren(children)
                    .setIdentifier(headerIdentifier)
                    .setText(headerText)
                    .build());
        } else {
            nodes.addAll(children);
        }
    }

}
