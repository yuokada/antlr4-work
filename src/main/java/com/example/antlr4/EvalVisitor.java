package com.example.antlr4;

import com.example.antlr4.ExprParser.*;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class EvalVisitor extends ExprBaseVisitor<Integer> {
    @Override
    public Integer visitAddSub(AddSubContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        return ctx.op.getType() == ExprParser.PLUS ? left + right : left - right;
    }

    @Override
    public Integer visitMulDiv(MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));
        return ctx.op.getType() == ExprParser.MUL ? left * right : left / right;
    }

    @Override
    public Integer visitInt(IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitParens(ParensContext ctx) {
        return visit(ctx.expr());
    }
}
