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

program : equations EOF{}; // additional token to simplify the passage in parameter

equations returns [ICryptaNode node]  // create a list of equations
    : equation (AND*) {$node=$equation.node;}
    |  e1=equation (AND | AND_INFIX)+ e2=equations {$node=new CryptaNode("&&", $e1.node, $e2.node);};

equation returns [ICryptaNode node]  // create an equation
    : '(' equation ')' {$node=$equation.node;}
    | left=expression COMPARATOR right=expression {$node=new CryptaNode($COMPARATOR.text, $left.node, $right.node);};

expression returns [ICryptaNode node] // create the tree of expressions
    : word {$node=new CryptaLeaf($word.text);}
    | '\'' NUMBER '\'' {$node=new CryptaConstant($NUMBER.text);}
    | '"' NUMBER '"' {$node=new CryptaConstant($NUMBER.text);}
    | '(' expression ')' {$node=$expression.node;}
    | e1=expression MOD_OR_POW e2=expression {$node=new CryptaNode($MOD_OR_POW.text, $e1.node, $e2.node);}
    | SUB expression {$node=new CryptaNode("-", new CryptaConstant("0"), $expression.node);}
    | e1=expression DIV_OR_MUL e2=expression {$node=new CryptaNode($DIV_OR_MUL.text, $e1.node, $e2.node);}
    | e1=expression op=(ADD | SUB) e2=expression {$node=new CryptaNode($op.text, $e1.node, $e2.node);};

word : (SYMBOL|NUMBER)+;

// Lexer Rules

// OPERATORS
MOD_OR_POW : '%' | '^';
DIV_OR_MUL : '/' | '//' | '*';
SUB: '-';
ADD: '+';

// COMPARATORS
COMPARATOR : '=' | '!=' | '<' | '>' | '<=' | '>=';

// SYMBOLS
SYMBOL : [a-zA-Z\u0080-\uFFFF] {};
NUMBER : [0-9]+ {};

// CONJUNCTIONS
AND : ';';
AND_INFIX : '&&';

// IGNORE
WHITESPACE : ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ -> skip ;

// ERROR
ERROR : (SYMBOL|NUMBER)+ WHITESPACE (SYMBOL|NUMBER)+;
