// Sentinel.g4
grammar Sentinel;

// --- Parser Rules ---
// The starting rule for a Sentinel policy file
policy: statement* EOF;

statement:
    variableDeclaration SEMI?
    | assignment SEMI?
    | functionDefinition
    | ruleDefinition
    | importStatement SEMI?
    | ifStatement
    | forStatement
    | returnStatement SEMI?
    | breakStatement SEMI?
    | continueStatement SEMI?
    | expression SEMI? // Simple expressions can also be statements
    ;

// Variable declaration and assignment
variableDeclaration:
    IDENTIFIER ASSIGN expression
    ;

assignment:
    IDENTIFIER assignmentOperator expression
    ;

assignmentOperator:
    ASSIGN
    | ADD_ASSIGN
    | SUB_ASSIGN
    ;

// Function definition
functionDefinition:
    'func' IDENTIFIER '(' (parameterList)? ')' block
    ;

parameterList:
    IDENTIFIER (',' IDENTIFIER)*
    ;

// Rule definition
ruleDefinition:
    'rule' IDENTIFIER block
    ;

// Import statement
importStatement:
    IMPORT STRING_LITERAL (AS IDENTIFIER)?
    ;

// Control flow
ifStatement:
    'if' expression block ('else' block)?
    ;

forStatement:
    // Sentinel style: for collection as key, value { ... }
    FOR expression AS IDENTIFIER (COMMA IDENTIFIER)? block (ELSE block)?
    // Compatibility with current grammar style
    | FOR IDENTIFIER (AS IDENTIFIER)? IN expression block (ELSE block)?
    ;

returnStatement:
    'return' expression?
    ;

breakStatement:
    'break'
    ;

continueStatement:
    'continue'
    ;

block:
    LCURLY statement* RCURLY
    ;

// Expressions:
// 1) postfix (index/field/call)
// 2) multiplicative
// 3) additive
// 4) relational/comparison
// 5) logical
expression:
    IDENTIFIER
    | literal
    | functionLiteral
    | ruleLiteral
    | '(' expression ')'
    | functionCall
    | expression '[' expression ']'
    | expression '.' IDENTIFIER
    | expression '.' IDENTIFIER '(' (argumentList)? ')'
    | expression op=(MUL | DIV | MOD) expression
    | expression op=(ADD | SUB) expression
    | expression op=(LT | GT | LE | GE | EQ | NE) expression // Comparison operators
    | expression IN expression
    | expression CONTAINS expression
    | expression IS expression
    | expression NOT IN expression
    | expression NOT CONTAINS expression
    | expression op=(AND | OR | XOR) expression // Logical operators
    | NOT expression
    | IN expression // 'in' operator for lists/maps
    | CONTAINS expression // 'contains' operator
    | IS expression // 'is' operator
    | listLiteral
    | mapLiteral
    ;

functionCall:
    IDENTIFIER '(' (argumentList)? ')'
    ;

functionLiteral:
    FUNC '(' (parameterList)? ')' block
    ;

ruleLiteral:
    RULE block
    ;

argumentList:
    expression (',' expression)*
    ;


// Literals
literal:
    INT_LITERAL
    | FLOAT_LITERAL
    | STRING_LITERAL
    | BOOLEAN_LITERAL
    | TRUE
    | FALSE
    | NULL_LITERAL
    ;

listLiteral:
    '[' (expression (',' expression)* COMMA?)? ']'
    ;

mapLiteral:
    '{' (keyValuePair (',' keyValuePair)* COMMA?)? '}'
    ;

keyValuePair:
    expression ':' expression
    ;

// --- Lexer Rules ---
// Keywords
TRUE: 'true';
FALSE: 'false';
BOOLEAN_LITERAL: TRUE | FALSE;

NULL_LITERAL: 'null';

FUNC: 'func';
RULE: 'rule';
IMPORT: 'import';
IF: 'if';
ELSE: 'else';
FOR: 'for';
IN: 'in';
AS: 'as';
RETURN: 'return';
BREAK: 'break';
CONTINUE: 'continue';
NOT: 'not';
AND: 'and';
OR: 'or';
XOR: 'xor';
CONTAINS: 'contains';
IS: 'is';

// Operators and Delimiters
ADD: '+';
SUB: '-';
MUL: '*';
DIV: '/';
MOD: '%';
ASSIGN: '=';
ADD_ASSIGN: '+=';
SUB_ASSIGN: '-=';
EQ: '==';
NE: '!=';
LT: '<';
GT: '>';
LE: '<=';
GE: '>=';

LPAREN: '(';
RPAREN: ')';
LBRACK: '[';
RBRACK: ']';
LCURLY: '{';
RCURLY: '}';
COMMA: ',';
DOT: '.';
COLON: ':';
SEMI: ';'; // Semicolon for statement termination

// Identifiers
IDENTIFIER: [a-zA-Z_] ([a-zA-Z0-9_])* ;

// Literals
INT_LITERAL: [0-9]+;
FLOAT_LITERAL: [0-9]+ '.' [0-9]* (('e'|'E') ('+'|'-')? [0-9]+)?
             | '.' [0-9]+ (('e'|'E') ('+'|'-')? [0-9]+)?
             | [0-9]+ ('e'|'E') ('+'|'-')? [0-9]+
             ;

STRING_LITERAL: '"' ( ~('\\'|'"') | ESCAPE_SEQUENCE )* '"'
              | '\'' ( ~('\\'|'\'') | ESCAPE_SEQUENCE )* '\''
              ;

fragment HEX: [0-9a-fA-F];
fragment ESCAPE_SEQUENCE:
    // Keep this permissive for fixture compatibility (e.g. \033, \uXXXX, etc.).
    '\\' ('u' HEX HEX HEX HEX | .)
    ;

// Comments and Whitespace
HASH_COMMENT: '#' ~[\r\n]* -> skip;
LINE_COMMENT: '//' ~[\r\n]* -> skip;
MULTI_LINE_COMMENT: '/*' .*? '*/' -> skip;
WS: [ \t\r\n]+ -> skip; // Whitespace
