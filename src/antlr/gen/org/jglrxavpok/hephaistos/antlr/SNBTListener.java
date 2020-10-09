// Generated from C:/Users/jglrxavpok/Documents/Programmation/MCModding/NBTMCALib/src/antlr/resources\SNBT.g4 by ANTLR 4.8
package org.jglrxavpok.hephaistos.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link SNBTParser}.
 */
public interface SNBTListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link SNBTParser#snbt}.
	 * @param ctx the parse tree
	 */
	void enterSnbt(SNBTParser.SnbtContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#snbt}.
	 * @param ctx the parse tree
	 */
	void exitSnbt(SNBTParser.SnbtContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#element}.
	 * @param ctx the parse tree
	 */
	void enterElement(SNBTParser.ElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#element}.
	 * @param ctx the parse tree
	 */
	void exitElement(SNBTParser.ElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#compound}.
	 * @param ctx the parse tree
	 */
	void enterCompound(SNBTParser.CompoundContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#compound}.
	 * @param ctx the parse tree
	 */
	void exitCompound(SNBTParser.CompoundContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#namedElement}.
	 * @param ctx the parse tree
	 */
	void enterNamedElement(SNBTParser.NamedElementContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#namedElement}.
	 * @param ctx the parse tree
	 */
	void exitNamedElement(SNBTParser.NamedElementContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#list}.
	 * @param ctx the parse tree
	 */
	void enterList(SNBTParser.ListContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#list}.
	 * @param ctx the parse tree
	 */
	void exitList(SNBTParser.ListContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#byteArray}.
	 * @param ctx the parse tree
	 */
	void enterByteArray(SNBTParser.ByteArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#byteArray}.
	 * @param ctx the parse tree
	 */
	void exitByteArray(SNBTParser.ByteArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#intArray}.
	 * @param ctx the parse tree
	 */
	void enterIntArray(SNBTParser.IntArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#intArray}.
	 * @param ctx the parse tree
	 */
	void exitIntArray(SNBTParser.IntArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#longArray}.
	 * @param ctx the parse tree
	 */
	void enterLongArray(SNBTParser.LongArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#longArray}.
	 * @param ctx the parse tree
	 */
	void exitLongArray(SNBTParser.LongArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#doubleNBT}.
	 * @param ctx the parse tree
	 */
	void enterDoubleNBT(SNBTParser.DoubleNBTContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#doubleNBT}.
	 * @param ctx the parse tree
	 */
	void exitDoubleNBT(SNBTParser.DoubleNBTContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#floatNBT}.
	 * @param ctx the parse tree
	 */
	void enterFloatNBT(SNBTParser.FloatNBTContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#floatNBT}.
	 * @param ctx the parse tree
	 */
	void exitFloatNBT(SNBTParser.FloatNBTContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#longNBT}.
	 * @param ctx the parse tree
	 */
	void enterLongNBT(SNBTParser.LongNBTContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#longNBT}.
	 * @param ctx the parse tree
	 */
	void exitLongNBT(SNBTParser.LongNBTContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#byteNBT}.
	 * @param ctx the parse tree
	 */
	void enterByteNBT(SNBTParser.ByteNBTContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#byteNBT}.
	 * @param ctx the parse tree
	 */
	void exitByteNBT(SNBTParser.ByteNBTContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#shortNBT}.
	 * @param ctx the parse tree
	 */
	void enterShortNBT(SNBTParser.ShortNBTContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#shortNBT}.
	 * @param ctx the parse tree
	 */
	void exitShortNBT(SNBTParser.ShortNBTContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#intNBT}.
	 * @param ctx the parse tree
	 */
	void enterIntNBT(SNBTParser.IntNBTContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#intNBT}.
	 * @param ctx the parse tree
	 */
	void exitIntNBT(SNBTParser.IntNBTContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#stringNBT}.
	 * @param ctx the parse tree
	 */
	void enterStringNBT(SNBTParser.StringNBTContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#stringNBT}.
	 * @param ctx the parse tree
	 */
	void exitStringNBT(SNBTParser.StringNBTContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#identifier}.
	 * @param ctx the parse tree
	 */
	void enterIdentifier(SNBTParser.IdentifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#identifier}.
	 * @param ctx the parse tree
	 */
	void exitIdentifier(SNBTParser.IdentifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link SNBTParser#integralNumber}.
	 * @param ctx the parse tree
	 */
	void enterIntegralNumber(SNBTParser.IntegralNumberContext ctx);
	/**
	 * Exit a parse tree produced by {@link SNBTParser#integralNumber}.
	 * @param ctx the parse tree
	 */
	void exitIntegralNumber(SNBTParser.IntegralNumberContext ctx);
}