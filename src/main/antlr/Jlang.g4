grammar Jlang;

/**
 * Parser Rules
 */

program: (extended_statement | NL)* EOF;

extended_statement
    : function_declaration
    | statement
    ;

statement
    : variable_declaration
    | variable_assignment
    | function_call
    | scoped_loop
    | scoped_if_statement
    ;

function_call
    : ID LPAREN ( argument_list? ) RPAREN #functionCall
    ;

function_declaration
    : function_signature scoped_body_with_return #functionDeclaration
    ;

function_signature
     : FUNCTION_DECLARATION_PREFIX ID LPAREN parameter_list? RPAREN TYPE_DECLARATION INT_TYPE #intFunctionDeclaration
     | FUNCTION_DECLARATION_PREFIX ID LPAREN parameter_list? RPAREN TYPE_DECLARATION DOUBLE_TYPE #doubleFunctionDeclaration
     | FUNCTION_DECLARATION_PREFIX ID LPAREN parameter_list? RPAREN TYPE_DECLARATION VOID_TYPE #voidFunctionDeclaration
     ;

parameter_list
    : parameter ( COMMA parameter )*
    ;

parameter
    : INT_TYPE ID #intParameter
    | DOUBLE_TYPE ID #doubleParameter
    ;

scoped_loop
    : LOOP_SEQUENCE scoped_body #loopDeclaration
    ;

scoped_if_statement
    : IF_SEQUENCE logical_expression scoped_body #scopedifStatement
    ;

logical_expression
    : logical_element ( AND logical_element )* #andExpression
    | logical_element ( OR logical_element )* #orExpression
    | logical_element ( EQ logical_element )* #eqExpression
    ;

logical_element
    : ID
    | FALSE
    | TRUE
    | arithmetic_logical_expression
    ;

arithmetic_logical_expression
    : ID EQ expression0 #arithmeticLogicalExpression
    ;

scoped_body
    : SCOPE_BEGIN (statement | NL)* SCOPE_END #scopeDecleration
    ;

scoped_body_with_return
    : SCOPE_BEGIN (statement | NL)* return_value SCOPE_END #scopeWithReturnDecleration
    ;

return_value
    : RETURN_SEQUENCE RETURN_VOID #returnVoid
    | RETURN_SEQUENCE ID #returnVariable
    ;

argument_list
    : expression0 ( COMMA expression0 )*
    ;

variable_declaration
    : VAR_DECLARATION ID TYPE_DECLARATION INT_TYPE #intDeclaration
    | VAR_DECLARATION ID TYPE_DECLARATION DOUBLE_TYPE #realDeclaration
    | VAR_DECLARATION ID TYPE_DECLARATION BOOLEAN_TYPE #booleanDeclaration
    | VAR_DECLARATION ID ASSIGNMENT_DECLARATION function_call #functionalAssignment
    | VAR_DECLARATION ID ASSIGNMENT_DECLARATION expression0 #variableDeclarationWithAssignment
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
    | boolean #bool
    | ID #variable
    | STRING #string
    | AMPERSAND ID #variableAddress
    | LPAREN expression0 RPAREN #parenthesis
    ;

boolean:
    | TRUE
    | FALSE
    ;

/**
 * Lexer Rules
 */

VOID_TYPE: 'wojdem';
INT_TYPE: 'intem';
DOUBLE_TYPE: 'rzeczywiste' ;
BOOLEAN_TYPE: 'logiczne' ;
VAR_DECLARATION: 'no to mamy';
TYPE_DECLARATION: 'co jest' ;
ASSIGNMENT_DECLARATION: 'rowne' ;
ARITHMETIC_ASSIGNMENT: 'bedzie drodzy panstwo' ;
FUNCTION_ASSIGNMENT: 'z glowy';
FUNCTION_DECLARATION_PREFIX: 'ciach ciach';
RETURN_SEQUENCE: 'pach pach' ;
RETURN_VOID: 'nic';
SCOPE_BEGIN: 'tu jest start';
SCOPE_END: 'no i tyle';
LOOP_SEQUENCE: 'tak w kolo';
IF_SEQUENCE: 'gdyby tak';


PLUS : '+' ;
MINUS : '-' ;
TIMES : '*' ;
DIVIDE : '/' ;
AND : 'i' ;
OR : 'albo' ;
EQ : '==' ;
COMMA : ',' ;
LPAREN : '(' ;
RPAREN : ')' ;
LCURLY : '{' ;
RCURLY : '}' ;
PT : '.' ;
AMPERSAND : '&' ;
TRUE : 'prawda' ;
FALSE : 'klamstwo' ;


INT : [0-9]+ ;
DBL : [0-9]+ '.' [0-9]+ ;
ID: [a-zA-Z_][a-zA-Z_0-9]* ;
STRING: '\'' ( ESCAPED_CHAR | ~'\'' )* '\'';
fragment ESCAPED_CHAR: '\\' ('n' | 't' | 'r' | 'b' | '\'' | '\\');
WS: [ \t\r\f]+ -> skip ;
NL: '\r'? '\n' ;
