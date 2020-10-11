// Generated from C:/Users/jglrxavpok/Documents/Programmation/MCModding/NBTMCALib/src/antlr/resources\SNBT.g4 by ANTLR 4.8
package org.jglrxavpok.hephaistos.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SNBTParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, DoubleQuoteText=11, SingleQuoteText=12, BOOLEAN=13, NEGATIVE_SIGN=14, 
		FLOAT=15, DOUBLE=16, INTEGER=17, LONG=18, BYTE=19, SHORT=20, IDENTIFIER_LETTERS=21, 
		WS=22;
	public static final int
		RULE_snbt = 0, RULE_element = 1, RULE_compound = 2, RULE_namedElement = 3, 
		RULE_list = 4, RULE_byteArray = 5, RULE_intArray = 6, RULE_longArray = 7, 
		RULE_doubleNBT = 8, RULE_floatNBT = 9, RULE_longNBT = 10, RULE_byteNBT = 11, 
		RULE_shortNBT = 12, RULE_intNBT = 13, RULE_stringNBT = 14, RULE_identifier = 15;
	private static String[] makeRuleNames() {
		return new String[] {
			"snbt", "element", "compound", "namedElement", "list", "byteArray", "intArray", 
			"longArray", "doubleNBT", "floatNBT", "longNBT", "byteNBT", "shortNBT", 
			"intNBT", "stringNBT", "identifier"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "','", "'}'", "':'", "'['", "']'", "'B'", "';'", "'I'", 
			"'L'", null, null, null, "'-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, "DoubleQuoteText", 
			"SingleQuoteText", "BOOLEAN", "NEGATIVE_SIGN", "FLOAT", "DOUBLE", "INTEGER", 
			"LONG", "BYTE", "SHORT", "IDENTIFIER_LETTERS", "WS"
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
	public String getGrammarFileName() { return "SNBT.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public SNBTParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class SnbtContext extends ParserRuleContext {
		public ElementContext element() {
			return getRuleContext(ElementContext.class,0);
		}
		public TerminalNode EOF() { return getToken(SNBTParser.EOF, 0); }
		public SnbtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_snbt; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterSnbt(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitSnbt(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitSnbt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SnbtContext snbt() throws RecognitionException {
		SnbtContext _localctx = new SnbtContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_snbt);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(32);
			element();
			setState(33);
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

	public static class ElementContext extends ParserRuleContext {
		public ByteNBTContext byteNBT() {
			return getRuleContext(ByteNBTContext.class,0);
		}
		public FloatNBTContext floatNBT() {
			return getRuleContext(FloatNBTContext.class,0);
		}
		public ShortNBTContext shortNBT() {
			return getRuleContext(ShortNBTContext.class,0);
		}
		public LongNBTContext longNBT() {
			return getRuleContext(LongNBTContext.class,0);
		}
		public IntNBTContext intNBT() {
			return getRuleContext(IntNBTContext.class,0);
		}
		public DoubleNBTContext doubleNBT() {
			return getRuleContext(DoubleNBTContext.class,0);
		}
		public StringNBTContext stringNBT() {
			return getRuleContext(StringNBTContext.class,0);
		}
		public ByteArrayContext byteArray() {
			return getRuleContext(ByteArrayContext.class,0);
		}
		public IntArrayContext intArray() {
			return getRuleContext(IntArrayContext.class,0);
		}
		public LongArrayContext longArray() {
			return getRuleContext(LongArrayContext.class,0);
		}
		public ListContext list() {
			return getRuleContext(ListContext.class,0);
		}
		public CompoundContext compound() {
			return getRuleContext(CompoundContext.class,0);
		}
		public ElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_element; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ElementContext element() throws RecognitionException {
		ElementContext _localctx = new ElementContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_element);
		try {
			setState(47);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(35);
				byteNBT();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(36);
				floatNBT();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(37);
				shortNBT();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(38);
				longNBT();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(39);
				intNBT();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(40);
				doubleNBT();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(41);
				stringNBT();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(42);
				byteArray();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(43);
				intArray();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(44);
				longArray();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(45);
				list();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(46);
				compound();
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

	public static class CompoundContext extends ParserRuleContext {
		public List<NamedElementContext> namedElement() {
			return getRuleContexts(NamedElementContext.class);
		}
		public NamedElementContext namedElement(int i) {
			return getRuleContext(NamedElementContext.class,i);
		}
		public CompoundContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compound; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterCompound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitCompound(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitCompound(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompoundContext compound() throws RecognitionException {
		CompoundContext _localctx = new CompoundContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_compound);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(49);
			match(T__0);
			setState(58);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DoubleQuoteText) | (1L << SingleQuoteText) | (1L << IDENTIFIER_LETTERS))) != 0)) {
				{
				setState(50);
				namedElement();
				setState(55);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(51);
						match(T__1);
						setState(52);
						namedElement();
						}
						} 
					}
					setState(57);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				}
			}

			setState(61);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(60);
				match(T__1);
				}
			}

			setState(63);
			match(T__2);
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

	public static class NamedElementContext extends ParserRuleContext {
		public StringNBTContext name;
		public ElementContext value;
		public StringNBTContext stringNBT() {
			return getRuleContext(StringNBTContext.class,0);
		}
		public ElementContext element() {
			return getRuleContext(ElementContext.class,0);
		}
		public NamedElementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namedElement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterNamedElement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitNamedElement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitNamedElement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamedElementContext namedElement() throws RecognitionException {
		NamedElementContext _localctx = new NamedElementContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_namedElement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65);
			((NamedElementContext)_localctx).name = stringNBT();
			setState(66);
			match(T__3);
			setState(67);
			((NamedElementContext)_localctx).value = element();
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

	public static class ListContext extends ParserRuleContext {
		public List<ElementContext> element() {
			return getRuleContexts(ElementContext.class);
		}
		public ElementContext element(int i) {
			return getRuleContext(ElementContext.class,i);
		}
		public ListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ListContext list() throws RecognitionException {
		ListContext _localctx = new ListContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_list);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(69);
			match(T__4);
			setState(78);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__4) | (1L << DoubleQuoteText) | (1L << SingleQuoteText) | (1L << BOOLEAN) | (1L << FLOAT) | (1L << DOUBLE) | (1L << INTEGER) | (1L << LONG) | (1L << BYTE) | (1L << SHORT) | (1L << IDENTIFIER_LETTERS))) != 0)) {
				{
				setState(70);
				element();
				setState(75);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(71);
						match(T__1);
						setState(72);
						element();
						}
						} 
					}
					setState(77);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				}
				}
			}

			setState(81);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(80);
				match(T__1);
				}
			}

			setState(83);
			match(T__5);
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

	public static class ByteArrayContext extends ParserRuleContext {
		public List<ByteNBTContext> byteNBT() {
			return getRuleContexts(ByteNBTContext.class);
		}
		public ByteNBTContext byteNBT(int i) {
			return getRuleContext(ByteNBTContext.class,i);
		}
		public ByteArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_byteArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterByteArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitByteArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitByteArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ByteArrayContext byteArray() throws RecognitionException {
		ByteArrayContext _localctx = new ByteArrayContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_byteArray);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(85);
			match(T__4);
			setState(86);
			match(T__6);
			setState(87);
			match(T__7);
			setState(99);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==BOOLEAN || _la==BYTE) {
				{
				setState(88);
				byteNBT();
				setState(93);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(89);
						match(T__1);
						setState(90);
						byteNBT();
						}
						} 
					}
					setState(95);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				}
				setState(97);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(96);
					match(T__1);
					}
				}

				}
			}

			setState(101);
			match(T__5);
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

	public static class IntArrayContext extends ParserRuleContext {
		public List<IntNBTContext> intNBT() {
			return getRuleContexts(IntNBTContext.class);
		}
		public IntNBTContext intNBT(int i) {
			return getRuleContext(IntNBTContext.class,i);
		}
		public IntArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterIntArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitIntArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitIntArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntArrayContext intArray() throws RecognitionException {
		IntArrayContext _localctx = new IntArrayContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_intArray);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(T__4);
			setState(104);
			match(T__8);
			setState(105);
			match(T__7);
			setState(117);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INTEGER) {
				{
				setState(106);
				intNBT();
				setState(111);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(107);
						match(T__1);
						setState(108);
						intNBT();
						}
						} 
					}
					setState(113);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				}
				setState(115);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(114);
					match(T__1);
					}
				}

				}
			}

			setState(119);
			match(T__5);
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

	public static class LongArrayContext extends ParserRuleContext {
		public List<LongNBTContext> longNBT() {
			return getRuleContexts(LongNBTContext.class);
		}
		public LongNBTContext longNBT(int i) {
			return getRuleContext(LongNBTContext.class,i);
		}
		public LongArrayContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_longArray; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterLongArray(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitLongArray(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitLongArray(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LongArrayContext longArray() throws RecognitionException {
		LongArrayContext _localctx = new LongArrayContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_longArray);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			match(T__4);
			setState(122);
			match(T__9);
			setState(123);
			match(T__7);
			setState(135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LONG) {
				{
				setState(124);
				longNBT();
				setState(129);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(125);
						match(T__1);
						setState(126);
						longNBT();
						}
						} 
					}
					setState(131);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				}
				setState(133);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(132);
					match(T__1);
					}
				}

				}
			}

			setState(137);
			match(T__5);
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

	public static class DoubleNBTContext extends ParserRuleContext {
		public TerminalNode DOUBLE() { return getToken(SNBTParser.DOUBLE, 0); }
		public DoubleNBTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_doubleNBT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterDoubleNBT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitDoubleNBT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitDoubleNBT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DoubleNBTContext doubleNBT() throws RecognitionException {
		DoubleNBTContext _localctx = new DoubleNBTContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_doubleNBT);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
			match(DOUBLE);
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

	public static class FloatNBTContext extends ParserRuleContext {
		public TerminalNode FLOAT() { return getToken(SNBTParser.FLOAT, 0); }
		public FloatNBTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_floatNBT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterFloatNBT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitFloatNBT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitFloatNBT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FloatNBTContext floatNBT() throws RecognitionException {
		FloatNBTContext _localctx = new FloatNBTContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_floatNBT);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(FLOAT);
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

	public static class LongNBTContext extends ParserRuleContext {
		public TerminalNode LONG() { return getToken(SNBTParser.LONG, 0); }
		public LongNBTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_longNBT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterLongNBT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitLongNBT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitLongNBT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LongNBTContext longNBT() throws RecognitionException {
		LongNBTContext _localctx = new LongNBTContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_longNBT);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(LONG);
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

	public static class ByteNBTContext extends ParserRuleContext {
		public TerminalNode BYTE() { return getToken(SNBTParser.BYTE, 0); }
		public TerminalNode BOOLEAN() { return getToken(SNBTParser.BOOLEAN, 0); }
		public ByteNBTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_byteNBT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterByteNBT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitByteNBT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitByteNBT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ByteNBTContext byteNBT() throws RecognitionException {
		ByteNBTContext _localctx = new ByteNBTContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_byteNBT);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			_la = _input.LA(1);
			if ( !(_la==BOOLEAN || _la==BYTE) ) {
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

	public static class ShortNBTContext extends ParserRuleContext {
		public TerminalNode SHORT() { return getToken(SNBTParser.SHORT, 0); }
		public ShortNBTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_shortNBT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterShortNBT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitShortNBT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitShortNBT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ShortNBTContext shortNBT() throws RecognitionException {
		ShortNBTContext _localctx = new ShortNBTContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_shortNBT);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(SHORT);
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

	public static class IntNBTContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(SNBTParser.INTEGER, 0); }
		public IntNBTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_intNBT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterIntNBT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitIntNBT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitIntNBT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntNBTContext intNBT() throws RecognitionException {
		IntNBTContext _localctx = new IntNBTContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_intNBT);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(149);
			match(INTEGER);
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

	public static class StringNBTContext extends ParserRuleContext {
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode DoubleQuoteText() { return getToken(SNBTParser.DoubleQuoteText, 0); }
		public TerminalNode SingleQuoteText() { return getToken(SNBTParser.SingleQuoteText, 0); }
		public StringNBTContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stringNBT; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterStringNBT(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitStringNBT(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitStringNBT(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringNBTContext stringNBT() throws RecognitionException {
		StringNBTContext _localctx = new StringNBTContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_stringNBT);
		try {
			setState(154);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER_LETTERS:
				enterOuterAlt(_localctx, 1);
				{
				setState(151);
				identifier();
				}
				break;
			case DoubleQuoteText:
				enterOuterAlt(_localctx, 2);
				{
				setState(152);
				match(DoubleQuoteText);
				}
				break;
			case SingleQuoteText:
				enterOuterAlt(_localctx, 3);
				{
				setState(153);
				match(SingleQuoteText);
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

	public static class IdentifierContext extends ParserRuleContext {
		public List<TerminalNode> IDENTIFIER_LETTERS() { return getTokens(SNBTParser.IDENTIFIER_LETTERS); }
		public TerminalNode IDENTIFIER_LETTERS(int i) {
			return getToken(SNBTParser.IDENTIFIER_LETTERS, i);
		}
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(156);
				match(IDENTIFIER_LETTERS);
				}
				}
				setState(159); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==IDENTIFIER_LETTERS );
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\30\u00a4\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3\2"+
		"\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\62\n\3\3\4\3"+
		"\4\3\4\3\4\7\48\n\4\f\4\16\4;\13\4\5\4=\n\4\3\4\5\4@\n\4\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\6\3\6\3\6\3\6\7\6L\n\6\f\6\16\6O\13\6\5\6Q\n\6\3\6\5\6T\n"+
		"\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\7\7^\n\7\f\7\16\7a\13\7\3\7\5\7d\n"+
		"\7\5\7f\n\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\7\bp\n\b\f\b\16\bs\13\b\3"+
		"\b\5\bv\n\b\5\bx\n\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u0082\n\t\f\t"+
		"\16\t\u0085\13\t\3\t\5\t\u0088\n\t\5\t\u008a\n\t\3\t\3\t\3\n\3\n\3\13"+
		"\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\5\20\u009d\n"+
		"\20\3\21\6\21\u00a0\n\21\r\21\16\21\u00a1\3\21\2\2\22\2\4\6\b\n\f\16\20"+
		"\22\24\26\30\32\34\36 \2\3\4\2\17\17\25\25\2\u00b0\2\"\3\2\2\2\4\61\3"+
		"\2\2\2\6\63\3\2\2\2\bC\3\2\2\2\nG\3\2\2\2\fW\3\2\2\2\16i\3\2\2\2\20{\3"+
		"\2\2\2\22\u008d\3\2\2\2\24\u008f\3\2\2\2\26\u0091\3\2\2\2\30\u0093\3\2"+
		"\2\2\32\u0095\3\2\2\2\34\u0097\3\2\2\2\36\u009c\3\2\2\2 \u009f\3\2\2\2"+
		"\"#\5\4\3\2#$\7\2\2\3$\3\3\2\2\2%\62\5\30\r\2&\62\5\24\13\2\'\62\5\32"+
		"\16\2(\62\5\26\f\2)\62\5\34\17\2*\62\5\22\n\2+\62\5\36\20\2,\62\5\f\7"+
		"\2-\62\5\16\b\2.\62\5\20\t\2/\62\5\n\6\2\60\62\5\6\4\2\61%\3\2\2\2\61"+
		"&\3\2\2\2\61\'\3\2\2\2\61(\3\2\2\2\61)\3\2\2\2\61*\3\2\2\2\61+\3\2\2\2"+
		"\61,\3\2\2\2\61-\3\2\2\2\61.\3\2\2\2\61/\3\2\2\2\61\60\3\2\2\2\62\5\3"+
		"\2\2\2\63<\7\3\2\2\649\5\b\5\2\65\66\7\4\2\2\668\5\b\5\2\67\65\3\2\2\2"+
		"8;\3\2\2\29\67\3\2\2\29:\3\2\2\2:=\3\2\2\2;9\3\2\2\2<\64\3\2\2\2<=\3\2"+
		"\2\2=?\3\2\2\2>@\7\4\2\2?>\3\2\2\2?@\3\2\2\2@A\3\2\2\2AB\7\5\2\2B\7\3"+
		"\2\2\2CD\5\36\20\2DE\7\6\2\2EF\5\4\3\2F\t\3\2\2\2GP\7\7\2\2HM\5\4\3\2"+
		"IJ\7\4\2\2JL\5\4\3\2KI\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2NQ\3\2\2\2"+
		"OM\3\2\2\2PH\3\2\2\2PQ\3\2\2\2QS\3\2\2\2RT\7\4\2\2SR\3\2\2\2ST\3\2\2\2"+
		"TU\3\2\2\2UV\7\b\2\2V\13\3\2\2\2WX\7\7\2\2XY\7\t\2\2Ye\7\n\2\2Z_\5\30"+
		"\r\2[\\\7\4\2\2\\^\5\30\r\2][\3\2\2\2^a\3\2\2\2_]\3\2\2\2_`\3\2\2\2`c"+
		"\3\2\2\2a_\3\2\2\2bd\7\4\2\2cb\3\2\2\2cd\3\2\2\2df\3\2\2\2eZ\3\2\2\2e"+
		"f\3\2\2\2fg\3\2\2\2gh\7\b\2\2h\r\3\2\2\2ij\7\7\2\2jk\7\13\2\2kw\7\n\2"+
		"\2lq\5\34\17\2mn\7\4\2\2np\5\34\17\2om\3\2\2\2ps\3\2\2\2qo\3\2\2\2qr\3"+
		"\2\2\2ru\3\2\2\2sq\3\2\2\2tv\7\4\2\2ut\3\2\2\2uv\3\2\2\2vx\3\2\2\2wl\3"+
		"\2\2\2wx\3\2\2\2xy\3\2\2\2yz\7\b\2\2z\17\3\2\2\2{|\7\7\2\2|}\7\f\2\2}"+
		"\u0089\7\n\2\2~\u0083\5\26\f\2\177\u0080\7\4\2\2\u0080\u0082\5\26\f\2"+
		"\u0081\177\3\2\2\2\u0082\u0085\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084"+
		"\3\2\2\2\u0084\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0086\u0088\7\4\2\2\u0087"+
		"\u0086\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u008a\3\2\2\2\u0089~\3\2\2\2"+
		"\u0089\u008a\3\2\2\2\u008a\u008b\3\2\2\2\u008b\u008c\7\b\2\2\u008c\21"+
		"\3\2\2\2\u008d\u008e\7\22\2\2\u008e\23\3\2\2\2\u008f\u0090\7\21\2\2\u0090"+
		"\25\3\2\2\2\u0091\u0092\7\24\2\2\u0092\27\3\2\2\2\u0093\u0094\t\2\2\2"+
		"\u0094\31\3\2\2\2\u0095\u0096\7\26\2\2\u0096\33\3\2\2\2\u0097\u0098\7"+
		"\23\2\2\u0098\35\3\2\2\2\u0099\u009d\5 \21\2\u009a\u009d\7\r\2\2\u009b"+
		"\u009d\7\16\2\2\u009c\u0099\3\2\2\2\u009c\u009a\3\2\2\2\u009c\u009b\3"+
		"\2\2\2\u009d\37\3\2\2\2\u009e\u00a0\7\27\2\2\u009f\u009e\3\2\2\2\u00a0"+
		"\u00a1\3\2\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a2\3\2\2\2\u00a2!\3\2\2\2"+
		"\24\619<?MPS_cequw\u0083\u0087\u0089\u009c\u00a1";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}