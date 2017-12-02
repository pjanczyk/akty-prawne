package agh.cs.oop.project1.documentparser;

import agh.cs.oop.project1.Node;

import java.util.List;

final class TerminalSplitter implements Splitter {
    private final Node.Type type;

    public TerminalSplitter(Node.Type type) {
        this.type = type;
    }

    @Override
    public List<Node> split(String text) {
        text = text.replaceFirst("\n\\z", "")
                .replace("-\n", "");

        return List.of(new Node.Builder()
                .setType(type)
                .setText(text)
                .build());
    }
}
