// Sentinel.g4
grammar Sentinel;

// --- Parser Rules ---
// The starting rule for a Sentinel policy file
policy: (statement | NEWLINE)* EOF;

statement:
    variableDeclaration ';'
    | assignment ';'
    | functionDefinition
    | ruleDefinition
    | importStatement ';'
    | ifStatement
    | forStatement
    | returnStatement ';'
    | breakStatement ';'
    | continueStatement ';'
    | expression ';' // Simple expressions can also be statements
    ;

// Variable declaration and assignment
variableDeclaration:
    IDENTIFIER '=' expression
    ;

assignment:
    IDENTIFIER '=' expression
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
    'import' STRING_LITERAL
    ;

// Control flow
ifStatement:
    'if' expression block ('else' block)?
    ;

forStatement:
    'for' IDENTIFIER ('as' IDENTIFIER)? 'in' expression block ('else' block)?
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
    '{' (statement | NEWLINE)* '}'
    ;

// Expressions (simplified)
expression:
    IDENTIFIER
    | literal
    | '(' expression ')'
    | expression '[' expression ']'
    | expression '.' IDENTIFIER
    | expression '.' IDENTIFIER '(' (argumentList)? ')'
    | expression op=(MUL | DIV | MOD) expression
    | expression op=(ADD | SUB) expression
    | expression op=(LT | GT | LE | GE | EQ | NE) expression // Comparison operators
    | expression op=(AND | OR | XOR) expression // Logical operators
    | NOT expression
    | IN expression // 'in' operator for lists/maps
    | CONTAINS expression // 'contains' operator
    | IS expression // 'is' operator
    | listLiteral
    | mapLiteral
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
    | NULL_LITERAL
    ;

listLiteral:
    '[' (expression (',' expression)*)? ']'
    ;

mapLiteral:
    '{' (keyValuePair (',' keyValuePair)*)? '}'
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
    '\\' ('b'|'t'|'n'|'f'|'r'|'u' HEX HEX HEX HEX | '"'|'\''|'\\') // Basic escapes; \uXXXX for unicode
    ;

// Comments and Whitespace
LINE_COMMENT: '//' ~[\r\n]* -> skip;
MULTI_LINE_COMMENT: '/*' .*? '*/' -> skip;
WS: [ \t\r\n]+ -> skip; // Whitespace

NEWLINE: '\r'? '\n'; // For basic line termination, though Sentinel also has automatic semicolon insertion.
                     // The ANTLR grammar would need to carefully manage automatic semicolon insertion if desired.
                     // For simplicity in this basic example, explicit semicolons are used.
