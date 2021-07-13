package cryptator.parser;

// Generated from Parser.g4 by ANTLR 4.9.2

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ParserParser}.
 */
public interface ParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ParserParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(ParserParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(ParserParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserParser#equation}.
	 * @param ctx the parse tree
	 */
	void enterEquation(ParserParser.EquationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserParser#equation}.
	 * @param ctx the parse tree
	 */
	void exitEquation(ParserParser.EquationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(ParserParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(ParserParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserParser#symbol}.
	 * @param ctx the parse tree
	 */
	void enterSymbol(ParserParser.SymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserParser#symbol}.
	 * @param ctx the parse tree
	 */
	void exitSymbol(ParserParser.SymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserParser#modORpow}.
	 * @param ctx the parse tree
	 */
	void enterModORpow(ParserParser.ModORpowContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserParser#modORpow}.
	 * @param ctx the parse tree
	 */
	void exitModORpow(ParserParser.ModORpowContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserParser#divORmul}.
	 * @param ctx the parse tree
	 */
	void enterDivORmul(ParserParser.DivORmulContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserParser#divORmul}.
	 * @param ctx the parse tree
	 */
	void exitDivORmul(ParserParser.DivORmulContext ctx);
	/**
	 * Enter a parse tree produced by {@link ParserParser#addORsub}.
	 * @param ctx the parse tree
	 */
	void enterAddORsub(ParserParser.AddORsubContext ctx);
	/**
	 * Exit a parse tree produced by {@link ParserParser#addORsub}.
	 * @param ctx the parse tree
	 */
	void exitAddORsub(ParserParser.AddORsubContext ctx);
}