import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


public class Main {
        public static void main(String[] args) throws Exception {
            CharStream input = CharStreams.fromString("(p+d)*n=z");
            ParserLexer lexer = new ParserLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            ParserParser parser = new ParserParser(tokens);
            ParseTree tree = parser.program();
        }
}
