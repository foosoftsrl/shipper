// Generated from Config.g4 by ANTLR 4.9.1

package com.logstash;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ConfigParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ConfigVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ConfigParser#config}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitConfig(ConfigParser.ConfigContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#stage_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStage_declaration(ConfigParser.Stage_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#stage_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStage_definition(ConfigParser.Stage_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#plugin_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlugin_declaration(ConfigParser.Plugin_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#plugin_definition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlugin_definition(ConfigParser.Plugin_definitionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#plugin_attribute}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlugin_attribute(ConfigParser.Plugin_attributeContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#plugin_attribute_value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlugin_attribute_value(ConfigParser.Plugin_attribute_valueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#stage_condition}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStage_condition(ConfigParser.Stage_conditionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#logical_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogical_expression(ConfigParser.Logical_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#negative_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegative_expression(ConfigParser.Negative_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#compare_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCompare_expression(ConfigParser.Compare_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#in_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIn_expression(ConfigParser.In_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#match_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatch_expression(ConfigParser.Match_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#rvalue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRvalue(ConfigParser.RvalueContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#fieldref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldref(ConfigParser.FieldrefContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#fieldref_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFieldref_element(ConfigParser.Fieldref_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#array}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray(ConfigParser.ArrayContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#array_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArray_element(ConfigParser.Array_elementContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#hash}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash(ConfigParser.HashContext ctx);
	/**
	 * Visit a parse tree produced by {@link ConfigParser#hash_element}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHash_element(ConfigParser.Hash_elementContext ctx);
}