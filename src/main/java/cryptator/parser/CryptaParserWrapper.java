/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.parser;

import cryptator.parser.CryptatorLexer;
import cryptator.parser.CryptatorParser;
import cryptator.parser.CryptatorParser.ProgramContext;

import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class CryptaParserWrapper implements ICryptaParser {

	public CryptaParserWrapper() {}

	@Override
	public ICryptaNode parse(String cryptarithm) throws CryptaParserException {
        final CharStream input = CharStreams.fromString(cryptarithm);
        CryptatorLexer lexer = new CryptatorLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CryptatorParser parser = new CryptatorParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);
        try {
            ProgramContext ctx = parser.program();
            return ctx.equation().node;
        }
        catch(Exception e){
        	// Catching here -> exception not handled depending on the context
            System.err.println(e.getMessage());
            throw new CryptaParserException();
        }
	}

}
