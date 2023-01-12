//
// This file is part of cryptator, https://github.com/arnaud-m/cryptator
//
// Copyright (c) 2023, Université Côte d'Azur. All rights reserved.
//
// Licensed under the BSD 3-clause license.
// See LICENSE file in the project root for full license information.
//

grammar Cryptator;

@header{
package cryptator.parser;

import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaNode;
import cryptator.tree.CryptaLeaf;
import cryptator.tree.CryptaConstant;

}


// Parser Rules

program : equations EOF{}; //additional token to simplify the passage in parameter

equations returns [ICryptaNode node]  //create a node of conjuncts
        : equation (AND*) {$node=$equation.node;}
        |  e1=equation (AND+) e2=equations (AND*) {$node=new CryptaNode("&&", $e1.node, $e2.node);};

equation returns [ICryptaNode node]  //create a node of the tree corresponding to an equation and return this node
        : '(' equation ')' {$node=$equation.node;}
        | left=expression COMPARATOR right=expression {$node=new CryptaNode($COMPARATOR.getText(), $left.node, $right.node);};
                 

expression returns [ICryptaNode node] //create recursively the tree of expressions with priority and return the root of the tree
            : word {$node=new CryptaLeaf($word.text);} //create a node of the tree corresponding to a leaf and return this node
            | '\'' number '\'' {$node=new CryptaConstant($number.text);}
            | '"' number '"' {$node=new CryptaConstant($number.text);}
            | '(' expression ')' {$node=$expression.node;}
            | e1=expression modORpow e2=expression {$node=new CryptaNode($modORpow.text, $e1.node, $e2.node);} //create a node of the tree corresponding to an operation and return this node
            | sub expression {$node=new CryptaNode($sub.text, new CryptaConstant("0"), $expression.node);}
            | e1=expression divORmul e2=expression {$node=new CryptaNode($divORmul.text, $e1.node, $e2.node);}
            | e1=expression addORsub e2=expression {$node=new CryptaNode($addORsub.text, $e1.node, $e2.node);};

word :  //additional token to simplify the passage in parameter
    (SYMBOL|DIGIT)+;

number : (DIGIT)+;
    
modORpow : '%' | '^';

divORmul : '/' | '//' | '*';

addORsub : '+' | sub;

sub : '-';

// Lexer Rules

COMPARATOR : '=' | '!=' | '<' | '>' | '<=' | '>=';

SYMBOL : [a-zA-Z\u0080-\uFFFF] {};

DIGIT : [0-9] {};

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ -> skip ;

AND : ';' | '&&';