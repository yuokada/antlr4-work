package com.baeldung.antlr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

public class Hellog4Test {
    @Test
    public void testGreeting() {
        CharStream input = CharStreams.fromString("hello world");
        HelloLexer lexer = new HelloLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HelloParser parser = new HelloParser(tokens);
        ParseTree tree = parser.greeting();
        assertEquals("(greeting hello world)", tree.toStringTree(parser));
    }

    @Test
    public void testGreetingWithName() {
        String name = "Adam";
        CharStream input = CharStreams.fromString("hello %s".formatted(name));
        HelloLexer lexer = new HelloLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        HelloParser parser = new HelloParser(tokens);
        ParseTree tree = parser.greeting();
        assertEquals("(greeting hello %s)".formatted(name), tree.toStringTree(parser));
    }
}
