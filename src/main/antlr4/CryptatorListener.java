// Generated from Cryptator.g4 by ANTLR 4.9.2

package cryptator.parser;

import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaNode;
import cryptator.tree.CryptaLeaf;


import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link CryptatorParser}.
 */
public interface CryptatorListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link CryptatorParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(CryptatorParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link CryptatorParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(CryptatorParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link CryptatorParser#equation}.
	 * @param ctx the parse tree
	 */
	void enterEquation(CryptatorParser.EquationContext ctx);
	/**
	 * Exit a parse tree produced by {@link CryptatorParser#equation}.
	 * @param ctx the parse tree
	 */
	void exitEquation(CryptatorParser.EquationContext ctx);
	/**
	 * Enter a parse tree produced by {@link CryptatorParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(CryptatorParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link CryptatorParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(CryptatorParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link CryptatorParser#symbol}.
	 * @param ctx the parse tree
	 */
	void enterSymbol(CryptatorParser.SymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link CryptatorParser#symbol}.
	 * @param ctx the parse tree
	 */
	void exitSymbol(CryptatorParser.SymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link CryptatorParser#modORpow}.
	 * @param ctx the parse tree
	 */
	void enterModORpow(CryptatorParser.ModORpowContext ctx);
	/**
	 * Exit a parse tree produced by {@link CryptatorParser#modORpow}.
	 * @param ctx the parse tree
	 */
	void exitModORpow(CryptatorParser.ModORpowContext ctx);
	/**
	 * Enter a parse tree produced by {@link CryptatorParser#divORmul}.
	 * @param ctx the parse tree
	 */
	void enterDivORmul(CryptatorParser.DivORmulContext ctx);
	/**
	 * Exit a parse tree produced by {@link CryptatorParser#divORmul}.
	 * @param ctx the parse tree
	 */
	void exitDivORmul(CryptatorParser.DivORmulContext ctx);
	/**
	 * Enter a parse tree produced by {@link CryptatorParser#addORsub}.
	 * @param ctx the parse tree
	 */
	void enterAddORsub(CryptatorParser.AddORsubContext ctx);
	/**
	 * Exit a parse tree produced by {@link CryptatorParser#addORsub}.
	 * @param ctx the parse tree
	 */
	void exitAddORsub(CryptatorParser.AddORsubContext ctx);
	/**
	 * Enter a parse tree produced by {@link CryptatorParser#sub}.
	 * @param ctx the parse tree
	 */
	void enterSub(CryptatorParser.SubContext ctx);
	/**
	 * Exit a parse tree produced by {@link CryptatorParser#sub}.
	 * @param ctx the parse tree
	 */
	void exitSub(CryptatorParser.SubContext ctx);
}