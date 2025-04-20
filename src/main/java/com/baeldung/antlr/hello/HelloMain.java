package com.baeldung.antlr.hello;

import com.baeldung.antlr.HelloLexer;
import com.baeldung.antlr.HelloParser;
import org.antlr.v4.runtime.CharStream;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class HelloMain {
    public static void main(String[] args) throws Exception {
        // Create a CharStream that reads from standard input
        CharStream input = CharStreams.fromStream(System.in);

        // Create a lexer that feeds off of the input CharStream
        HelloLexer lexer = new HelloLexer(input);

        // Create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Create a parser that feeds off the tokens buffer
        HelloParser parser = new HelloParser(tokens);

        // Begin parsing at the 'greeting' rule
        ParseTree tree = parser.greeting();

        // Print the LISP-style tree
        System.out.println(tree.toStringTree(parser));
    }
}
