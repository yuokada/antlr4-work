package com.baeldung.antlr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

public class SentinelParserUnitTest {

    @Test
    public void whenValidSentinelInput_thenParsesWithoutSyntaxError() {
        String input = """
                x = 1;
                if x {
                  return x;
                }
                """;

        SentinelLexer lexer = new SentinelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SentinelParser parser = new SentinelParser(tokens);
        SyntaxErrorCollector errors = new SyntaxErrorCollector();
        parser.removeErrorListeners();
        parser.addErrorListener(errors);

        parser.policy();

        assertEquals(0, errors.count());
    }

    @Test
    public void whenMissingSemicolon_thenSyntaxErrorIsReported() {
        String input = """
                x = 1
                return x;
                """;

        SentinelLexer lexer = new SentinelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SentinelParser parser = new SentinelParser(tokens);
        SyntaxErrorCollector errors = new SyntaxErrorCollector();
        parser.removeErrorListeners();
        parser.addErrorListener(errors);

        parser.policy();

        assertTrue(errors.count() > 0);
    }

    @Test
    public void whenExpressionUsesMultiplyAndAdd_thenMultiplyBindsStrongerThanAdd() {
        SentinelParser parser = newParser("1 + 2 * 3");
        SyntaxErrorCollector errors = new SyntaxErrorCollector();
        parser.removeErrorListeners();
        parser.addErrorListener(errors);

        ParseTree tree = parser.expression();

        assertEquals(0, errors.count());
        assertEquals(
            "(expression (expression (literal 1)) + (expression (expression (literal 2)) * (expression (literal 3))))",
            tree.toStringTree(parser));
    }

    @Test
    public void whenExpressionUsesInContainsIs_thenTheyParseAsUnaryOperators() {
        SentinelParser inParser = newParser("in value");
        SentinelParser containsParser = newParser("contains items");
        SentinelParser isParser = newParser("is null");
        SyntaxErrorCollector inErrors = new SyntaxErrorCollector();
        SyntaxErrorCollector containsErrors = new SyntaxErrorCollector();
        SyntaxErrorCollector isErrors = new SyntaxErrorCollector();

        inParser.removeErrorListeners();
        containsParser.removeErrorListeners();
        isParser.removeErrorListeners();
        inParser.addErrorListener(inErrors);
        containsParser.addErrorListener(containsErrors);
        isParser.addErrorListener(isErrors);

        ParseTree inTree = inParser.expression();
        ParseTree containsTree = containsParser.expression();
        ParseTree isTree = isParser.expression();

        assertEquals(0, inErrors.count());
        assertEquals(0, containsErrors.count());
        assertEquals(0, isErrors.count());
        assertEquals("(expression in (expression value))", inTree.toStringTree(inParser));
        assertEquals("(expression contains (expression items))",
            containsTree.toStringTree(containsParser));
        assertEquals("(expression is (expression (literal null)))", isTree.toStringTree(isParser));
    }

    private SentinelParser newParser(String input) {
        SentinelLexer lexer = new SentinelLexer(CharStreams.fromString(input));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        return new SentinelParser(tokens);
    }

    private static class SyntaxErrorCollector extends BaseErrorListener {
        private final List<String> messages = new ArrayList<>();

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
            int charPositionInLine, String msg, RecognitionException e) {
            messages.add("line %d:%d %s".formatted(line, charPositionInLine, msg));
        }

        public int count() {
            return messages.size();
        }
    }
}
