grammar Jlang;

/**
 * Parser Rules
 */

program: (statement | NL)* EOF;

statement
    : variable_declaration
    | variable_assignment
//    | function_call #functionCall
    ;

variable_declaration
    : VAR_DECLARATION ID TYPE_DECLARATION INT_TYPE #intDeclaration
    | VAR_DECLARATION ID TYPE_DECLARATION DOUBLE_TYPE #realDeclaration
    | VAR_DECLARATION ID ASSIGNMENT_DECLARATION expression0 #variableDeclarationWithAssignment
//    |
    ;

variable_assignment
    : ID ARITHMETIC_ASSIGNMENT expression0 #variableAssignment
    ;

expression0
    : expression1 #singleExpression0
    | expression0 PLUS expression1 #addition
    | expression0 MINUS expression1 #subtraction
    ;

expression1
    : expression2 #singleExpression1
    | expression1 TIMES expression2 #multiplication
    | expression1 DIVIDE expression2 #division
    ;

expression2
    : INT #int
    | DBL #double
    | ID #variable
    | LPAREN expression0 RPAREN #parenthesis
    ;

//variable_assignment
//    : VAR_DECLARATION ID ASSIGNMENT_DECLARATION (
//        NUMBER
//        |
//        arithmetic_statement
//      )
//    ;
//
//arithmetic_assignment
//    : ID ARITHMETIC_ASSIGNMENT arithmetic_statement
//    ;
//
//arithmetic_statement
//    : arithmetic_term ((PLUS | MINUS) arithmetic_term)*
//    ;
//
//arithmetic_term
//    : arithmetic_factor ((TIMES | DIVIDE) arithmetic_factor)*
//    ;
//
//arithmetic_factor
//    : NUMBER
//    | ID
//    | LPAREN arithmetic_statement RPAREN
//    ;
//
//function_call
//    : ID LPAREN argument_list? RPAREN
//    ;
//
//argument_list
//    : expr (COMMA expr)*
//    ;
//
//expr
//    : ID
//    | NUMBER
//    | STRING
//    | func
//    | NOT expr
//    | expr AND expr
//    | expr OR expr
//    | arithmetic_statement
//    ;
//
//func
//    : ID LPAREN argument_list? RPAREN
//    ;
//
/**
 * Lexer Rules
 */

//NUMBER_TYPE : INT_TYPE | REAL_TYPE ;
//NUMBER : INT | DBL ;

INT_TYPE: 'intem';
DOUBLE_TYPE: 'rzeczywiste' ;
VAR_DECLARATION: 'no to mamy';
TYPE_DECLARATION: 'co jest' ;
ASSIGNMENT_DECLARATION: 'rowne' ;
ARITHMETIC_ASSIGNMENT: 'bedzie drodzy panstwo' ;

PLUS : '+' ;
MINUS : '-' ;
TIMES : '*' ;
DIVIDE : '/' ;
AND : 'and' ;
OR : 'or' ;
NOT : 'not' ;
EQ : '=' ;
COMMA : ',' ;
LPAREN : '(' ;
RPAREN : ')' ;
LCURLY : '{' ;
RCURLY : '}' ;
PT : '.' ;

INT : [0-9]+ ;
DBL : [0-9]+ '.' [0-9]+ ;
ID: [a-zA-Z_][a-zA-Z_0-9]* ;
STRING: '\'' ( ESCAPED_CHAR | ~'\'' )* '\'';
fragment ESCAPED_CHAR: '\\' ('n' | 't' | 'r' | 'b' | '\'' | '\\');
WS: [ \t\r\f]+ -> skip ;
NL: '\r'? '\n' ;
