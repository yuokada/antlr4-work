package com.baeldung.antlr;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.baeldung.antlr.java.UppercaseMethodListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.Test;

public class JavaParserUnitTest {

    @Test
    public void whenOneMethodStartsWithUpperCase_thenOneErrorReturned() throws Exception{

        String javaClassContent = "public class SampleClass { void DoSomething(){} }";
        Java8Lexer java8Lexer = new Java8Lexer(CharStreams.fromString(javaClassContent));
        CommonTokenStream tokens = new CommonTokenStream(java8Lexer);
        Java8Parser java8Parser = new Java8Parser(tokens);
        ParseTree tree = java8Parser.compilationUnit();
        ParseTreeWalker walker = new ParseTreeWalker();
        UppercaseMethodListener uppercaseMethodListener = new UppercaseMethodListener();
        walker.walk(uppercaseMethodListener, tree);

        assertEquals(1, uppercaseMethodListener.getErrors().size());
        assertEquals(uppercaseMethodListener.getErrors().get(0), "Method DoSomething is uppercased!");
    }
}
