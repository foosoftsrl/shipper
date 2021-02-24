// Generated from C:\Users\luca\devel\shipper\shipper-core/antlr/Config.g4 by ANTLR 4.9.1

package com.logstash;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ConfigParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, INPUT=2, FILTER=3, OUTPUT=4, COMMAT=5, LBRACE=6, RBRACE=7, LPAREN=8, 
		RPAREN=9, LBRACKET=10, RBRACKET=11, QMARK=12, HASHROCKET=13, GT=14, GE=15, 
		LT=16, LE=17, EQ=18, NEQ=19, BANG=20, COMA=21, IN=22, NOT=23, MATCH=24, 
		NOT_MATCH=25, AND=26, OR=27, XOR=28, NAND=29, IF=30, ELSE=31, STRING=32, 
		REGEX=33, DECIMAL=34, IDENTIFIER=35, WS=36, COMMENT=37;
	public static final int
		RULE_config = 0, RULE_stage_declaration = 1, RULE_stage_definition = 2, 
		RULE_exit_statement = 3, RULE_plugin_declaration = 4, RULE_plugin_definition = 5, 
		RULE_plugin_attribute = 6, RULE_plugin_attribute_value = 7, RULE_stage_condition = 8, 
		RULE_logical_expression = 9, RULE_negative_expression = 10, RULE_compare_expression = 11, 
		RULE_in_expression = 12, RULE_match_expression = 13, RULE_rvalue = 14, 
		RULE_fieldref = 15, RULE_fieldref_element = 16, RULE_array = 17, RULE_array_element = 18, 
		RULE_hash = 19, RULE_hash_element = 20;
	private static String[] makeRuleNames() {
		return new String[] {
			"config", "stage_declaration", "stage_definition", "exit_statement", 
			"plugin_declaration", "plugin_definition", "plugin_attribute", "plugin_attribute_value", 
			"stage_condition", "logical_expression", "negative_expression", "compare_expression", 
			"in_expression", "match_expression", "rvalue", "fieldref", "fieldref_element", 
			"array", "array_element", "hash", "hash_element"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'exit'", "'input'", "'filter'", "'output'", "'@'", "'{'", "'}'", 
			"'('", "')'", "'['", "']'", "'?'", "'=>'", "'>'", "'>='", "'<'", "'<='", 
			"'=='", "'!='", "'!'", "','", "'in'", "'not'", "'=~'", "'!~'", "'and'", 
			"'or'", "'xor'", "'nand'", "'if'", "'else'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "INPUT", "FILTER", "OUTPUT", "COMMAT", "LBRACE", "RBRACE", 
			"LPAREN", "RPAREN", "LBRACKET", "RBRACKET", "QMARK", "HASHROCKET", "GT", 
			"GE", "LT", "LE", "EQ", "NEQ", "BANG", "COMA", "IN", "NOT", "MATCH", 
			"NOT_MATCH", "AND", "OR", "XOR", "NAND", "IF", "ELSE", "STRING", "REGEX", 
			"DECIMAL", "IDENTIFIER", "WS", "COMMENT"
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
	public String getGrammarFileName() { return "Config.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public ConfigParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ConfigContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(ConfigParser.EOF, 0); }
		public List<Stage_declarationContext> stage_declaration() {
			return getRuleContexts(Stage_declarationContext.class);
		}
		public Stage_declarationContext stage_declaration(int i) {
			return getRuleContext(Stage_declarationContext.class,i);
		}
		public ConfigContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_config; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterConfig(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitConfig(this);
		}
	}

	public final ConfigContext config() throws RecognitionException {
		ConfigContext _localctx = new ConfigContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_config);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(42);
				stage_declaration();
				}
				}
				setState(45); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INPUT) | (1L << FILTER) | (1L << OUTPUT))) != 0) );
			setState(47);
			match(EOF);
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

	public static class Stage_declarationContext extends ParserRuleContext {
		public Stage_definitionContext stage_definition() {
			return getRuleContext(Stage_definitionContext.class,0);
		}
		public TerminalNode INPUT() { return getToken(ConfigParser.INPUT, 0); }
		public TerminalNode FILTER() { return getToken(ConfigParser.FILTER, 0); }
		public TerminalNode OUTPUT() { return getToken(ConfigParser.OUTPUT, 0); }
		public Stage_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stage_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterStage_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitStage_declaration(this);
		}
	}

	public final Stage_declarationContext stage_declaration() throws RecognitionException {
		Stage_declarationContext _localctx = new Stage_declarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stage_declaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INPUT) | (1L << FILTER) | (1L << OUTPUT))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(50);
			stage_definition();
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

	public static class Stage_definitionContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ConfigParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(ConfigParser.RBRACE, 0); }
		public List<Plugin_declarationContext> plugin_declaration() {
			return getRuleContexts(Plugin_declarationContext.class);
		}
		public Plugin_declarationContext plugin_declaration(int i) {
			return getRuleContext(Plugin_declarationContext.class,i);
		}
		public List<Stage_conditionContext> stage_condition() {
			return getRuleContexts(Stage_conditionContext.class);
		}
		public Stage_conditionContext stage_condition(int i) {
			return getRuleContext(Stage_conditionContext.class,i);
		}
		public List<Exit_statementContext> exit_statement() {
			return getRuleContexts(Exit_statementContext.class);
		}
		public Exit_statementContext exit_statement(int i) {
			return getRuleContext(Exit_statementContext.class,i);
		}
		public Stage_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stage_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterStage_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitStage_definition(this);
		}
	}

	public final Stage_definitionContext stage_definition() throws RecognitionException {
		Stage_definitionContext _localctx = new Stage_definitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_stage_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52);
			match(LBRACE);
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << IF) | (1L << IDENTIFIER))) != 0)) {
				{
				setState(56);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IDENTIFIER:
					{
					setState(53);
					plugin_declaration();
					}
					break;
				case IF:
					{
					setState(54);
					stage_condition();
					}
					break;
				case T__0:
					{
					setState(55);
					exit_statement();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(60);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(61);
			match(RBRACE);
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

	public static class Exit_statementContext extends ParserRuleContext {
		public Exit_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exit_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterExit_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitExit_statement(this);
		}
	}

	public final Exit_statementContext exit_statement() throws RecognitionException {
		Exit_statementContext _localctx = new Exit_statementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_exit_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(T__0);
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

	public static class Plugin_declarationContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(ConfigParser.IDENTIFIER, 0); }
		public Plugin_definitionContext plugin_definition() {
			return getRuleContext(Plugin_definitionContext.class,0);
		}
		public Plugin_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plugin_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterPlugin_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitPlugin_declaration(this);
		}
	}

	public final Plugin_declarationContext plugin_declaration() throws RecognitionException {
		Plugin_declarationContext _localctx = new Plugin_declarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_plugin_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			match(IDENTIFIER);
			setState(66);
			plugin_definition();
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

	public static class Plugin_definitionContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ConfigParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(ConfigParser.RBRACE, 0); }
		public List<Plugin_attributeContext> plugin_attribute() {
			return getRuleContexts(Plugin_attributeContext.class);
		}
		public Plugin_attributeContext plugin_attribute(int i) {
			return getRuleContext(Plugin_attributeContext.class,i);
		}
		public Plugin_definitionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plugin_definition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterPlugin_definition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitPlugin_definition(this);
		}
	}

	public final Plugin_definitionContext plugin_definition() throws RecognitionException {
		Plugin_definitionContext _localctx = new Plugin_definitionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_plugin_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(68);
			match(LBRACE);
			setState(72);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(69);
				plugin_attribute();
				}
				}
				setState(74);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(75);
			match(RBRACE);
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

	public static class Plugin_attributeContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(ConfigParser.IDENTIFIER, 0); }
		public TerminalNode HASHROCKET() { return getToken(ConfigParser.HASHROCKET, 0); }
		public Plugin_attribute_valueContext plugin_attribute_value() {
			return getRuleContext(Plugin_attribute_valueContext.class,0);
		}
		public Plugin_attributeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plugin_attribute; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterPlugin_attribute(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitPlugin_attribute(this);
		}
	}

	public final Plugin_attributeContext plugin_attribute() throws RecognitionException {
		Plugin_attributeContext _localctx = new Plugin_attributeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_plugin_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			match(IDENTIFIER);
			setState(78);
			match(HASHROCKET);
			setState(79);
			plugin_attribute_value();
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

	public static class Plugin_attribute_valueContext extends ParserRuleContext {
		public Plugin_declarationContext plugin_declaration() {
			return getRuleContext(Plugin_declarationContext.class,0);
		}
		public TerminalNode IDENTIFIER() { return getToken(ConfigParser.IDENTIFIER, 0); }
		public TerminalNode STRING() { return getToken(ConfigParser.STRING, 0); }
		public TerminalNode DECIMAL() { return getToken(ConfigParser.DECIMAL, 0); }
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public HashContext hash() {
			return getRuleContext(HashContext.class,0);
		}
		public Plugin_attribute_valueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_plugin_attribute_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterPlugin_attribute_value(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitPlugin_attribute_value(this);
		}
	}

	public final Plugin_attribute_valueContext plugin_attribute_value() throws RecognitionException {
		Plugin_attribute_valueContext _localctx = new Plugin_attribute_valueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_plugin_attribute_value);
		try {
			setState(87);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(81);
				plugin_declaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(82);
				match(IDENTIFIER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(83);
				match(STRING);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(84);
				match(DECIMAL);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(85);
				array();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(86);
				hash();
				}
				break;
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

	public static class Stage_conditionContext extends ParserRuleContext {
		public List<TerminalNode> IF() { return getTokens(ConfigParser.IF); }
		public TerminalNode IF(int i) {
			return getToken(ConfigParser.IF, i);
		}
		public List<Logical_expressionContext> logical_expression() {
			return getRuleContexts(Logical_expressionContext.class);
		}
		public Logical_expressionContext logical_expression(int i) {
			return getRuleContext(Logical_expressionContext.class,i);
		}
		public List<Stage_definitionContext> stage_definition() {
			return getRuleContexts(Stage_definitionContext.class);
		}
		public Stage_definitionContext stage_definition(int i) {
			return getRuleContext(Stage_definitionContext.class,i);
		}
		public List<TerminalNode> ELSE() { return getTokens(ConfigParser.ELSE); }
		public TerminalNode ELSE(int i) {
			return getToken(ConfigParser.ELSE, i);
		}
		public Stage_conditionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stage_condition; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterStage_condition(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitStage_condition(this);
		}
	}

	public final Stage_conditionContext stage_condition() throws RecognitionException {
		Stage_conditionContext _localctx = new Stage_conditionContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_stage_condition);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			match(IF);
			setState(90);
			logical_expression(0);
			setState(91);
			stage_definition();
			setState(99);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(92);
					match(ELSE);
					setState(93);
					match(IF);
					setState(94);
					logical_expression(0);
					setState(95);
					stage_definition();
					}
					} 
				}
				setState(101);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(104);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(102);
				match(ELSE);
				setState(103);
				stage_definition();
				}
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

	public static class Logical_expressionContext extends ParserRuleContext {
		public Compare_expressionContext compare_expression() {
			return getRuleContext(Compare_expressionContext.class,0);
		}
		public In_expressionContext in_expression() {
			return getRuleContext(In_expressionContext.class,0);
		}
		public Match_expressionContext match_expression() {
			return getRuleContext(Match_expressionContext.class,0);
		}
		public Negative_expressionContext negative_expression() {
			return getRuleContext(Negative_expressionContext.class,0);
		}
		public TerminalNode LPAREN() { return getToken(ConfigParser.LPAREN, 0); }
		public List<Logical_expressionContext> logical_expression() {
			return getRuleContexts(Logical_expressionContext.class);
		}
		public Logical_expressionContext logical_expression(int i) {
			return getRuleContext(Logical_expressionContext.class,i);
		}
		public TerminalNode RPAREN() { return getToken(ConfigParser.RPAREN, 0); }
		public RvalueContext rvalue() {
			return getRuleContext(RvalueContext.class,0);
		}
		public TerminalNode AND() { return getToken(ConfigParser.AND, 0); }
		public TerminalNode OR() { return getToken(ConfigParser.OR, 0); }
		public TerminalNode NAND() { return getToken(ConfigParser.NAND, 0); }
		public TerminalNode XOR() { return getToken(ConfigParser.XOR, 0); }
		public Logical_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_logical_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterLogical_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitLogical_expression(this);
		}
	}

	public final Logical_expressionContext logical_expression() throws RecognitionException {
		return logical_expression(0);
	}

	private Logical_expressionContext logical_expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Logical_expressionContext _localctx = new Logical_expressionContext(_ctx, _parentState);
		Logical_expressionContext _prevctx = _localctx;
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_logical_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(116);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(107);
				compare_expression();
				}
				break;
			case 2:
				{
				setState(108);
				in_expression();
				}
				break;
			case 3:
				{
				setState(109);
				match_expression();
				}
				break;
			case 4:
				{
				setState(110);
				negative_expression();
				}
				break;
			case 5:
				{
				setState(111);
				match(LPAREN);
				setState(112);
				logical_expression(0);
				setState(113);
				match(RPAREN);
				}
				break;
			case 6:
				{
				setState(115);
				rvalue();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(132);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(130);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new Logical_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(118);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(119);
						match(AND);
						setState(120);
						logical_expression(11);
						}
						break;
					case 2:
						{
						_localctx = new Logical_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(121);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(122);
						match(OR);
						setState(123);
						logical_expression(10);
						}
						break;
					case 3:
						{
						_localctx = new Logical_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(124);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(125);
						match(NAND);
						setState(126);
						logical_expression(9);
						}
						break;
					case 4:
						{
						_localctx = new Logical_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(127);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(128);
						match(XOR);
						setState(129);
						logical_expression(8);
						}
						break;
					}
					} 
				}
				setState(134);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
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

	public static class Negative_expressionContext extends ParserRuleContext {
		public TerminalNode BANG() { return getToken(ConfigParser.BANG, 0); }
		public Logical_expressionContext logical_expression() {
			return getRuleContext(Logical_expressionContext.class,0);
		}
		public Negative_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_negative_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterNegative_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitNegative_expression(this);
		}
	}

	public final Negative_expressionContext negative_expression() throws RecognitionException {
		Negative_expressionContext _localctx = new Negative_expressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_negative_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			match(BANG);
			setState(136);
			logical_expression(0);
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

	public static class Compare_expressionContext extends ParserRuleContext {
		public List<RvalueContext> rvalue() {
			return getRuleContexts(RvalueContext.class);
		}
		public RvalueContext rvalue(int i) {
			return getRuleContext(RvalueContext.class,i);
		}
		public TerminalNode GT() { return getToken(ConfigParser.GT, 0); }
		public TerminalNode GE() { return getToken(ConfigParser.GE, 0); }
		public TerminalNode LT() { return getToken(ConfigParser.LT, 0); }
		public TerminalNode LE() { return getToken(ConfigParser.LE, 0); }
		public TerminalNode EQ() { return getToken(ConfigParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(ConfigParser.NEQ, 0); }
		public Compare_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compare_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterCompare_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitCompare_expression(this);
		}
	}

	public final Compare_expressionContext compare_expression() throws RecognitionException {
		Compare_expressionContext _localctx = new Compare_expressionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_compare_expression);
		try {
			setState(162);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(138);
				rvalue();
				setState(139);
				match(GT);
				setState(140);
				rvalue();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(142);
				rvalue();
				setState(143);
				match(GE);
				setState(144);
				rvalue();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(146);
				rvalue();
				setState(147);
				match(LT);
				setState(148);
				rvalue();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(150);
				rvalue();
				setState(151);
				match(LE);
				setState(152);
				rvalue();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(154);
				rvalue();
				setState(155);
				match(EQ);
				setState(156);
				rvalue();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(158);
				rvalue();
				setState(159);
				match(NEQ);
				setState(160);
				rvalue();
				}
				break;
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

	public static class In_expressionContext extends ParserRuleContext {
		public List<RvalueContext> rvalue() {
			return getRuleContexts(RvalueContext.class);
		}
		public RvalueContext rvalue(int i) {
			return getRuleContext(RvalueContext.class,i);
		}
		public TerminalNode IN() { return getToken(ConfigParser.IN, 0); }
		public TerminalNode NOT() { return getToken(ConfigParser.NOT, 0); }
		public In_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_in_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterIn_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitIn_expression(this);
		}
	}

	public final In_expressionContext in_expression() throws RecognitionException {
		In_expressionContext _localctx = new In_expressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_in_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			rvalue();
			setState(166);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(165);
				match(NOT);
				}
			}

			setState(168);
			match(IN);
			setState(169);
			rvalue();
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

	public static class Match_expressionContext extends ParserRuleContext {
		public RvalueContext rvalue() {
			return getRuleContext(RvalueContext.class,0);
		}
		public TerminalNode MATCH() { return getToken(ConfigParser.MATCH, 0); }
		public TerminalNode NOT_MATCH() { return getToken(ConfigParser.NOT_MATCH, 0); }
		public TerminalNode STRING() { return getToken(ConfigParser.STRING, 0); }
		public TerminalNode REGEX() { return getToken(ConfigParser.REGEX, 0); }
		public Match_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_match_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterMatch_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitMatch_expression(this);
		}
	}

	public final Match_expressionContext match_expression() throws RecognitionException {
		Match_expressionContext _localctx = new Match_expressionContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_match_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(171);
			rvalue();
			setState(172);
			_la = _input.LA(1);
			if ( !(_la==MATCH || _la==NOT_MATCH) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(173);
			_la = _input.LA(1);
			if ( !(_la==STRING || _la==REGEX) ) {
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

	public static class RvalueContext extends ParserRuleContext {
		public TerminalNode STRING() { return getToken(ConfigParser.STRING, 0); }
		public TerminalNode DECIMAL() { return getToken(ConfigParser.DECIMAL, 0); }
		public FieldrefContext fieldref() {
			return getRuleContext(FieldrefContext.class,0);
		}
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public RvalueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rvalue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterRvalue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitRvalue(this);
		}
	}

	public final RvalueContext rvalue() throws RecognitionException {
		RvalueContext _localctx = new RvalueContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_rvalue);
		try {
			setState(179);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(175);
				match(STRING);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(176);
				match(DECIMAL);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(177);
				fieldref();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(178);
				array();
				}
				break;
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

	public static class FieldrefContext extends ParserRuleContext {
		public List<Fieldref_elementContext> fieldref_element() {
			return getRuleContexts(Fieldref_elementContext.class);
		}
		public Fieldref_elementContext fieldref_element(int i) {
			return getRuleContext(Fieldref_elementContext.class,i);
		}
		public FieldrefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldref; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterFieldref(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitFieldref(this);
		}
	}

	public final FieldrefContext fieldref() throws RecognitionException {
		FieldrefContext _localctx = new FieldrefContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_fieldref);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(182); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(181);
					fieldref_element();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(184); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
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

	public static class Fieldref_elementContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(ConfigParser.LBRACKET, 0); }
		public TerminalNode IDENTIFIER() { return getToken(ConfigParser.IDENTIFIER, 0); }
		public TerminalNode RBRACKET() { return getToken(ConfigParser.RBRACKET, 0); }
		public Fieldref_elementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldref_element; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterFieldref_element(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitFieldref_element(this);
		}
	}

	public final Fieldref_elementContext fieldref_element() throws RecognitionException {
		Fieldref_elementContext _localctx = new Fieldref_elementContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_fieldref_element);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(LBRACKET);
			setState(187);
			match(IDENTIFIER);
			setState(188);
			match(RBRACKET);
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

	public static class ArrayContext extends ParserRuleContext {
		public TerminalNode LBRACKET() { return getToken(ConfigParser.LBRACKET, 0); }
		public TerminalNode RBRACKET() { return getToken(ConfigParser.RBRACKET, 0); }
		public List<Array_elementContext> array_element() {
			return getRuleContexts(Array_elementContext.class);
		}
		public Array_elementContext array_element(int i) {
			return getRuleContext(Array_elementContext.class,i);
		}
		public List<TerminalNode> COMA() { return getTokens(ConfigParser.COMA); }
		public TerminalNode COMA(int i) {
			return getToken(ConfigParser.COMA, i);
		}
		public ArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitArray(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(190);
			match(LBRACKET);
			setState(199);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LBRACE) | (1L << LBRACKET) | (1L << STRING) | (1L << DECIMAL) | (1L << IDENTIFIER))) != 0)) {
				{
				setState(191);
				array_element();
				setState(196);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==COMA) {
					{
					{
					setState(192);
					match(COMA);
					setState(193);
					array_element();
					}
					}
					setState(198);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(201);
			match(RBRACKET);
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

	public static class Array_elementContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(ConfigParser.IDENTIFIER, 0); }
		public TerminalNode STRING() { return getToken(ConfigParser.STRING, 0); }
		public TerminalNode DECIMAL() { return getToken(ConfigParser.DECIMAL, 0); }
		public ArrayContext array() {
			return getRuleContext(ArrayContext.class,0);
		}
		public HashContext hash() {
			return getRuleContext(HashContext.class,0);
		}
		public Array_elementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_array_element; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterArray_element(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitArray_element(this);
		}
	}

	public final Array_elementContext array_element() throws RecognitionException {
		Array_elementContext _localctx = new Array_elementContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_array_element);
		try {
			setState(208);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(203);
				match(IDENTIFIER);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(204);
				match(STRING);
				}
				break;
			case DECIMAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(205);
				match(DECIMAL);
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 4);
				{
				setState(206);
				array();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 5);
				{
				setState(207);
				hash();
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

	public static class HashContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(ConfigParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(ConfigParser.RBRACE, 0); }
		public List<Hash_elementContext> hash_element() {
			return getRuleContexts(Hash_elementContext.class);
		}
		public Hash_elementContext hash_element(int i) {
			return getRuleContext(Hash_elementContext.class,i);
		}
		public HashContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hash; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterHash(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitHash(this);
		}
	}

	public final HashContext hash() throws RecognitionException {
		HashContext _localctx = new HashContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_hash);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(210);
			match(LBRACE);
			setState(214);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << DECIMAL) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(211);
				hash_element();
				}
				}
				setState(216);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(217);
			match(RBRACE);
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

	public static class Hash_elementContext extends ParserRuleContext {
		public TerminalNode HASHROCKET() { return getToken(ConfigParser.HASHROCKET, 0); }
		public Plugin_attribute_valueContext plugin_attribute_value() {
			return getRuleContext(Plugin_attribute_valueContext.class,0);
		}
		public TerminalNode DECIMAL() { return getToken(ConfigParser.DECIMAL, 0); }
		public TerminalNode IDENTIFIER() { return getToken(ConfigParser.IDENTIFIER, 0); }
		public TerminalNode STRING() { return getToken(ConfigParser.STRING, 0); }
		public Hash_elementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hash_element; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).enterHash_element(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof ConfigListener ) ((ConfigListener)listener).exitHash_element(this);
		}
	}

	public final Hash_elementContext hash_element() throws RecognitionException {
		Hash_elementContext _localctx = new Hash_elementContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_hash_element);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << DECIMAL) | (1L << IDENTIFIER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(220);
			match(HASHROCKET);
			setState(221);
			plugin_attribute_value();
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
		case 9:
			return logical_expression_sempred((Logical_expressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean logical_expression_sempred(Logical_expressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 10);
		case 1:
			return precpred(_ctx, 9);
		case 2:
			return precpred(_ctx, 8);
		case 3:
			return precpred(_ctx, 7);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\'\u00e2\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\6\2.\n\2\r\2\16\2/\3\2\3"+
		"\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\7\4;\n\4\f\4\16\4>\13\4\3\4\3\4\3\5\3\5"+
		"\3\6\3\6\3\6\3\7\3\7\7\7I\n\7\f\7\16\7L\13\7\3\7\3\7\3\b\3\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\t\3\t\3\t\5\tZ\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\7\nd\n"+
		"\n\f\n\16\ng\13\n\3\n\3\n\5\nk\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\5\13w\n\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13"+
		"\3\13\3\13\3\13\7\13\u0085\n\13\f\13\16\13\u0088\13\13\3\f\3\f\3\f\3\r"+
		"\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\3\r\3\r\3\r\3\r\3\r\5\r\u00a5\n\r\3\16\3\16\5\16\u00a9\n\16\3\16\3"+
		"\16\3\16\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\5\20\u00b6\n\20\3\21"+
		"\6\21\u00b9\n\21\r\21\16\21\u00ba\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3"+
		"\23\7\23\u00c5\n\23\f\23\16\23\u00c8\13\23\5\23\u00ca\n\23\3\23\3\23\3"+
		"\24\3\24\3\24\3\24\3\24\5\24\u00d3\n\24\3\25\3\25\7\25\u00d7\n\25\f\25"+
		"\16\25\u00da\13\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\2\3\24\27\2\4\6"+
		"\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*\2\6\3\2\4\6\3\2\32\33\3\2\"#"+
		"\4\2\"\"$%\2\u00f2\2-\3\2\2\2\4\63\3\2\2\2\6\66\3\2\2\2\bA\3\2\2\2\nC"+
		"\3\2\2\2\fF\3\2\2\2\16O\3\2\2\2\20Y\3\2\2\2\22[\3\2\2\2\24v\3\2\2\2\26"+
		"\u0089\3\2\2\2\30\u00a4\3\2\2\2\32\u00a6\3\2\2\2\34\u00ad\3\2\2\2\36\u00b5"+
		"\3\2\2\2 \u00b8\3\2\2\2\"\u00bc\3\2\2\2$\u00c0\3\2\2\2&\u00d2\3\2\2\2"+
		"(\u00d4\3\2\2\2*\u00dd\3\2\2\2,.\5\4\3\2-,\3\2\2\2./\3\2\2\2/-\3\2\2\2"+
		"/\60\3\2\2\2\60\61\3\2\2\2\61\62\7\2\2\3\62\3\3\2\2\2\63\64\t\2\2\2\64"+
		"\65\5\6\4\2\65\5\3\2\2\2\66<\7\b\2\2\67;\5\n\6\28;\5\22\n\29;\5\b\5\2"+
		":\67\3\2\2\2:8\3\2\2\2:9\3\2\2\2;>\3\2\2\2<:\3\2\2\2<=\3\2\2\2=?\3\2\2"+
		"\2><\3\2\2\2?@\7\t\2\2@\7\3\2\2\2AB\7\3\2\2B\t\3\2\2\2CD\7%\2\2DE\5\f"+
		"\7\2E\13\3\2\2\2FJ\7\b\2\2GI\5\16\b\2HG\3\2\2\2IL\3\2\2\2JH\3\2\2\2JK"+
		"\3\2\2\2KM\3\2\2\2LJ\3\2\2\2MN\7\t\2\2N\r\3\2\2\2OP\7%\2\2PQ\7\17\2\2"+
		"QR\5\20\t\2R\17\3\2\2\2SZ\5\n\6\2TZ\7%\2\2UZ\7\"\2\2VZ\7$\2\2WZ\5$\23"+
		"\2XZ\5(\25\2YS\3\2\2\2YT\3\2\2\2YU\3\2\2\2YV\3\2\2\2YW\3\2\2\2YX\3\2\2"+
		"\2Z\21\3\2\2\2[\\\7 \2\2\\]\5\24\13\2]e\5\6\4\2^_\7!\2\2_`\7 \2\2`a\5"+
		"\24\13\2ab\5\6\4\2bd\3\2\2\2c^\3\2\2\2dg\3\2\2\2ec\3\2\2\2ef\3\2\2\2f"+
		"j\3\2\2\2ge\3\2\2\2hi\7!\2\2ik\5\6\4\2jh\3\2\2\2jk\3\2\2\2k\23\3\2\2\2"+
		"lm\b\13\1\2mw\5\30\r\2nw\5\32\16\2ow\5\34\17\2pw\5\26\f\2qr\7\n\2\2rs"+
		"\5\24\13\2st\7\13\2\2tw\3\2\2\2uw\5\36\20\2vl\3\2\2\2vn\3\2\2\2vo\3\2"+
		"\2\2vp\3\2\2\2vq\3\2\2\2vu\3\2\2\2w\u0086\3\2\2\2xy\f\f\2\2yz\7\34\2\2"+
		"z\u0085\5\24\13\r{|\f\13\2\2|}\7\35\2\2}\u0085\5\24\13\f~\177\f\n\2\2"+
		"\177\u0080\7\37\2\2\u0080\u0085\5\24\13\13\u0081\u0082\f\t\2\2\u0082\u0083"+
		"\7\36\2\2\u0083\u0085\5\24\13\n\u0084x\3\2\2\2\u0084{\3\2\2\2\u0084~\3"+
		"\2\2\2\u0084\u0081\3\2\2\2\u0085\u0088\3\2\2\2\u0086\u0084\3\2\2\2\u0086"+
		"\u0087\3\2\2\2\u0087\25\3\2\2\2\u0088\u0086\3\2\2\2\u0089\u008a\7\26\2"+
		"\2\u008a\u008b\5\24\13\2\u008b\27\3\2\2\2\u008c\u008d\5\36\20\2\u008d"+
		"\u008e\7\20\2\2\u008e\u008f\5\36\20\2\u008f\u00a5\3\2\2\2\u0090\u0091"+
		"\5\36\20\2\u0091\u0092\7\21\2\2\u0092\u0093\5\36\20\2\u0093\u00a5\3\2"+
		"\2\2\u0094\u0095\5\36\20\2\u0095\u0096\7\22\2\2\u0096\u0097\5\36\20\2"+
		"\u0097\u00a5\3\2\2\2\u0098\u0099\5\36\20\2\u0099\u009a\7\23\2\2\u009a"+
		"\u009b\5\36\20\2\u009b\u00a5\3\2\2\2\u009c\u009d\5\36\20\2\u009d\u009e"+
		"\7\24\2\2\u009e\u009f\5\36\20\2\u009f\u00a5\3\2\2\2\u00a0\u00a1\5\36\20"+
		"\2\u00a1\u00a2\7\25\2\2\u00a2\u00a3\5\36\20\2\u00a3\u00a5\3\2\2\2\u00a4"+
		"\u008c\3\2\2\2\u00a4\u0090\3\2\2\2\u00a4\u0094\3\2\2\2\u00a4\u0098\3\2"+
		"\2\2\u00a4\u009c\3\2\2\2\u00a4\u00a0\3\2\2\2\u00a5\31\3\2\2\2\u00a6\u00a8"+
		"\5\36\20\2\u00a7\u00a9\7\31\2\2\u00a8\u00a7\3\2\2\2\u00a8\u00a9\3\2\2"+
		"\2\u00a9\u00aa\3\2\2\2\u00aa\u00ab\7\30\2\2\u00ab\u00ac\5\36\20\2\u00ac"+
		"\33\3\2\2\2\u00ad\u00ae\5\36\20\2\u00ae\u00af\t\3\2\2\u00af\u00b0\t\4"+
		"\2\2\u00b0\35\3\2\2\2\u00b1\u00b6\7\"\2\2\u00b2\u00b6\7$\2\2\u00b3\u00b6"+
		"\5 \21\2\u00b4\u00b6\5$\23\2\u00b5\u00b1\3\2\2\2\u00b5\u00b2\3\2\2\2\u00b5"+
		"\u00b3\3\2\2\2\u00b5\u00b4\3\2\2\2\u00b6\37\3\2\2\2\u00b7\u00b9\5\"\22"+
		"\2\u00b8\u00b7\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00b8\3\2\2\2\u00ba\u00bb"+
		"\3\2\2\2\u00bb!\3\2\2\2\u00bc\u00bd\7\f\2\2\u00bd\u00be\7%\2\2\u00be\u00bf"+
		"\7\r\2\2\u00bf#\3\2\2\2\u00c0\u00c9\7\f\2\2\u00c1\u00c6\5&\24\2\u00c2"+
		"\u00c3\7\27\2\2\u00c3\u00c5\5&\24\2\u00c4\u00c2\3\2\2\2\u00c5\u00c8\3"+
		"\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7\3\2\2\2\u00c7\u00ca\3\2\2\2\u00c8"+
		"\u00c6\3\2\2\2\u00c9\u00c1\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb\3\2"+
		"\2\2\u00cb\u00cc\7\r\2\2\u00cc%\3\2\2\2\u00cd\u00d3\7%\2\2\u00ce\u00d3"+
		"\7\"\2\2\u00cf\u00d3\7$\2\2\u00d0\u00d3\5$\23\2\u00d1\u00d3\5(\25\2\u00d2"+
		"\u00cd\3\2\2\2\u00d2\u00ce\3\2\2\2\u00d2\u00cf\3\2\2\2\u00d2\u00d0\3\2"+
		"\2\2\u00d2\u00d1\3\2\2\2\u00d3\'\3\2\2\2\u00d4\u00d8\7\b\2\2\u00d5\u00d7"+
		"\5*\26\2\u00d6\u00d5\3\2\2\2\u00d7\u00da\3\2\2\2\u00d8\u00d6\3\2\2\2\u00d8"+
		"\u00d9\3\2\2\2\u00d9\u00db\3\2\2\2\u00da\u00d8\3\2\2\2\u00db\u00dc\7\t"+
		"\2\2\u00dc)\3\2\2\2\u00dd\u00de\t\5\2\2\u00de\u00df\7\17\2\2\u00df\u00e0"+
		"\5\20\t\2\u00e0+\3\2\2\2\24/:<JYejv\u0084\u0086\u00a4\u00a8\u00b5\u00ba"+
		"\u00c6\u00c9\u00d2\u00d8";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}