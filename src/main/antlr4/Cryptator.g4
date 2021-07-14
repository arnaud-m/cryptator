grammar Cryptator;

@header{
package cryptator.parser;
import cryptator.structure.*;
}

// Parser Rules

program : equation {System.out.println($equation.node.toString());}; //additional token to simplify the passage in parameter

equation returns [NodeTree node]: //create a node of the tree corresponding to an equation and return this node
                left=expression COMPARATEUR right=expression {$node=new Equation($COMPARATEUR.getText(), $left.node, $right.node);};


expression returns [NodeTree node]: //create recursively the tree of expressions with priority and return the root of the tree
            symbol {$node=new Feuille($symbol.text);} //create a node of the tree corresponding to a leaf and return this node
            | '(' expression ')' {$node=$expression.node;}
            | e1=expression modORpow e2=expression {$node=new Operation($modORpow.text, $e1.node, $e2.node);} //create a node of the tree corresponding to an operation and return this node
            | e1=expression divORmul e2=expression {$node=new Operation($divORmul.text, $e1.node, $e2.node);}
            | e1=expression addORsub e2=expression {$node=new Operation($addORsub.text, $e1.node, $e2.node);};

symbol : LETTER+ {}; //additional token to simplify the passage in parameter

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

