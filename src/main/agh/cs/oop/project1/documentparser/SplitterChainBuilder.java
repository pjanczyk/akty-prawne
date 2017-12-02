package agh.cs.oop.project1.documentparser;

import agh.cs.oop.project1.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

final class SplitterChainBuilder {

    private final List<Element> elements = new ArrayList<>();

    public SplitterChainBuilder splitBy(Node.Type type, Pattern headerPattern) {
        this.elements.add(new Element(type, headerPattern));
        return this;
    }

    public Splitter endWith(Node.Type type) {
        // fold right
        Splitter splitter = new TerminalSplitter(type);
        for (int i = elements.size() - 1; i >= 0; i--) {
            splitter = new SplitterImpl(elements.get(i).type, elements.get(i).headerPattern, splitter);
        }
        return splitter;
    }

    private static class Element {
        final Node.Type type;
        final Pattern headerPattern;

        Element(Node.Type type, Pattern headerPattern) {
            this.type = type;
            this.headerPattern = headerPattern;
        }
    }
}
