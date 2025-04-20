package com.example.antlr4;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    void testSimpleExpression() throws Exception {
        String expr = "10*(2+3)";
        // 標準出力をキャプチャして実行
        Main.main(new String[] { expr });
        // 実際には System.setOut/capture などで出力を検証するか、
        // EvalVisitor を直接呼び出して結果をアサートします。
        EvalVisitor eval = new EvalVisitor();
        int result = eval.visit(new ExprParser(
            new CommonTokenStream(new ExprLexer(
                CharStreams.fromString(expr)
            ))
        ).prog());
        assertEquals(50, result);
    }
}
