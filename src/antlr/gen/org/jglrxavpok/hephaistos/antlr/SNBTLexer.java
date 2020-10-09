// Generated from C:/Users/jglrxavpok/Documents/Programmation/MCModding/NBTMCALib/src/antlr/resources\SNBT.g4 by ANTLR 4.8
package org.jglrxavpok.hephaistos.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SNBTLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, DoubleQuoteText=16, 
		SingleQuoteText=17, BOOLEAN=18, NEGATIVE_SIGN=19, INTEGER=20, LONG=21, 
		BYTE=22, SHORT=23, IDENTIFIER_LETTERS=24, WS=25;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "DoubleQuoteText", 
			"SingleQuoteText", "BOOLEAN", "NEGATIVE_SIGN", "INTEGER", "LONG", "BYTE", 
			"SHORT", "IDENTIFIER_LETTERS", "WS"
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


	public SNBTLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SNBT.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\33\u009c\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3"+
		"\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20"+
		"\3\20\3\21\3\21\3\21\3\21\7\21X\n\21\f\21\16\21[\13\21\3\21\3\21\3\22"+
		"\3\22\3\22\3\22\7\22c\n\22\f\22\16\22f\13\22\3\22\3\22\3\23\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\5\23s\n\23\3\24\3\24\3\25\6\25x\n\25\r"+
		"\25\16\25y\3\26\6\26}\n\26\r\26\16\26~\3\26\3\26\3\27\6\27\u0084\n\27"+
		"\r\27\16\27\u0085\3\27\3\27\3\30\6\30\u008b\n\30\r\30\16\30\u008c\3\30"+
		"\3\30\3\31\6\31\u0092\n\31\r\31\16\31\u0093\3\32\6\32\u0097\n\32\r\32"+
		"\16\32\u0098\3\32\3\32\2\2\33\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13"+
		"\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61"+
		"\32\63\33\3\2\n\3\2$$\3\2))\3\2\62;\4\2NNnn\4\2DDdd\4\2UUuu\6\2\62;C\\"+
		"aac|\5\2\13\f\17\17\"\"\2\u00a6\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2"+
		"\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\3\65\3\2\2"+
		"\2\5\67\3\2\2\2\79\3\2\2\2\t;\3\2\2\2\13=\3\2\2\2\r?\3\2\2\2\17A\3\2\2"+
		"\2\21C\3\2\2\2\23E\3\2\2\2\25G\3\2\2\2\27I\3\2\2\2\31K\3\2\2\2\33M\3\2"+
		"\2\2\35O\3\2\2\2\37Q\3\2\2\2!S\3\2\2\2#^\3\2\2\2%r\3\2\2\2\'t\3\2\2\2"+
		")w\3\2\2\2+|\3\2\2\2-\u0083\3\2\2\2/\u008a\3\2\2\2\61\u0091\3\2\2\2\63"+
		"\u0096\3\2\2\2\65\66\7}\2\2\66\4\3\2\2\2\678\7.\2\28\6\3\2\2\29:\7\177"+
		"\2\2:\b\3\2\2\2;<\7<\2\2<\n\3\2\2\2=>\7]\2\2>\f\3\2\2\2?@\7_\2\2@\16\3"+
		"\2\2\2AB\7D\2\2B\20\3\2\2\2CD\7=\2\2D\22\3\2\2\2EF\7K\2\2F\24\3\2\2\2"+
		"GH\7N\2\2H\26\3\2\2\2IJ\7\60\2\2J\30\3\2\2\2KL\7f\2\2L\32\3\2\2\2MN\7"+
		"F\2\2N\34\3\2\2\2OP\7h\2\2P\36\3\2\2\2QR\7H\2\2R \3\2\2\2SY\7$\2\2TX\n"+
		"\2\2\2UV\7^\2\2VX\7$\2\2WT\3\2\2\2WU\3\2\2\2X[\3\2\2\2YW\3\2\2\2YZ\3\2"+
		"\2\2Z\\\3\2\2\2[Y\3\2\2\2\\]\7$\2\2]\"\3\2\2\2^d\7)\2\2_c\n\3\2\2`a\7"+
		"^\2\2ac\7)\2\2b_\3\2\2\2b`\3\2\2\2cf\3\2\2\2db\3\2\2\2de\3\2\2\2eg\3\2"+
		"\2\2fd\3\2\2\2gh\7)\2\2h$\3\2\2\2ij\7h\2\2jk\7c\2\2kl\7n\2\2lm\7u\2\2"+
		"ms\7g\2\2no\7v\2\2op\7t\2\2pq\7w\2\2qs\7g\2\2ri\3\2\2\2rn\3\2\2\2s&\3"+
		"\2\2\2tu\7/\2\2u(\3\2\2\2vx\t\4\2\2wv\3\2\2\2xy\3\2\2\2yw\3\2\2\2yz\3"+
		"\2\2\2z*\3\2\2\2{}\t\4\2\2|{\3\2\2\2}~\3\2\2\2~|\3\2\2\2~\177\3\2\2\2"+
		"\177\u0080\3\2\2\2\u0080\u0081\t\5\2\2\u0081,\3\2\2\2\u0082\u0084\t\4"+
		"\2\2\u0083\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0083\3\2\2\2\u0085"+
		"\u0086\3\2\2\2\u0086\u0087\3\2\2\2\u0087\u0088\t\6\2\2\u0088.\3\2\2\2"+
		"\u0089\u008b\t\4\2\2\u008a\u0089\3\2\2\2\u008b\u008c\3\2\2\2\u008c\u008a"+
		"\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008e\3\2\2\2\u008e\u008f\t\7\2\2\u008f"+
		"\60\3\2\2\2\u0090\u0092\t\b\2\2\u0091\u0090\3\2\2\2\u0092\u0093\3\2\2"+
		"\2\u0093\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\62\3\2\2\2\u0095\u0097"+
		"\t\t\2\2\u0096\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0096\3\2\2\2\u0098"+
		"\u0099\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009b\b\32\2\2\u009b\64\3\2\2"+
		"\2\16\2WYbdry~\u0085\u008c\u0093\u0098\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}