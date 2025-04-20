package com.example.antlr4;

//import org.antlr.v4.runtime.*;
//import org.antlr.v4.runtime.tree.*;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

public class Main {
    public static void main(String[] args) throws Exception {
        String inputExpr = args.length > 0 ? args[0] : "1+2*3";
        // 入力ストリーム
        CharStream input = CharStreams.fromString(inputExpr);
        // レキシング & トークン化
        ExprLexer lexer = new ExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // パース
        ExprParser parser = new ExprParser(tokens);
        ParseTree tree = parser.prog();
        // 解析結果ツリーを表示
        System.out.println(tree.toStringTree(parser));

        // 評価ビジターを走らせる
        EvalVisitor visitor = new EvalVisitor();
        Integer result = visitor.visit(tree);
        System.out.printf("Result: %d%n", result);
    }
}
