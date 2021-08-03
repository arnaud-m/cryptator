// Generated from Cryptator.g4 by ANTLR 4.9.2

package cryptator.parser;

import cryptator.specs.ICryptaNode;
import cryptator.tree.CryptaNode;
import cryptator.tree.CryptaLeaf;


import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CryptatorParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, COMPARATEUR=9, 
		LETTER=10, WHITESPACE=11;
	public static final int
		RULE_program = 0, RULE_equation = 1, RULE_expression = 2, RULE_symbol = 3, 
		RULE_modORpow = 4, RULE_divORmul = 5, RULE_addORsub = 6, RULE_sub = 7;
	private static String[] makeRuleNames() {
		return new String[] {
			"program", "equation", "expression", "symbol", "modORpow", "divORmul", 
			"addORsub", "sub"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'%'", "'^'", "'/'", "'*'", "'+'", "'-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, "COMPARATEUR", 
			"LETTER", "WHITESPACE"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Cryptator.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CryptatorParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramContext extends ParserRuleContext {
		public EquationContext equation() {
			return getRuleContext(EquationContext.class,0);
		}
		public ProgramContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_program; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).enterProgram(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).exitProgram(this);
		}
	}

	public final ProgramContext program() throws RecognitionException {
		ProgramContext _localctx = new ProgramContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_program);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(16);
			equation();

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EquationContext extends ParserRuleContext {
		public ICryptaNode node;
		public ExpressionContext left;
		public Token COMPARATEUR;
		public ExpressionContext right;
		public TerminalNode COMPARATEUR() { return getToken(CryptatorParser.COMPARATEUR, 0); }
		public TerminalNode EOF() { return getToken(CryptatorParser.EOF, 0); }
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public EquationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_equation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).enterEquation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).exitEquation(this);
		}
	}

	public final EquationContext equation() throws RecognitionException {
		EquationContext _localctx = new EquationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_equation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(19);
			((EquationContext)_localctx).left = expression(0);
			setState(20);
			((EquationContext)_localctx).COMPARATEUR = match(COMPARATEUR);
			setState(21);
			((EquationContext)_localctx).right = expression(0);
			setState(22);
			match(EOF);
			((EquationContext)_localctx).node = new CryptaNode(((EquationContext)_localctx).COMPARATEUR.getText(), ((EquationContext)_localctx).left.node, ((EquationContext)_localctx).right.node);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ICryptaNode node;
		public ExpressionContext e1;
		public SymbolContext symbol;
		public ExpressionContext expression;
		public SubContext sub;
		public ModORpowContext modORpow;
		public ExpressionContext e2;
		public DivORmulContext divORmul;
		public AddORsubContext addORsub;
		public SymbolContext symbol() {
			return getRuleContext(SymbolContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public SubContext sub() {
			return getRuleContext(SubContext.class,0);
		}
		public ModORpowContext modORpow() {
			return getRuleContext(ModORpowContext.class,0);
		}
		public DivORmulContext divORmul() {
			return getRuleContext(DivORmulContext.class,0);
		}
		public AddORsubContext addORsub() {
			return getRuleContext(AddORsubContext.class,0);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).exitExpression(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				{
				setState(26);
				((ExpressionContext)_localctx).symbol = symbol();
				((ExpressionContext)_localctx).node = new CryptaLeaf((((ExpressionContext)_localctx).symbol!=null?_input.getText(((ExpressionContext)_localctx).symbol.start,((ExpressionContext)_localctx).symbol.stop):null));
				}
				break;
			case 2:
				{
				setState(29);
				match(T__0);
				setState(30);
				((ExpressionContext)_localctx).expression = expression(0);
				setState(31);
				match(T__1);
				((ExpressionContext)_localctx).node = ((ExpressionContext)_localctx).expression.node;
				}
				break;
			case 3:
				{
				setState(34);
				((ExpressionContext)_localctx).sub = sub();
				setState(35);
				((ExpressionContext)_localctx).expression = expression(3);
				((ExpressionContext)_localctx).node = new CryptaNode((((ExpressionContext)_localctx).sub!=null?_input.getText(((ExpressionContext)_localctx).sub.start,((ExpressionContext)_localctx).sub.stop):null), new CryptaLeaf("0"), ((ExpressionContext)_localctx).expression.node);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(57);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(55);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(40);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(41);
						((ExpressionContext)_localctx).modORpow = modORpow();
						setState(42);
						((ExpressionContext)_localctx).e2 = ((ExpressionContext)_localctx).expression = expression(5);
						((ExpressionContext)_localctx).node = new CryptaNode((((ExpressionContext)_localctx).modORpow!=null?_input.getText(((ExpressionContext)_localctx).modORpow.start,((ExpressionContext)_localctx).modORpow.stop):null), ((ExpressionContext)_localctx).e1.node, ((ExpressionContext)_localctx).e2.node);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(45);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(46);
						((ExpressionContext)_localctx).divORmul = divORmul();
						setState(47);
						((ExpressionContext)_localctx).e2 = ((ExpressionContext)_localctx).expression = expression(3);
						((ExpressionContext)_localctx).node = new CryptaNode((((ExpressionContext)_localctx).divORmul!=null?_input.getText(((ExpressionContext)_localctx).divORmul.start,((ExpressionContext)_localctx).divORmul.stop):null), ((ExpressionContext)_localctx).e1.node, ((ExpressionContext)_localctx).e2.node);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						_localctx.e1 = _prevctx;
						_localctx.e1 = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(50);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(51);
						((ExpressionContext)_localctx).addORsub = addORsub();
						setState(52);
						((ExpressionContext)_localctx).e2 = ((ExpressionContext)_localctx).expression = expression(2);
						((ExpressionContext)_localctx).node = new CryptaNode((((ExpressionContext)_localctx).addORsub!=null?_input.getText(((ExpressionContext)_localctx).addORsub.start,((ExpressionContext)_localctx).addORsub.stop):null), ((ExpressionContext)_localctx).e1.node, ((ExpressionContext)_localctx).e2.node);
						}
						break;
					}
					} 
				}
				setState(59);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class SymbolContext extends ParserRuleContext {
		public int n = 0;
		public List<TerminalNode> LETTER() { return getTokens(CryptatorParser.LETTER); }
		public TerminalNode LETTER(int i) {
			return getToken(CryptatorParser.LETTER, i);
		}
		public SymbolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_symbol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).enterSymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).exitSymbol(this);
		}
	}

	public final SymbolContext symbol() throws RecognitionException {
		SymbolContext _localctx = new SymbolContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_symbol);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(63); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(60);
					if (!(_localctx.n <= 8)) throw new FailedPredicateException(this, "$n <= 8");
					setState(61);
					match(LETTER);
					_localctx.n++;
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(65); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ModORpowContext extends ParserRuleContext {
		public ModORpowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_modORpow; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).enterModORpow(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).exitModORpow(this);
		}
	}

	public final ModORpowContext modORpow() throws RecognitionException {
		ModORpowContext _localctx = new ModORpowContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_modORpow);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			_la = _input.LA(1);
			if ( !(_la==T__2 || _la==T__3) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DivORmulContext extends ParserRuleContext {
		public DivORmulContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_divORmul; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).enterDivORmul(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).exitDivORmul(this);
		}
	}

	public final DivORmulContext divORmul() throws RecognitionException {
		DivORmulContext _localctx = new DivORmulContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_divORmul);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			_la = _input.LA(1);
			if ( !(_la==T__4 || _la==T__5) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AddORsubContext extends ParserRuleContext {
		public SubContext sub() {
			return getRuleContext(SubContext.class,0);
		}
		public AddORsubContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_addORsub; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).enterAddORsub(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).exitAddORsub(this);
		}
	}

	public final AddORsubContext addORsub() throws RecognitionException {
		AddORsubContext _localctx = new AddORsubContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_addORsub);
		try {
			setState(73);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(71);
				match(T__6);
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(72);
				sub();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SubContext extends ParserRuleContext {
		public SubContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sub; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).enterSub(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof CryptatorListener ) ((CryptatorListener)listener).exitSub(this);
		}
	}

	public final SubContext sub() throws RecognitionException {
		SubContext _localctx = new SubContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_sub);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			match(T__7);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		case 3:
			return symbol_sempred((SymbolContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 2);
		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean symbol_sempred(SymbolContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return _localctx.n <= 8;
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\rP\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\3\2\3\2\3\2\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5"+
		"\4)\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\7"+
		"\4:\n\4\f\4\16\4=\13\4\3\5\3\5\3\5\6\5B\n\5\r\5\16\5C\3\6\3\6\3\7\3\7"+
		"\3\b\3\b\5\bL\n\b\3\t\3\t\3\t\2\3\6\n\2\4\6\b\n\f\16\20\2\4\3\2\5\6\3"+
		"\2\7\b\2N\2\22\3\2\2\2\4\25\3\2\2\2\6(\3\2\2\2\bA\3\2\2\2\nE\3\2\2\2\f"+
		"G\3\2\2\2\16K\3\2\2\2\20M\3\2\2\2\22\23\5\4\3\2\23\24\b\2\1\2\24\3\3\2"+
		"\2\2\25\26\5\6\4\2\26\27\7\13\2\2\27\30\5\6\4\2\30\31\7\2\2\3\31\32\b"+
		"\3\1\2\32\5\3\2\2\2\33\34\b\4\1\2\34\35\5\b\5\2\35\36\b\4\1\2\36)\3\2"+
		"\2\2\37 \7\3\2\2 !\5\6\4\2!\"\7\4\2\2\"#\b\4\1\2#)\3\2\2\2$%\5\20\t\2"+
		"%&\5\6\4\5&\'\b\4\1\2\')\3\2\2\2(\33\3\2\2\2(\37\3\2\2\2($\3\2\2\2);\3"+
		"\2\2\2*+\f\6\2\2+,\5\n\6\2,-\5\6\4\7-.\b\4\1\2.:\3\2\2\2/\60\f\4\2\2\60"+
		"\61\5\f\7\2\61\62\5\6\4\5\62\63\b\4\1\2\63:\3\2\2\2\64\65\f\3\2\2\65\66"+
		"\5\16\b\2\66\67\5\6\4\4\678\b\4\1\28:\3\2\2\29*\3\2\2\29/\3\2\2\29\64"+
		"\3\2\2\2:=\3\2\2\2;9\3\2\2\2;<\3\2\2\2<\7\3\2\2\2=;\3\2\2\2>?\6\5\5\3"+
		"?@\7\f\2\2@B\b\5\1\2A>\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\t\3\2\2"+
		"\2EF\t\2\2\2F\13\3\2\2\2GH\t\3\2\2H\r\3\2\2\2IL\7\t\2\2JL\5\20\t\2KI\3"+
		"\2\2\2KJ\3\2\2\2L\17\3\2\2\2MN\7\n\2\2N\21\3\2\2\2\7(9;CK";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}