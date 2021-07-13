/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import cryptator.parser.CryptatorLexer;
import cryptator.parser.CryptatorParser;

public class Main {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromString("p+di*n=z");
       CryptatorLexer lexer = new CryptatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CryptatorParser parser = new CryptatorParser(tokens);
        ParseTree tree = parser.program();
        //System.out.println(tree.getPayload());
    }
}
