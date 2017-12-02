package agh.cs.oop.project1;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Node {
    public final Type type;
    public final @Nullable Identifier identifier;
    public final @Nullable String text;
    public final @Nullable List<Node> children;

    private Node(Type type, @Nullable Identifier identifier, @Nullable String text, @Nullable List<Node> children) {
        if (type == null) throw new NullPointerException();

        boolean hasIdentifier = identifier != null;
        boolean hasText = text != null;
        boolean hasChildren = children != null;

        if (hasIdentifier != type.hasIdentifier || hasText != type.hasText || hasChildren != type.hasChildren) {
            throw new IllegalArgumentException();
        }

        this.type = type;
        this.identifier = identifier;
        this.text = text;
        this.children = hasChildren ? Collections.unmodifiableList(new ArrayList<>(children)) : null; // defensive copy
    }

    public enum Type {
        Document(0, false, true, true),   // dokument
        Part(1, true, true, true),        // dział
        Chapter(2, true, true, true),     // rozdział
        Title(3, false, true, true),      // tytuł
        Article(4, true, false, true),    // artykuł
        Paragraph(5, true, false, true),  // ustęp
        Point(6, true, false, true),      // punkt
        Letter(7, true, false, true),     // litera
        PlainText(8, false, true, false); // czysty tekst

        public final int level;
        public final boolean hasIdentifier;
        public final boolean hasText;
        public final boolean hasChildren;

        Type(int level, boolean hasIdentifier, boolean hasText, boolean hasChildren) {
            this.level = level;
            this.hasIdentifier = hasIdentifier;
            this.hasText = hasText;
            this.hasChildren = hasChildren;
        }
    }

    public static class Builder {
        private Type type;
        private String text;
        private Identifier identifier;
        private List<Node> children;

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setIdentifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        public Builder setChildren(List<Node> children) {
            this.children = children;
            return this;
        }

        public Node build() {
            try {
                return new Node(type, identifier, text, children);
            } catch (NullPointerException | IllegalArgumentException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}
