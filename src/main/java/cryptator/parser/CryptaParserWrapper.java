/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2022, Université Côte d'Azur. All rights reserved.
 *
 * Licensed under the BSD 3-clause license.
 * See LICENSE file in the project root for full license information.
 */
package cryptator.parser;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import cryptator.specs.ICryptaNode;
import cryptator.specs.ICryptaParser;

public class CryptaParserWrapper implements ICryptaParser {

	@Override
	public ICryptaNode parse(final String cryptarithm) throws CryptaParserException {
        final CharStream input = CharStreams.fromString(cryptarithm);
        final CryptatorLexer lexer = new CryptatorLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final CryptatorParser parser = new CryptatorParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);
        cryptator.parser.CryptatorParser.ProgramContext ctx = parser.program();
        return ctx.equation().node;  
    }

}
