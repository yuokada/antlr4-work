grammar Expr;

// エントリポイント
prog: expr EOF ;

// 式の定義：+ - * /
expr
    : expr op=('+'|'-') expr   # AddSub
    | expr op=('*'|'/') expr   # MulDiv
    | INT                      # Int
    | '(' expr ')'             # Parens
    ;

INT : [0-9]+ ;
WS  : [ \t\r\n]+ -> skip ;
