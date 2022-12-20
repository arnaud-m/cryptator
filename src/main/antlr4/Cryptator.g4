//
// This file is part of cryptator, https://github.com/arnaud-m/cryptator
//
// Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
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
        : equation andI* {$node=$equation.node;}
        |  e1=equation andI+ e2=equations andI* {$node=new CryptaNode("&&", $e1.node, $e2.node);};

equation returns [ICryptaNode node]  //create a node of the tree corresponding to an equation and return this node
        : '(' equation ')' {$node=$equation.node;}
        | left=expression COMPARATOR WHITESPACE* right=expression {$node=new CryptaNode($COMPARATOR.getText(), $left.node, $right.node);};
                 

expression returns [ICryptaNode node] //create recursively the tree of expressions with priority and return the root of the tree
            : wordI {$node=new CryptaLeaf($wordI.s);} //create a node of the tree corresponding to a leaf and return this node
            | '\'' numberI '\'' WHITESPACE* {$node=new CryptaConstant($numberI.n);}
            | '"' numberI '"' WHITESPACE* {$node=new CryptaConstant($numberI.n);}
            | '(' WHITESPACE* expression ')' WHITESPACE* {$node=$expression.node;}
            | e1=expression modORpow WHITESPACE* e2=expression {$node=new CryptaNode($modORpow.text, $e1.node, $e2.node);} //create a node of the tree corresponding to an operation and return this node
            | sub WHITESPACE* expression {$node=new CryptaNode($sub.text, new CryptaConstant("0"), $expression.node);}
            | e1=expression divORmul WHITESPACE* e2=expression {$node=new CryptaNode($divORmul.text, $e1.node, $e2.node);}
            | e1=expression addORsub WHITESPACE* e2=expression {$node=new CryptaNode($addORsub.text, $e1.node, $e2.node);};

//additional token to simplify the passage in parameter
wordI returns [String s]: word WHITESPACE* {$s=$word.text;};
andI returns [String s]: AND WHITESPACE* {$s=$AND.text;};
numberI returns [String n]: number WHITESPACE* {$n=$number.text;};

word: (SYMBOL|DIGIT)+;

number: (DIGIT)+;

modORpow : '%' | '^';

divORmul : '/' | '//' | '*';

addORsub : '+' | sub;

sub : '-';


// Lexer Rules

COMPARATOR : '=' | '!=' | '<' | '>' | '<=' | '>=';

SYMBOL : [a-zA-Z\u0080-\uFFFF] {};

DIGIT : [0-9] {};

WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' ) ;

AND : ';' | '&&';