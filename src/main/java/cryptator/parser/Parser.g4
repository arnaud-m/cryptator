grammar Parser;

//To run

// Parser Rules

program : equation ;
 
equation : e1=expression COMPARATEUR e2=expression {};


expression :
            symbol {}
            | '(' expression ')' {}
            | e1=expression modORpow e2=expression {}
            | e1=expression divORmul e2=expression {}
            | e1=expression addORsub e2=expression {};

symbol : LETTER+ {};

modORpow : '%' | '^';

divORmul : '/' | '*';

addORsub : '+' | '-';

//term : LETTER symbol{};
//
//symbol : LETTER symbol
//        | ;
 
// Lexer Rules
 
//CALC : '+' | '-' | '*' | '/' | '%' | '^';

COMPARATEUR : '=' | '<' | '>' | '<=' | '>=';
 
LETTER : [a-zA-Z0-9_] {};
 
WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ -> skip ;


