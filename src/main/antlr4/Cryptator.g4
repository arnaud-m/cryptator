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

program : WHITESPACE* equations WHITESPACE* EOF{}; //additional token to simplify the passage in parameter

equations returns [ICryptaNode node]  //create a node of conjuncts
        : equation and* {$node=$equation.node;}
        |  e1=equation and+ e2=equations and* {$node=new CryptaNode("&&", $e1.node, $e2.node);};

equation returns [ICryptaNode node]  //create a node of the tree corresponding to an equation and return this node
        : openPar equation closePar {$node=$equation.node;}
        | left=expression comparator right=expression {$node=new CryptaNode($comparator.n, $left.node, $right.node);};
                 

expression returns [ICryptaNode node] //create recursively the tree of expressions with priority and return the root of the tree
            : word {$node=new CryptaLeaf($word.s);} //create a node of the tree corresponding to a leaf and return this node
            | tick number tick {$node=new CryptaConstant($number.n);}
            | doubleTick number doubleTick {$node=new CryptaConstant($number.n);}
            | openPar expression closePar {$node=$expression.node;}
            | e1=expression modORpow e2=expression {$node=new CryptaNode($modORpow.n, $e1.node, $e2.node);} //create a node of the tree corresponding to an operation and return this node
            | sub expression {$node=new CryptaNode($sub.n, new CryptaConstant("0"), $expression.node);}
            | e1=expression divORmul e2=expression {$node=new CryptaNode($divORmul.n, $e1.node, $e2.node);}
            | e1=expression addORsub e2=expression {$node=new CryptaNode($addORsub.n, $e1.node, $e2.node);};

//additional token to simplify the passage in parameter
word returns [String s]: wordText WHITESPACE* {$s=$wordText.text;};
and returns [String s]: AND WHITESPACE* {$s=$AND.text;};
number returns [String n]: numberText WHITESPACE* {$n=$numberText.text;};

tick returns [String n]: '\'' WHITESPACE* {$n="'";};
doubleTick returns [String n]: '"' WHITESPACE* {$n="\"";};

openPar returns [String n]: '(' WHITESPACE* {$n="(";};
closePar returns [String n]: ')' WHITESPACE* {$n="(";};

comparator returns [String n]: COMPARATOR WHITESPACE* {$n=$COMPARATOR.getText();};

modORpow returns [String n]: MOD_OR_POW WHITESPACE* {$n=$MOD_OR_POW.getText();};
divORmul returns [String n]: DIV_OR_MUL WHITESPACE* {$n=$DIV_OR_MUL.getText();};
addORsub returns [String n]: e=(ADD | SUB) WHITESPACE* {$n=$e.getText();};
sub returns [String n]: SUB WHITESPACE* {$n=$SUB.getText();};

wordText: (SYMBOL|DIGIT)+;

numberText: (DIGIT)+;

// Lexer Rules

MOD_OR_POW : '%' | '^';
DIV_OR_MUL : '/' | '//' | '*';
SUB : '-';
ADD : '+';

COMPARATOR : '=' | '!=' | '<' | '>' | '<=' | '>=';

SYMBOL : [a-zA-Z\u0080-\uFFFF] {};

DIGIT : [0-9] {};

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' ) ;

AND : ';' | '&&';