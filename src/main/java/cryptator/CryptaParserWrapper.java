package cryptator;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import cryptator.parser.CryptatorLexer;
import cryptator.parser.CryptatorParser;
import cryptator.parser.CryptatorParser.ProgramContext;
import cryptator.specs.ICryptaTree;
import cryptator.specs.ICryptarithmParser;

public class CryptaParserWrapper implements ICryptarithmParser {

	public CryptaParserWrapper() {}

	@Override
	public ICryptaTree parse(String cryptarithm) {
		final CharStream input = CharStreams.fromString(cryptarithm);
        CryptatorLexer lexer = new CryptatorLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CryptatorParser parser = new CryptatorParser(tokens);
        ProgramContext ctx = parser.program();
        //System.out.println(ctx.equation.node);
		return null;
	}
	
	

}
