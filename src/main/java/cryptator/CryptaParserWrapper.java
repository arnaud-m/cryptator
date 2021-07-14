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

import cryptator.parser.CryptatorLexer;
import cryptator.parser.CryptatorParser;
import cryptator.parser.CryptatorParser.ProgramContext;
import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaParser;

public class CryptaParserWrapper implements ICryptaParser {

	public CryptaParserWrapper() {}

	@Override
	public ICryptaNode parse(String cryptarithm) {
		final CharStream input = CharStreams.fromString(cryptarithm);
        CryptatorLexer lexer = new CryptatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CryptatorParser parser = new CryptatorParser(tokens);
        ProgramContext ctx = parser.program();
        //System.out.println(ctx.equation.node);
		return null;
	}
	
	

}
