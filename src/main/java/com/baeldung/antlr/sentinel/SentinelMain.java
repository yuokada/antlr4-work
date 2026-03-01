package com.baeldung.antlr.sentinel;

import com.baeldung.antlr.SentinelLexer;
import com.baeldung.antlr.SentinelParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class SentinelMain {
    public static void main(String[] args) throws Exception {
        System.out.printf("Enter a Sentinel policy (Press ENTER and then Ctrl+D): ");
        CharStream input = CharStreams.fromStream(System.in);

        SentinelLexer lexer = new SentinelLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SentinelParser parser = new SentinelParser(tokens);

        ParseTree tree = parser.policy();
        System.out.println("Parse tree: " + tree.toStringTree(parser));
    }
}
