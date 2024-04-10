grammar Jlang;

/**
 * Parser Rules
 */

program
    : content ;

content
    : stat content* EOF
    | def content* EOF
    | variable_declaration content* EOF
    | variable_assignment content* EOF
    | arithmetic_assignment content* EOF
    ;

stat: ID '=' expr
    | expr
    ;

def : ID '(' ID (',' ID)* ')' '{' stat* '}' ;

expr: ID
    | INT
    | func
    | 'not' expr
    | expr 'and' expr
    | expr 'or' expr
    ;

func : ID '(' expr (',' expr)* ')' |
standard_hardcoded_function_with_string_input;

arithmetic_assignment :
ID ARITHMETIC_ASSIGNMENT arithmetic_statement ;

arithmetic_statement :
ID arithmetic_statement_content |
NUMBER arithmetic_statement_content* ;

arithmetic_statement_content :
ARITHMETIC_SIGN ID
    arithmetic_statement_content* |
ARITHMETIC_SIGN NUMBER
    arithmetic_statement_content* |
ARITHMETIC_SIGN computed_in_brackets
    arithmetic_statement_content* ;

standard_hardcoded_function_with_string_input :
    HARDCODED_FUNCTIONS_WITH_STRING_INPUTS
    '(' STRING ')' ;

computed_in_brackets:
    '(' arithmetic_statement ')' ;

variable_assignment :
VAR_DECLARATION ID
    ASSIGNMENT_DECLARATION NUMBER |
 VAR_DECLARATION ID
    ASSIGNMENT_DECLARATION arithmetic_statement ;

variable_declaration :
VAR_DECLARATION ID
    TYPE_DECLARATION NUMBER_TYPE ;


/**
 * Lexer Rules
 */

HARDCODED_FUNCTIONS_WITH_STRING_INPUTS :
    STD_READ |
    STD_INPUT ;

NUMBER_TYPE : INT_TYPE | REAL_TYPE ;
NUMBER : INT | DBL ;

INT_TYPE: 'intem' ;
REAL_TYPE: 'rzeczywiste' ;
VAR_DECLARATION: 'no to mamy';
TYPE_DECLARATION: 'co jest' ;
ASSIGNMENT_DECLARATION: 'rowne' ;
ARITHMETIC_ASSIGNMENT: 'bedzie drodzy panstwo' ;
STD_READ : 'na zachodzie jest' ;
STD_INPUT : 'lewa reka za prawe ucho' ;

ARITHMETIC_SIGN
        : PLUS
        | MINUS
        | TIMES
        | DIVIDE
        ;


PLUS : '+' ;
MINUS : '-' ;
TIMES : '*' ;
DIVIDE : '/' ;
AND : 'and' ;
OR : 'or' ;
NOT : 'not' ;
EQ : '=' ;
COMMA : ',' ;
SEMI : ';' ;
LPAREN : '(' ;
RPAREN : ')' ;
LCURLY : '{' ;
RCURLY : '}' ;
PT : '.' ;



INT : [0-9]+ ;
DBL : INT+ PT INT+
    | PT INT+
    | INT+
    | INT PT
    ;
ID: [a-zA-Z_][a-zA-Z_0-9]* ;
STRING: '\'' [a-zA-Z \t]* '\'' ;
WS: [ \t\n\r\f]+ -> skip ;


