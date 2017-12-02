package agh.cs.oop.project1.query;

import agh.cs.oop.project1.Identifier;
import agh.cs.oop.project1.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public final class Query {

    public final List<Segment> segments;

    public Query(List<Segment> segments) {
        this.segments = segments;
    }

    public List<Result> apply(Node node) {
        Stream<Result> nodes = Stream.of(new Result(node, List.of()));

        for (Segment segment : segments) {
            nodes = nodes.flatMap(result -> segment.apply(result.node, result.parents));
        }

        return nodes.collect(toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Query query = (Query) o;
        return Objects.equals(segments, query.segments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(segments);
    }

    @Override
    public String toString() {
        return segments.stream()
                .map(Segment::toString)
                .collect(joining(", "));
    }

    public static class Segment {
        public final Node.Type type;
        public final Identifier startIdentifier;
        public final Identifier endIdentifier;

        public Segment(Node.Type type, Identifier identifier) {
            this.type = type;
            this.startIdentifier = identifier;
            this.endIdentifier = identifier;
        }

        public Segment(Node.Type type, Identifier startIdentifier, Identifier endIdentifier) {
            this.type = type;
            this.startIdentifier = startIdentifier;
            this.endIdentifier = endIdentifier;
        }

        public boolean matches(Node node) {
            return node.type == type
                    && node.identifier != null
                    && node.identifier.isInRange(startIdentifier, endIdentifier);
        }

        public Stream<Result> apply(Node node, List<Node> parents) {
            if (matches(node)) {
                return Stream.of(new Result(node, parents));
            } else if (node.children != null) {
                List<Node> newParents = new ArrayList<>(parents);
                newParents.add(node);
                return node.children.stream().flatMap(n -> apply(n, newParents));
            } else {
                return Stream.empty();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Segment segment = (Segment) o;
            return Objects.equals(type, segment.type) &&
                    Objects.equals(startIdentifier, segment.startIdentifier) &&
                    Objects.equals(endIdentifier, segment.endIdentifier);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, startIdentifier, endIdentifier);
        }

        @Override
        public String toString() {
            return type.toString() + " " + startIdentifier + "-" + endIdentifier;
        }
    }

    public static class Result {
        public final Node node;
        public final List<Node> parents;

        public Result(Node node, List<Node> parents) {
            this.node = node;
            this.parents = Collections.unmodifiableList(new ArrayList<>(parents)); // defensive copy
        }
    }

}
