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
		T__9=10, DoubleQuoteText=11, SingleQuoteText=12, BOOLEAN=13, NEGATIVE_SIGN=14, 
		FLOAT=15, DOUBLE=16, INTEGER=17, LONG=18, BYTE=19, SHORT=20, WS=21, IDENTIFIER_LETTERS=22;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "DoubleQuoteText", "SingleQuoteText", "BOOLEAN", "NEGATIVE_SIGN", 
			"FLOAT", "DOUBLE", "INTEGER", "LONG", "BYTE", "SHORT", "WS", "IDENTIFIER_LETTERS"
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
			"LONG", "BYTE", "SHORT", "WS", "IDENTIFIER_LETTERS"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\30\u00ed\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\3\2\3\2\3\3\3"+
		"\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3"+
		"\f\3\f\3\f\3\f\7\fH\n\f\f\f\16\fK\13\f\3\f\3\f\3\r\3\r\3\r\3\r\7\rS\n"+
		"\r\f\r\16\rV\13\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\5\16c\n\16\3\17\3\17\3\20\5\20h\n\20\3\20\7\20k\n\20\f\20\16\20n\13\20"+
		"\3\20\3\20\7\20r\n\20\f\20\16\20u\13\20\5\20w\n\20\3\20\6\20z\n\20\r\20"+
		"\16\20{\5\20~\n\20\3\20\3\20\3\21\5\21\u0083\n\21\3\21\7\21\u0086\n\21"+
		"\f\21\16\21\u0089\13\21\3\21\3\21\7\21\u008d\n\21\f\21\16\21\u0090\13"+
		"\21\5\21\u0092\n\21\3\21\6\21\u0095\n\21\r\21\16\21\u0096\5\21\u0099\n"+
		"\21\3\21\3\21\5\21\u009d\n\21\3\21\7\21\u00a0\n\21\f\21\16\21\u00a3\13"+
		"\21\3\21\3\21\6\21\u00a7\n\21\r\21\16\21\u00a8\3\21\5\21\u00ac\n\21\3"+
		"\21\6\21\u00af\n\21\r\21\16\21\u00b0\3\21\3\21\7\21\u00b5\n\21\f\21\16"+
		"\21\u00b8\13\21\5\21\u00ba\n\21\3\22\5\22\u00bd\n\22\3\22\6\22\u00c0\n"+
		"\22\r\22\16\22\u00c1\3\23\5\23\u00c5\n\23\3\23\6\23\u00c8\n\23\r\23\16"+
		"\23\u00c9\3\23\3\23\3\24\5\24\u00cf\n\24\3\24\6\24\u00d2\n\24\r\24\16"+
		"\24\u00d3\3\24\3\24\3\25\5\25\u00d9\n\25\3\25\6\25\u00dc\n\25\r\25\16"+
		"\25\u00dd\3\25\3\25\3\26\6\26\u00e3\n\26\r\26\16\26\u00e4\3\26\3\26\3"+
		"\27\6\27\u00ea\n\27\r\27\16\27\u00eb\2\2\30\3\3\5\4\7\5\t\6\13\7\r\b\17"+
		"\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+"+
		"\27-\30\3\2\f\3\2$$\3\2))\3\2\62;\4\2HHhh\4\2FFff\4\2NNnn\4\2DDdd\4\2"+
		"UUuu\5\2\13\f\17\17\"\"\7\2\"\"\62;C\\aac|\2\u010f\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3"+
		"\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\3/\3\2\2\2\5\61\3\2\2\2\7\63\3"+
		"\2\2\2\t\65\3\2\2\2\13\67\3\2\2\2\r9\3\2\2\2\17;\3\2\2\2\21=\3\2\2\2\23"+
		"?\3\2\2\2\25A\3\2\2\2\27C\3\2\2\2\31N\3\2\2\2\33b\3\2\2\2\35d\3\2\2\2"+
		"\37g\3\2\2\2!\u00b9\3\2\2\2#\u00bc\3\2\2\2%\u00c4\3\2\2\2\'\u00ce\3\2"+
		"\2\2)\u00d8\3\2\2\2+\u00e2\3\2\2\2-\u00e9\3\2\2\2/\60\7}\2\2\60\4\3\2"+
		"\2\2\61\62\7.\2\2\62\6\3\2\2\2\63\64\7\177\2\2\64\b\3\2\2\2\65\66\7<\2"+
		"\2\66\n\3\2\2\2\678\7]\2\28\f\3\2\2\29:\7_\2\2:\16\3\2\2\2;<\7D\2\2<\20"+
		"\3\2\2\2=>\7=\2\2>\22\3\2\2\2?@\7K\2\2@\24\3\2\2\2AB\7N\2\2B\26\3\2\2"+
		"\2CI\7$\2\2DH\n\2\2\2EF\7^\2\2FH\7$\2\2GD\3\2\2\2GE\3\2\2\2HK\3\2\2\2"+
		"IG\3\2\2\2IJ\3\2\2\2JL\3\2\2\2KI\3\2\2\2LM\7$\2\2M\30\3\2\2\2NT\7)\2\2"+
		"OS\n\3\2\2PQ\7^\2\2QS\7)\2\2RO\3\2\2\2RP\3\2\2\2SV\3\2\2\2TR\3\2\2\2T"+
		"U\3\2\2\2UW\3\2\2\2VT\3\2\2\2WX\7)\2\2X\32\3\2\2\2YZ\7h\2\2Z[\7c\2\2["+
		"\\\7n\2\2\\]\7u\2\2]c\7g\2\2^_\7v\2\2_`\7t\2\2`a\7w\2\2ac\7g\2\2bY\3\2"+
		"\2\2b^\3\2\2\2c\34\3\2\2\2de\7/\2\2e\36\3\2\2\2fh\7/\2\2gf\3\2\2\2gh\3"+
		"\2\2\2h}\3\2\2\2ik\t\4\2\2ji\3\2\2\2kn\3\2\2\2lj\3\2\2\2lm\3\2\2\2mv\3"+
		"\2\2\2nl\3\2\2\2os\7\60\2\2pr\t\4\2\2qp\3\2\2\2ru\3\2\2\2sq\3\2\2\2st"+
		"\3\2\2\2tw\3\2\2\2us\3\2\2\2vo\3\2\2\2vw\3\2\2\2w~\3\2\2\2xz\t\4\2\2y"+
		"x\3\2\2\2z{\3\2\2\2{y\3\2\2\2{|\3\2\2\2|~\3\2\2\2}l\3\2\2\2}y\3\2\2\2"+
		"~\177\3\2\2\2\177\u0080\t\5\2\2\u0080 \3\2\2\2\u0081\u0083\7/\2\2\u0082"+
		"\u0081\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0098\3\2\2\2\u0084\u0086\t\4"+
		"\2\2\u0085\u0084\3\2\2\2\u0086\u0089\3\2\2\2\u0087\u0085\3\2\2\2\u0087"+
		"\u0088\3\2\2\2\u0088\u0091\3\2\2\2\u0089\u0087\3\2\2\2\u008a\u008e\7\60"+
		"\2\2\u008b\u008d\t\4\2\2\u008c\u008b\3\2\2\2\u008d\u0090\3\2\2\2\u008e"+
		"\u008c\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0092\3\2\2\2\u0090\u008e\3\2"+
		"\2\2\u0091\u008a\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0099\3\2\2\2\u0093"+
		"\u0095\t\4\2\2\u0094\u0093\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0094\3\2"+
		"\2\2\u0096\u0097\3\2\2\2\u0097\u0099\3\2\2\2\u0098\u0087\3\2\2\2\u0098"+
		"\u0094\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u00ba\t\6\2\2\u009b\u009d\7/"+
		"\2\2\u009c\u009b\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u00a1\3\2\2\2\u009e"+
		"\u00a0\t\4\2\2\u009f\u009e\3\2\2\2\u00a0\u00a3\3\2\2\2\u00a1\u009f\3\2"+
		"\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a4\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a4"+
		"\u00a6\7\60\2\2\u00a5\u00a7\t\4\2\2\u00a6\u00a5\3\2\2\2\u00a7\u00a8\3"+
		"\2\2\2\u00a8\u00a6\3\2\2\2\u00a8\u00a9\3\2\2\2\u00a9\u00ba\3\2\2\2\u00aa"+
		"\u00ac\7/\2\2\u00ab\u00aa\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ae\3\2"+
		"\2\2\u00ad\u00af\t\4\2\2\u00ae\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0"+
		"\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b6\7\60"+
		"\2\2\u00b3\u00b5\t\4\2\2\u00b4\u00b3\3\2\2\2\u00b5\u00b8\3\2\2\2\u00b6"+
		"\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00ba\3\2\2\2\u00b8\u00b6\3\2"+
		"\2\2\u00b9\u0082\3\2\2\2\u00b9\u009c\3\2\2\2\u00b9\u00ab\3\2\2\2\u00ba"+
		"\"\3\2\2\2\u00bb\u00bd\7/\2\2\u00bc\u00bb\3\2\2\2\u00bc\u00bd\3\2\2\2"+
		"\u00bd\u00bf\3\2\2\2\u00be\u00c0\t\4\2\2\u00bf\u00be\3\2\2\2\u00c0\u00c1"+
		"\3\2\2\2\u00c1\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2$\3\2\2\2\u00c3"+
		"\u00c5\7/\2\2\u00c4\u00c3\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c7\3\2"+
		"\2\2\u00c6\u00c8\t\4\2\2\u00c7\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9"+
		"\u00c7\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cc\t\7"+
		"\2\2\u00cc&\3\2\2\2\u00cd\u00cf\7/\2\2\u00ce\u00cd\3\2\2\2\u00ce\u00cf"+
		"\3\2\2\2\u00cf\u00d1\3\2\2\2\u00d0\u00d2\t\4\2\2\u00d1\u00d0\3\2\2\2\u00d2"+
		"\u00d3\3\2\2\2\u00d3\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d5\3\2"+
		"\2\2\u00d5\u00d6\t\b\2\2\u00d6(\3\2\2\2\u00d7\u00d9\7/\2\2\u00d8\u00d7"+
		"\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00db\3\2\2\2\u00da\u00dc\t\4\2\2\u00db"+
		"\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00db\3\2\2\2\u00dd\u00de\3\2"+
		"\2\2\u00de\u00df\3\2\2\2\u00df\u00e0\t\t\2\2\u00e0*\3\2\2\2\u00e1\u00e3"+
		"\t\n\2\2\u00e2\u00e1\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e2\3\2\2\2\u00e4"+
		"\u00e5\3\2\2\2\u00e5\u00e6\3\2\2\2\u00e6\u00e7\b\26\2\2\u00e7,\3\2\2\2"+
		"\u00e8\u00ea\t\13\2\2\u00e9\u00e8\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb\u00e9"+
		"\3\2\2\2\u00eb\u00ec\3\2\2\2\u00ec.\3\2\2\2%\2GIRTbglsv{}\u0082\u0087"+
		"\u008e\u0091\u0096\u0098\u009c\u00a1\u00a8\u00ab\u00b0\u00b6\u00b9\u00bc"+
		"\u00c1\u00c4\u00c9\u00ce\u00d3\u00d8\u00dd\u00e4\u00eb\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}