grammar Parser;

@header{
import structure.NodeTree;
import structure.Feuille;
import structure.Operation;
import structure.Equation;
}

// Parser Rules

program : equation {$equation.node.visualise();};

equation returns [NodeTree node]:
                left=expression COMPARATEUR right=expression {$node=new Equation($COMPARATEUR.getText(), $left.node, $right.node);};


expression returns [NodeTree node]:
            symbol {$node=new Feuille($symbol.text);}
            | '(' expression ')' {$node=$expression.node;}
            | e1=expression modORpow e2=expression {$node=new Operation($modORpow.text, $e1.node, $e2.node);}
            | e1=expression divORmul e2=expression {$node=new Operation($divORmul.text, $e1.node, $e2.node);}
            | e1=expression addORsub e2=expression {$node=new Operation($addORsub.text, $e1.node, $e2.node);};

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

