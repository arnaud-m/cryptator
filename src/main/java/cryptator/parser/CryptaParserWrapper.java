/**
 * This file is part of cryptator, https://github.com/arnaud-m/cryptator
 *
 * Copyright (c) 2021, Université Côte d'Azur. All rights reserved.
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
import org.antlr.v4.runtime.RecognitionException;

public class CryptaParserWrapper implements ICryptaParser {

	public CryptaParserWrapper() {}

	@Override
	public ICryptaNode parse(String cryptarithm) throws CryptaParserException {
        final CharStream input = CharStreams.fromString(cryptarithm);
        cryptator.parser.CryptatorLexer lexer = new cryptator.parser.CryptatorLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(ThrowingErrorListener.INSTANCE);

        CommonTokenStream tokens = new CommonTokenStream(lexer);
        cryptator.parser.CryptatorParser parser = new cryptator.parser.CryptatorParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(ThrowingErrorListener.INSTANCE);
        try {
            cryptator.parser.CryptatorParser.ProgramContext ctx = parser.program();
            return ctx.equation().node;
        } catch (RecognitionException e) {
            // Catching here -> exception not handled depending on the context
            // TODO @Margaux throw an exception with useful attributes for helping the user.
            System.err.println(e.getMessage()); // FOR DEBUG
            e.printStackTrace();
            //TODO catch this exception
            throw new CryptaParserException();
        }

    }

}
