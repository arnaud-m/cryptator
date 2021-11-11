grammar Cryptator;

@header{
package cryptator.parser;

import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaNode;
import cryptator.tree.CryptaLeaf;

}


// Parser Rules

program : equation EOF{}; //additional token to simplify the passage in parameter

equation returns [ICryptaNode node]: //create a node of the tree corresponding to an equation and return this node
                 '(' equation ')' {$node=$equation.node;}
                 | left=expression COMPARATOR right=expression {$node=new CryptaNode($COMPARATOR.getText(), $left.node, $right.node);};
                 

expression returns [ICryptaNode node]: //create recursively the tree of expressions with priority and return the root of the tree
            word {$node=new CryptaLeaf($word.text);} //create a node of the tree corresponding to a leaf and return this node
            | '(' expression ')' {$node=$expression.node;}
            | e1=expression modORpow e2=expression {$node=new CryptaNode($modORpow.text, $e1.node, $e2.node);} //create a node of the tree corresponding to an operation and return this node
            | sub expression {$node=new CryptaNode($sub.text, new CryptaLeaf(), $expression.node);}
            | e1=expression divORmul e2=expression {$node=new CryptaNode($divORmul.text, $e1.node, $e2.node);}
            | e1=expression addORsub e2=expression {$node=new CryptaNode($addORsub.text, $e1.node, $e2.node);};

word :  //additional token to simplify the passage in parameter
    (LETTER)+; 
    
modORpow : '%' | '^';

divORmul : '/' | '//' | '*';

addORsub : '+' | sub;

sub : '-';

// Lexer Rules

COMPARATOR : '=' | '!=' | '<' | '>' | '<=' | '>=';

LETTER : [a-zA-Z0-9_] {};

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ -> skip ;

