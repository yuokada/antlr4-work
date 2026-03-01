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
