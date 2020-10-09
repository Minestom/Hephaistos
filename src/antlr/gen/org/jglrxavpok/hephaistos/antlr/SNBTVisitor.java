// Generated from C:/Users/jglrxavpok/Documents/Programmation/MCModding/NBTMCALib/src/antlr/resources\SNBT.g4 by ANTLR 4.8
package org.jglrxavpok.hephaistos.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link SNBTParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface SNBTVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link SNBTParser#snbt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSnbt(SNBTParser.SnbtContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitElement(SNBTParser.ElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#compound}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompound(SNBTParser.CompoundContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#namedElement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamedElement(SNBTParser.NamedElementContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitList(SNBTParser.ListContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#byteArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitByteArray(SNBTParser.ByteArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#intArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntArray(SNBTParser.IntArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#longArray}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongArray(SNBTParser.LongArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#doubleNBT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDoubleNBT(SNBTParser.DoubleNBTContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#floatNBT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatNBT(SNBTParser.FloatNBTContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#longNBT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLongNBT(SNBTParser.LongNBTContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#byteNBT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitByteNBT(SNBTParser.ByteNBTContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#shortNBT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitShortNBT(SNBTParser.ShortNBTContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#intNBT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntNBT(SNBTParser.IntNBTContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#stringNBT}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringNBT(SNBTParser.StringNBTContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#identifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIdentifier(SNBTParser.IdentifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link SNBTParser#integralNumber}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntegralNumber(SNBTParser.IntegralNumberContext ctx);
}