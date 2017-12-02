package agh.cs.oop.project1.documentparser;

import agh.cs.oop.project1.Node;

import java.util.List;

interface Splitter {
    List<Node> split(String text);
}
