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
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, DoubleQuoteText=16, 
		SingleQuoteText=17, BOOLEAN=18, NEGATIVE_SIGN=19, INTEGER=20, LONG=21, 
		BYTE=22, SHORT=23, IDENTIFIER_LETTERS=24, WS=25;
	public static final int
		RULE_snbt = 0, RULE_element = 1, RULE_compound = 2, RULE_namedElement = 3, 
		RULE_list = 4, RULE_byteArray = 5, RULE_intArray = 6, RULE_longArray = 7, 
		RULE_doubleNBT = 8, RULE_floatNBT = 9, RULE_longNBT = 10, RULE_byteNBT = 11, 
		RULE_shortNBT = 12, RULE_intNBT = 13, RULE_stringNBT = 14, RULE_identifier = 15, 
		RULE_integralNumber = 16;
	private static String[] makeRuleNames() {
		return new String[] {
			"snbt", "element", "compound", "namedElement", "list", "byteArray", "intArray", 
			"longArray", "doubleNBT", "floatNBT", "longNBT", "byteNBT", "shortNBT", 
			"intNBT", "stringNBT", "identifier", "integralNumber"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'{'", "','", "'}'", "':'", "'['", "']'", "'B'", "';'", "'I'", 
			"'L'", "'.'", "'d'", "'D'", "'f'", "'F'", null, null, null, "'-'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, "DoubleQuoteText", "SingleQuoteText", "BOOLEAN", 
			"NEGATIVE_SIGN", "INTEGER", "LONG", "BYTE", "SHORT", "IDENTIFIER_LETTERS", 
			"WS"
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
			setState(34);
			element();
			setState(35);
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
		public DoubleNBTContext doubleNBT() {
			return getRuleContext(DoubleNBTContext.class,0);
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
			setState(49);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(37);
				byteNBT();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(38);
				floatNBT();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(39);
				doubleNBT();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(40);
				shortNBT();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(41);
				longNBT();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(42);
				intNBT();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(43);
				stringNBT();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(44);
				byteArray();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(45);
				intArray();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(46);
				longArray();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(47);
				list();
				}
				break;
			case 12:
				enterOuterAlt(_localctx, 12);
				{
				setState(48);
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
			setState(51);
			match(T__0);
			setState(60);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DoubleQuoteText) | (1L << SingleQuoteText) | (1L << IDENTIFIER_LETTERS))) != 0)) {
				{
				setState(52);
				namedElement();
				setState(57);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(53);
						match(T__1);
						setState(54);
						namedElement();
						}
						} 
					}
					setState(59);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				}
				}
			}

			setState(63);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(62);
				match(T__1);
				}
			}

			setState(65);
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
			setState(67);
			((NamedElementContext)_localctx).name = stringNBT();
			setState(68);
			match(T__3);
			setState(69);
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
			setState(71);
			match(T__4);
			setState(80);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__0) | (1L << T__4) | (1L << DoubleQuoteText) | (1L << SingleQuoteText) | (1L << BOOLEAN) | (1L << NEGATIVE_SIGN) | (1L << INTEGER) | (1L << LONG) | (1L << BYTE) | (1L << SHORT) | (1L << IDENTIFIER_LETTERS))) != 0)) {
				{
				setState(72);
				element();
				setState(77);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(73);
						match(T__1);
						setState(74);
						element();
						}
						} 
					}
					setState(79);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
				}
				}
			}

			setState(83);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(82);
				match(T__1);
				}
			}

			setState(85);
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
			setState(87);
			match(T__4);
			setState(88);
			match(T__6);
			setState(89);
			match(T__7);
			setState(101);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==BOOLEAN || _la==BYTE) {
				{
				setState(90);
				byteNBT();
				setState(95);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(91);
						match(T__1);
						setState(92);
						byteNBT();
						}
						} 
					}
					setState(97);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				}
				setState(99);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(98);
					match(T__1);
					}
				}

				}
			}

			setState(103);
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
			setState(105);
			match(T__4);
			setState(106);
			match(T__8);
			setState(107);
			match(T__7);
			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NEGATIVE_SIGN || _la==INTEGER) {
				{
				setState(108);
				intNBT();
				setState(113);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(109);
						match(T__1);
						setState(110);
						intNBT();
						}
						} 
					}
					setState(115);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,10,_ctx);
				}
				setState(117);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(116);
					match(T__1);
					}
				}

				}
			}

			setState(121);
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
			setState(123);
			match(T__4);
			setState(124);
			match(T__9);
			setState(125);
			match(T__7);
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LONG) {
				{
				setState(126);
				longNBT();
				setState(131);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(127);
						match(T__1);
						setState(128);
						longNBT();
						}
						} 
					}
					setState(133);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,13,_ctx);
				}
				setState(135);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__1) {
					{
					setState(134);
					match(T__1);
					}
				}

				}
			}

			setState(139);
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
		public IntegralNumberContext integerPart;
		public Token fractionalPart;
		public IntegralNumberContext integralNumber() {
			return getRuleContext(IntegralNumberContext.class,0);
		}
		public TerminalNode INTEGER() { return getToken(SNBTParser.INTEGER, 0); }
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			((DoubleNBTContext)_localctx).integerPart = integralNumber();
			setState(144);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(142);
				match(T__10);
				setState(143);
				((DoubleNBTContext)_localctx).fractionalPart = match(INTEGER);
				}
			}

			setState(146);
			_la = _input.LA(1);
			if ( !(_la==T__11 || _la==T__12) ) {
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

	public static class FloatNBTContext extends ParserRuleContext {
		public IntegralNumberContext integerPart;
		public Token fractionalPart;
		public IntegralNumberContext integralNumber() {
			return getRuleContext(IntegralNumberContext.class,0);
		}
		public TerminalNode INTEGER() { return getToken(SNBTParser.INTEGER, 0); }
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			((FloatNBTContext)_localctx).integerPart = integralNumber();
			setState(151);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(149);
				match(T__10);
				setState(150);
				((FloatNBTContext)_localctx).fractionalPart = match(INTEGER);
				}
			}

			setState(153);
			_la = _input.LA(1);
			if ( !(_la==T__13 || _la==T__14) ) {
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
			setState(155);
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
			setState(157);
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
			setState(159);
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
		public IntegralNumberContext integralNumber() {
			return getRuleContext(IntegralNumberContext.class,0);
		}
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
			setState(161);
			integralNumber();
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
			setState(166);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IDENTIFIER_LETTERS:
				enterOuterAlt(_localctx, 1);
				{
				setState(163);
				identifier();
				}
				break;
			case DoubleQuoteText:
				enterOuterAlt(_localctx, 2);
				{
				setState(164);
				match(DoubleQuoteText);
				}
				break;
			case SingleQuoteText:
				enterOuterAlt(_localctx, 3);
				{
				setState(165);
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
			setState(169); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(168);
				match(IDENTIFIER_LETTERS);
				}
				}
				setState(171); 
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

	public static class IntegralNumberContext extends ParserRuleContext {
		public TerminalNode INTEGER() { return getToken(SNBTParser.INTEGER, 0); }
		public TerminalNode NEGATIVE_SIGN() { return getToken(SNBTParser.NEGATIVE_SIGN, 0); }
		public IntegralNumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_integralNumber; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).enterIntegralNumber(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof SNBTListener ) ((SNBTListener)listener).exitIntegralNumber(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof SNBTVisitor ) return ((SNBTVisitor<? extends T>)visitor).visitIntegralNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IntegralNumberContext integralNumber() throws RecognitionException {
		IntegralNumberContext _localctx = new IntegralNumberContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_integralNumber);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(174);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==NEGATIVE_SIGN) {
				{
				setState(173);
				match(NEGATIVE_SIGN);
				}
			}

			setState(176);
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\33\u00b5\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5\3\64\n"+
		"\3\3\4\3\4\3\4\3\4\7\4:\n\4\f\4\16\4=\13\4\5\4?\n\4\3\4\5\4B\n\4\3\4\3"+
		"\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\7\6N\n\6\f\6\16\6Q\13\6\5\6S\n\6\3"+
		"\6\5\6V\n\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\7\7`\n\7\f\7\16\7c\13\7\3"+
		"\7\5\7f\n\7\5\7h\n\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b\7\br\n\b\f\b\16\b"+
		"u\13\b\3\b\5\bx\n\b\5\bz\n\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\7\t\u0084"+
		"\n\t\f\t\16\t\u0087\13\t\3\t\5\t\u008a\n\t\5\t\u008c\n\t\3\t\3\t\3\n\3"+
		"\n\3\n\5\n\u0093\n\n\3\n\3\n\3\13\3\13\3\13\5\13\u009a\n\13\3\13\3\13"+
		"\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\5\20\u00a9\n\20\3"+
		"\21\6\21\u00ac\n\21\r\21\16\21\u00ad\3\22\5\22\u00b1\n\22\3\22\3\22\3"+
		"\22\2\2\23\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"\2\5\3\2\16\17\3\2"+
		"\20\21\4\2\24\24\30\30\2\u00c3\2$\3\2\2\2\4\63\3\2\2\2\6\65\3\2\2\2\b"+
		"E\3\2\2\2\nI\3\2\2\2\fY\3\2\2\2\16k\3\2\2\2\20}\3\2\2\2\22\u008f\3\2\2"+
		"\2\24\u0096\3\2\2\2\26\u009d\3\2\2\2\30\u009f\3\2\2\2\32\u00a1\3\2\2\2"+
		"\34\u00a3\3\2\2\2\36\u00a8\3\2\2\2 \u00ab\3\2\2\2\"\u00b0\3\2\2\2$%\5"+
		"\4\3\2%&\7\2\2\3&\3\3\2\2\2\'\64\5\30\r\2(\64\5\24\13\2)\64\5\22\n\2*"+
		"\64\5\32\16\2+\64\5\26\f\2,\64\5\34\17\2-\64\5\36\20\2.\64\5\f\7\2/\64"+
		"\5\16\b\2\60\64\5\20\t\2\61\64\5\n\6\2\62\64\5\6\4\2\63\'\3\2\2\2\63("+
		"\3\2\2\2\63)\3\2\2\2\63*\3\2\2\2\63+\3\2\2\2\63,\3\2\2\2\63-\3\2\2\2\63"+
		".\3\2\2\2\63/\3\2\2\2\63\60\3\2\2\2\63\61\3\2\2\2\63\62\3\2\2\2\64\5\3"+
		"\2\2\2\65>\7\3\2\2\66;\5\b\5\2\678\7\4\2\28:\5\b\5\29\67\3\2\2\2:=\3\2"+
		"\2\2;9\3\2\2\2;<\3\2\2\2<?\3\2\2\2=;\3\2\2\2>\66\3\2\2\2>?\3\2\2\2?A\3"+
		"\2\2\2@B\7\4\2\2A@\3\2\2\2AB\3\2\2\2BC\3\2\2\2CD\7\5\2\2D\7\3\2\2\2EF"+
		"\5\36\20\2FG\7\6\2\2GH\5\4\3\2H\t\3\2\2\2IR\7\7\2\2JO\5\4\3\2KL\7\4\2"+
		"\2LN\5\4\3\2MK\3\2\2\2NQ\3\2\2\2OM\3\2\2\2OP\3\2\2\2PS\3\2\2\2QO\3\2\2"+
		"\2RJ\3\2\2\2RS\3\2\2\2SU\3\2\2\2TV\7\4\2\2UT\3\2\2\2UV\3\2\2\2VW\3\2\2"+
		"\2WX\7\b\2\2X\13\3\2\2\2YZ\7\7\2\2Z[\7\t\2\2[g\7\n\2\2\\a\5\30\r\2]^\7"+
		"\4\2\2^`\5\30\r\2_]\3\2\2\2`c\3\2\2\2a_\3\2\2\2ab\3\2\2\2be\3\2\2\2ca"+
		"\3\2\2\2df\7\4\2\2ed\3\2\2\2ef\3\2\2\2fh\3\2\2\2g\\\3\2\2\2gh\3\2\2\2"+
		"hi\3\2\2\2ij\7\b\2\2j\r\3\2\2\2kl\7\7\2\2lm\7\13\2\2my\7\n\2\2ns\5\34"+
		"\17\2op\7\4\2\2pr\5\34\17\2qo\3\2\2\2ru\3\2\2\2sq\3\2\2\2st\3\2\2\2tw"+
		"\3\2\2\2us\3\2\2\2vx\7\4\2\2wv\3\2\2\2wx\3\2\2\2xz\3\2\2\2yn\3\2\2\2y"+
		"z\3\2\2\2z{\3\2\2\2{|\7\b\2\2|\17\3\2\2\2}~\7\7\2\2~\177\7\f\2\2\177\u008b"+
		"\7\n\2\2\u0080\u0085\5\26\f\2\u0081\u0082\7\4\2\2\u0082\u0084\5\26\f\2"+
		"\u0083\u0081\3\2\2\2\u0084\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0086"+
		"\3\2\2\2\u0086\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0088\u008a\7\4\2\2\u0089"+
		"\u0088\3\2\2\2\u0089\u008a\3\2\2\2\u008a\u008c\3\2\2\2\u008b\u0080\3\2"+
		"\2\2\u008b\u008c\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008e\7\b\2\2\u008e"+
		"\21\3\2\2\2\u008f\u0092\5\"\22\2\u0090\u0091\7\r\2\2\u0091\u0093\7\26"+
		"\2\2\u0092\u0090\3\2\2\2\u0092\u0093\3\2\2\2\u0093\u0094\3\2\2\2\u0094"+
		"\u0095\t\2\2\2\u0095\23\3\2\2\2\u0096\u0099\5\"\22\2\u0097\u0098\7\r\2"+
		"\2\u0098\u009a\7\26\2\2\u0099\u0097\3\2\2\2\u0099\u009a\3\2\2\2\u009a"+
		"\u009b\3\2\2\2\u009b\u009c\t\3\2\2\u009c\25\3\2\2\2\u009d\u009e\7\27\2"+
		"\2\u009e\27\3\2\2\2\u009f\u00a0\t\4\2\2\u00a0\31\3\2\2\2\u00a1\u00a2\7"+
		"\31\2\2\u00a2\33\3\2\2\2\u00a3\u00a4\5\"\22\2\u00a4\35\3\2\2\2\u00a5\u00a9"+
		"\5 \21\2\u00a6\u00a9\7\22\2\2\u00a7\u00a9\7\23\2\2\u00a8\u00a5\3\2\2\2"+
		"\u00a8\u00a6\3\2\2\2\u00a8\u00a7\3\2\2\2\u00a9\37\3\2\2\2\u00aa\u00ac"+
		"\7\32\2\2\u00ab\u00aa\3\2\2\2\u00ac\u00ad\3\2\2\2\u00ad\u00ab\3\2\2\2"+
		"\u00ad\u00ae\3\2\2\2\u00ae!\3\2\2\2\u00af\u00b1\7\25\2\2\u00b0\u00af\3"+
		"\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b3\7\26\2\2\u00b3"+
		"#\3\2\2\2\27\63;>AORUaegswy\u0085\u0089\u008b\u0092\u0099\u00a8\u00ad"+
		"\u00b0";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}