// Generated from Config.g4 by ANTLR 4.9.1

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
		INPUT=1, FILTER=2, OUTPUT=3, LBRACE=4, RBRACE=5, LPAREN=6, RPAREN=7, LBRACKET=8, 
		RBRACKET=9, QMARK=10, HASHROCKET=11, GT=12, GE=13, LT=14, LE=15, EQ=16, 
		NEQ=17, BANG=18, COMA=19, IN=20, NOT=21, MATCH=22, NOT_MATCH=23, AND=24, 
		OR=25, XOR=26, NAND=27, IF=28, ELSE=29, STRING=30, REGEX=31, DECIMAL=32, 
		IDENTIFIER=33, WS=34, COMMENT=35;
	public static final int
		RULE_config = 0, RULE_stage_declaration = 1, RULE_stage_definition = 2, 
		RULE_plugin_declaration = 3, RULE_plugin_definition = 4, RULE_plugin_attribute = 5, 
		RULE_plugin_attribute_value = 6, RULE_stage_condition = 7, RULE_logical_expression = 8, 
		RULE_negative_expression = 9, RULE_compare_expression = 10, RULE_in_expression = 11, 
		RULE_match_expression = 12, RULE_rvalue = 13, RULE_fieldref = 14, RULE_fieldref_element = 15, 
		RULE_array = 16, RULE_array_element = 17, RULE_hash = 18, RULE_hash_element = 19;
	private static String[] makeRuleNames() {
		return new String[] {
			"config", "stage_declaration", "stage_definition", "plugin_declaration", 
			"plugin_definition", "plugin_attribute", "plugin_attribute_value", "stage_condition", 
			"logical_expression", "negative_expression", "compare_expression", "in_expression", 
			"match_expression", "rvalue", "fieldref", "fieldref_element", "array", 
			"array_element", "hash", "hash_element"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'input'", "'filter'", "'output'", "'{'", "'}'", "'('", "')'", 
			"'['", "']'", "'?'", "'=>'", "'>'", "'>='", "'<'", "'<='", "'=='", "'!='", 
			"'!'", "','", "'in'", "'not'", "'=~'", "'!~'", "'and'", "'or'", "'xor'", 
			"'nand'", "'if'", "'else'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "INPUT", "FILTER", "OUTPUT", "LBRACE", "RBRACE", "LPAREN", "RPAREN", 
			"LBRACKET", "RBRACKET", "QMARK", "HASHROCKET", "GT", "GE", "LT", "LE", 
			"EQ", "NEQ", "BANG", "COMA", "IN", "NOT", "MATCH", "NOT_MATCH", "AND", 
			"OR", "XOR", "NAND", "IF", "ELSE", "STRING", "REGEX", "DECIMAL", "IDENTIFIER", 
			"WS", "COMMENT"
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitConfig(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ConfigContext config() throws RecognitionException {
		ConfigContext _localctx = new ConfigContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_config);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(40);
				stage_declaration();
				}
				}
				setState(43); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INPUT) | (1L << FILTER) | (1L << OUTPUT))) != 0) );
			setState(45);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitStage_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Stage_declarationContext stage_declaration() throws RecognitionException {
		Stage_declarationContext _localctx = new Stage_declarationContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_stage_declaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(47);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INPUT) | (1L << FILTER) | (1L << OUTPUT))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(48);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitStage_definition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Stage_definitionContext stage_definition() throws RecognitionException {
		Stage_definitionContext _localctx = new Stage_definitionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_stage_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(50);
			match(LBRACE);
			setState(55);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IF || _la==IDENTIFIER) {
				{
				setState(53);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IDENTIFIER:
					{
					setState(51);
					plugin_declaration();
					}
					break;
				case IF:
					{
					setState(52);
					stage_condition();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(57);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(58);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitPlugin_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Plugin_declarationContext plugin_declaration() throws RecognitionException {
		Plugin_declarationContext _localctx = new Plugin_declarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_plugin_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(60);
			match(IDENTIFIER);
			setState(61);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitPlugin_definition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Plugin_definitionContext plugin_definition() throws RecognitionException {
		Plugin_definitionContext _localctx = new Plugin_definitionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_plugin_definition);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(63);
			match(LBRACE);
			setState(67);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IDENTIFIER) {
				{
				{
				setState(64);
				plugin_attribute();
				}
				}
				setState(69);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(70);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitPlugin_attribute(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Plugin_attributeContext plugin_attribute() throws RecognitionException {
		Plugin_attributeContext _localctx = new Plugin_attributeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_plugin_attribute);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72);
			match(IDENTIFIER);
			setState(73);
			match(HASHROCKET);
			setState(74);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitPlugin_attribute_value(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Plugin_attribute_valueContext plugin_attribute_value() throws RecognitionException {
		Plugin_attribute_valueContext _localctx = new Plugin_attribute_valueContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_plugin_attribute_value);
		try {
			setState(82);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(76);
				plugin_declaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(77);
				match(IDENTIFIER);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(78);
				match(STRING);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(79);
				match(DECIMAL);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(80);
				array();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(81);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitStage_condition(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Stage_conditionContext stage_condition() throws RecognitionException {
		Stage_conditionContext _localctx = new Stage_conditionContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_stage_condition);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(IF);
			setState(85);
			logical_expression(0);
			setState(86);
			stage_definition();
			setState(94);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(87);
					match(ELSE);
					setState(88);
					match(IF);
					setState(89);
					logical_expression(0);
					setState(90);
					stage_definition();
					}
					} 
				}
				setState(96);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			setState(99);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(97);
				match(ELSE);
				setState(98);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitLogical_expression(this);
			else return visitor.visitChildren(this);
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
		int _startState = 16;
		enterRecursionRule(_localctx, 16, RULE_logical_expression, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(102);
				compare_expression();
				}
				break;
			case 2:
				{
				setState(103);
				in_expression();
				}
				break;
			case 3:
				{
				setState(104);
				match_expression();
				}
				break;
			case 4:
				{
				setState(105);
				negative_expression();
				}
				break;
			case 5:
				{
				setState(106);
				match(LPAREN);
				setState(107);
				logical_expression(0);
				setState(108);
				match(RPAREN);
				}
				break;
			case 6:
				{
				setState(110);
				rvalue();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(127);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(125);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new Logical_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(113);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(114);
						match(AND);
						setState(115);
						logical_expression(11);
						}
						break;
					case 2:
						{
						_localctx = new Logical_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(116);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(117);
						match(OR);
						setState(118);
						logical_expression(10);
						}
						break;
					case 3:
						{
						_localctx = new Logical_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(119);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(120);
						match(NAND);
						setState(121);
						logical_expression(9);
						}
						break;
					case 4:
						{
						_localctx = new Logical_expressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_logical_expression);
						setState(122);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(123);
						match(XOR);
						setState(124);
						logical_expression(8);
						}
						break;
					}
					} 
				}
				setState(129);
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
		public TerminalNode LPAREN() { return getToken(ConfigParser.LPAREN, 0); }
		public Logical_expressionContext logical_expression() {
			return getRuleContext(Logical_expressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(ConfigParser.RPAREN, 0); }
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitNegative_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Negative_expressionContext negative_expression() throws RecognitionException {
		Negative_expressionContext _localctx = new Negative_expressionContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_negative_expression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(BANG);
			setState(131);
			match(LPAREN);
			setState(132);
			logical_expression(0);
			setState(133);
			match(RPAREN);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitCompare_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Compare_expressionContext compare_expression() throws RecognitionException {
		Compare_expressionContext _localctx = new Compare_expressionContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_compare_expression);
		try {
			setState(159);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(135);
				rvalue();
				setState(136);
				match(GT);
				setState(137);
				rvalue();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(139);
				rvalue();
				setState(140);
				match(GE);
				setState(141);
				rvalue();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(143);
				rvalue();
				setState(144);
				match(LT);
				setState(145);
				rvalue();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(147);
				rvalue();
				setState(148);
				match(LE);
				setState(149);
				rvalue();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(151);
				rvalue();
				setState(152);
				match(EQ);
				setState(153);
				rvalue();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(155);
				rvalue();
				setState(156);
				match(NEQ);
				setState(157);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitIn_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final In_expressionContext in_expression() throws RecognitionException {
		In_expressionContext _localctx = new In_expressionContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_in_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			rvalue();
			setState(163);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NOT) {
				{
				setState(162);
				match(NOT);
				}
			}

			setState(165);
			match(IN);
			setState(166);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitMatch_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Match_expressionContext match_expression() throws RecognitionException {
		Match_expressionContext _localctx = new Match_expressionContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_match_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(168);
			rvalue();
			setState(169);
			_la = _input.LA(1);
			if ( !(_la==MATCH || _la==NOT_MATCH) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(170);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitRvalue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RvalueContext rvalue() throws RecognitionException {
		RvalueContext _localctx = new RvalueContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_rvalue);
		try {
			setState(176);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(172);
				match(STRING);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(173);
				match(DECIMAL);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(174);
				fieldref();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(175);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitFieldref(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldrefContext fieldref() throws RecognitionException {
		FieldrefContext _localctx = new FieldrefContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_fieldref);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(179); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(178);
					fieldref_element();
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(181); 
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitFieldref_element(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Fieldref_elementContext fieldref_element() throws RecognitionException {
		Fieldref_elementContext _localctx = new Fieldref_elementContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_fieldref_element);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			match(LBRACKET);
			setState(184);
			match(IDENTIFIER);
			setState(185);
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
		public List<Array_elementContext> array_element() {
			return getRuleContexts(Array_elementContext.class);
		}
		public Array_elementContext array_element(int i) {
			return getRuleContext(Array_elementContext.class,i);
		}
		public TerminalNode RBRACKET() { return getToken(ConfigParser.RBRACKET, 0); }
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArrayContext array() throws RecognitionException {
		ArrayContext _localctx = new ArrayContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_array);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			match(LBRACKET);
			setState(188);
			array_element();
			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(189);
				match(COMA);
				setState(190);
				array_element();
				}
				}
				setState(195);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(196);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitArray_element(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Array_elementContext array_element() throws RecognitionException {
		Array_elementContext _localctx = new Array_elementContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_array_element);
		try {
			setState(203);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER:
				enterOuterAlt(_localctx, 1);
				{
				setState(198);
				match(IDENTIFIER);
				}
				break;
			case STRING:
				enterOuterAlt(_localctx, 2);
				{
				setState(199);
				match(STRING);
				}
				break;
			case DECIMAL:
				enterOuterAlt(_localctx, 3);
				{
				setState(200);
				match(DECIMAL);
				}
				break;
			case LBRACKET:
				enterOuterAlt(_localctx, 4);
				{
				setState(201);
				array();
				}
				break;
			case LBRACE:
				enterOuterAlt(_localctx, 5);
				{
				setState(202);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitHash(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HashContext hash() throws RecognitionException {
		HashContext _localctx = new HashContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_hash);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(205);
			match(LBRACE);
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << DECIMAL) | (1L << IDENTIFIER))) != 0)) {
				{
				{
				setState(206);
				hash_element();
				}
				}
				setState(211);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(212);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof ConfigVisitor ) return ((ConfigVisitor<? extends T>)visitor).visitHash_element(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Hash_elementContext hash_element() throws RecognitionException {
		Hash_elementContext _localctx = new Hash_elementContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_hash_element);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << DECIMAL) | (1L << IDENTIFIER))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(215);
			match(HASHROCKET);
			setState(216);
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
		case 8:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3%\u00dd\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\3\2\6\2,\n\2\r\2\16\2-\3\2\3\2\3\3\3\3"+
		"\3\3\3\4\3\4\3\4\7\48\n\4\f\4\16\4;\13\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\7"+
		"\6D\n\6\f\6\16\6G\13\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b"+
		"\5\bU\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t_\n\t\f\t\16\tb\13\t\3\t"+
		"\3\t\5\tf\n\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\nr\n\n\3\n\3\n"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u0080\n\n\f\n\16\n\u0083"+
		"\13\n\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\3\f\5\f\u00a2\n\f"+
		"\3\r\3\r\5\r\u00a6\n\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17"+
		"\3\17\5\17\u00b3\n\17\3\20\6\20\u00b6\n\20\r\20\16\20\u00b7\3\21\3\21"+
		"\3\21\3\21\3\22\3\22\3\22\3\22\7\22\u00c2\n\22\f\22\16\22\u00c5\13\22"+
		"\3\22\3\22\3\23\3\23\3\23\3\23\3\23\5\23\u00ce\n\23\3\24\3\24\7\24\u00d2"+
		"\n\24\f\24\16\24\u00d5\13\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\2\3\22"+
		"\26\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(\2\6\3\2\3\5\3\2\30\31"+
		"\3\2 !\4\2  \"#\2\u00ec\2+\3\2\2\2\4\61\3\2\2\2\6\64\3\2\2\2\b>\3\2\2"+
		"\2\nA\3\2\2\2\fJ\3\2\2\2\16T\3\2\2\2\20V\3\2\2\2\22q\3\2\2\2\24\u0084"+
		"\3\2\2\2\26\u00a1\3\2\2\2\30\u00a3\3\2\2\2\32\u00aa\3\2\2\2\34\u00b2\3"+
		"\2\2\2\36\u00b5\3\2\2\2 \u00b9\3\2\2\2\"\u00bd\3\2\2\2$\u00cd\3\2\2\2"+
		"&\u00cf\3\2\2\2(\u00d8\3\2\2\2*,\5\4\3\2+*\3\2\2\2,-\3\2\2\2-+\3\2\2\2"+
		"-.\3\2\2\2./\3\2\2\2/\60\7\2\2\3\60\3\3\2\2\2\61\62\t\2\2\2\62\63\5\6"+
		"\4\2\63\5\3\2\2\2\649\7\6\2\2\658\5\b\5\2\668\5\20\t\2\67\65\3\2\2\2\67"+
		"\66\3\2\2\28;\3\2\2\29\67\3\2\2\29:\3\2\2\2:<\3\2\2\2;9\3\2\2\2<=\7\7"+
		"\2\2=\7\3\2\2\2>?\7#\2\2?@\5\n\6\2@\t\3\2\2\2AE\7\6\2\2BD\5\f\7\2CB\3"+
		"\2\2\2DG\3\2\2\2EC\3\2\2\2EF\3\2\2\2FH\3\2\2\2GE\3\2\2\2HI\7\7\2\2I\13"+
		"\3\2\2\2JK\7#\2\2KL\7\r\2\2LM\5\16\b\2M\r\3\2\2\2NU\5\b\5\2OU\7#\2\2P"+
		"U\7 \2\2QU\7\"\2\2RU\5\"\22\2SU\5&\24\2TN\3\2\2\2TO\3\2\2\2TP\3\2\2\2"+
		"TQ\3\2\2\2TR\3\2\2\2TS\3\2\2\2U\17\3\2\2\2VW\7\36\2\2WX\5\22\n\2X`\5\6"+
		"\4\2YZ\7\37\2\2Z[\7\36\2\2[\\\5\22\n\2\\]\5\6\4\2]_\3\2\2\2^Y\3\2\2\2"+
		"_b\3\2\2\2`^\3\2\2\2`a\3\2\2\2ae\3\2\2\2b`\3\2\2\2cd\7\37\2\2df\5\6\4"+
		"\2ec\3\2\2\2ef\3\2\2\2f\21\3\2\2\2gh\b\n\1\2hr\5\26\f\2ir\5\30\r\2jr\5"+
		"\32\16\2kr\5\24\13\2lm\7\b\2\2mn\5\22\n\2no\7\t\2\2or\3\2\2\2pr\5\34\17"+
		"\2qg\3\2\2\2qi\3\2\2\2qj\3\2\2\2qk\3\2\2\2ql\3\2\2\2qp\3\2\2\2r\u0081"+
		"\3\2\2\2st\f\f\2\2tu\7\32\2\2u\u0080\5\22\n\rvw\f\13\2\2wx\7\33\2\2x\u0080"+
		"\5\22\n\fyz\f\n\2\2z{\7\35\2\2{\u0080\5\22\n\13|}\f\t\2\2}~\7\34\2\2~"+
		"\u0080\5\22\n\n\177s\3\2\2\2\177v\3\2\2\2\177y\3\2\2\2\177|\3\2\2\2\u0080"+
		"\u0083\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082\23\3\2\2\2"+
		"\u0083\u0081\3\2\2\2\u0084\u0085\7\24\2\2\u0085\u0086\7\b\2\2\u0086\u0087"+
		"\5\22\n\2\u0087\u0088\7\t\2\2\u0088\25\3\2\2\2\u0089\u008a\5\34\17\2\u008a"+
		"\u008b\7\16\2\2\u008b\u008c\5\34\17\2\u008c\u00a2\3\2\2\2\u008d\u008e"+
		"\5\34\17\2\u008e\u008f\7\17\2\2\u008f\u0090\5\34\17\2\u0090\u00a2\3\2"+
		"\2\2\u0091\u0092\5\34\17\2\u0092\u0093\7\20\2\2\u0093\u0094\5\34\17\2"+
		"\u0094\u00a2\3\2\2\2\u0095\u0096\5\34\17\2\u0096\u0097\7\21\2\2\u0097"+
		"\u0098\5\34\17\2\u0098\u00a2\3\2\2\2\u0099\u009a\5\34\17\2\u009a\u009b"+
		"\7\22\2\2\u009b\u009c\5\34\17\2\u009c\u00a2\3\2\2\2\u009d\u009e\5\34\17"+
		"\2\u009e\u009f\7\23\2\2\u009f\u00a0\5\34\17\2\u00a0\u00a2\3\2\2\2\u00a1"+
		"\u0089\3\2\2\2\u00a1\u008d\3\2\2\2\u00a1\u0091\3\2\2\2\u00a1\u0095\3\2"+
		"\2\2\u00a1\u0099\3\2\2\2\u00a1\u009d\3\2\2\2\u00a2\27\3\2\2\2\u00a3\u00a5"+
		"\5\34\17\2\u00a4\u00a6\7\27\2\2\u00a5\u00a4\3\2\2\2\u00a5\u00a6\3\2\2"+
		"\2\u00a6\u00a7\3\2\2\2\u00a7\u00a8\7\26\2\2\u00a8\u00a9\5\34\17\2\u00a9"+
		"\31\3\2\2\2\u00aa\u00ab\5\34\17\2\u00ab\u00ac\t\3\2\2\u00ac\u00ad\t\4"+
		"\2\2\u00ad\33\3\2\2\2\u00ae\u00b3\7 \2\2\u00af\u00b3\7\"\2\2\u00b0\u00b3"+
		"\5\36\20\2\u00b1\u00b3\5\"\22\2\u00b2\u00ae\3\2\2\2\u00b2\u00af\3\2\2"+
		"\2\u00b2\u00b0\3\2\2\2\u00b2\u00b1\3\2\2\2\u00b3\35\3\2\2\2\u00b4\u00b6"+
		"\5 \21\2\u00b5\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7"+
		"\u00b8\3\2\2\2\u00b8\37\3\2\2\2\u00b9\u00ba\7\n\2\2\u00ba\u00bb\7#\2\2"+
		"\u00bb\u00bc\7\13\2\2\u00bc!\3\2\2\2\u00bd\u00be\7\n\2\2\u00be\u00c3\5"+
		"$\23\2\u00bf\u00c0\7\25\2\2\u00c0\u00c2\5$\23\2\u00c1\u00bf\3\2\2\2\u00c2"+
		"\u00c5\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c6\3\2"+
		"\2\2\u00c5\u00c3\3\2\2\2\u00c6\u00c7\7\13\2\2\u00c7#\3\2\2\2\u00c8\u00ce"+
		"\7#\2\2\u00c9\u00ce\7 \2\2\u00ca\u00ce\7\"\2\2\u00cb\u00ce\5\"\22\2\u00cc"+
		"\u00ce\5&\24\2\u00cd\u00c8\3\2\2\2\u00cd\u00c9\3\2\2\2\u00cd\u00ca\3\2"+
		"\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00cc\3\2\2\2\u00ce%\3\2\2\2\u00cf\u00d3"+
		"\7\6\2\2\u00d0\u00d2\5(\25\2\u00d1\u00d0\3\2\2\2\u00d2\u00d5\3\2\2\2\u00d3"+
		"\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00d3\3\2"+
		"\2\2\u00d6\u00d7\7\7\2\2\u00d7\'\3\2\2\2\u00d8\u00d9\t\5\2\2\u00d9\u00da"+
		"\7\r\2\2\u00da\u00db\5\16\b\2\u00db)\3\2\2\2\23-\679ET`eq\177\u0081\u00a1"+
		"\u00a5\u00b2\u00b7\u00c3\u00cd\u00d3";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}