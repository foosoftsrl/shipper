// Generated from Config.g4 by ANTLR 4.9.1

package com.logstash;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link ConfigParser}.
 */
public interface ConfigListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link ConfigParser#config}.
	 * @param ctx the parse tree
	 */
	void enterConfig(ConfigParser.ConfigContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#config}.
	 * @param ctx the parse tree
	 */
	void exitConfig(ConfigParser.ConfigContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#stage_declaration}.
	 * @param ctx the parse tree
	 */
	void enterStage_declaration(ConfigParser.Stage_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#stage_declaration}.
	 * @param ctx the parse tree
	 */
	void exitStage_declaration(ConfigParser.Stage_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#stage_definition}.
	 * @param ctx the parse tree
	 */
	void enterStage_definition(ConfigParser.Stage_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#stage_definition}.
	 * @param ctx the parse tree
	 */
	void exitStage_definition(ConfigParser.Stage_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#plugin_declaration}.
	 * @param ctx the parse tree
	 */
	void enterPlugin_declaration(ConfigParser.Plugin_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#plugin_declaration}.
	 * @param ctx the parse tree
	 */
	void exitPlugin_declaration(ConfigParser.Plugin_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#plugin_definition}.
	 * @param ctx the parse tree
	 */
	void enterPlugin_definition(ConfigParser.Plugin_definitionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#plugin_definition}.
	 * @param ctx the parse tree
	 */
	void exitPlugin_definition(ConfigParser.Plugin_definitionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#plugin_attribute}.
	 * @param ctx the parse tree
	 */
	void enterPlugin_attribute(ConfigParser.Plugin_attributeContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#plugin_attribute}.
	 * @param ctx the parse tree
	 */
	void exitPlugin_attribute(ConfigParser.Plugin_attributeContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#plugin_attribute_value}.
	 * @param ctx the parse tree
	 */
	void enterPlugin_attribute_value(ConfigParser.Plugin_attribute_valueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#plugin_attribute_value}.
	 * @param ctx the parse tree
	 */
	void exitPlugin_attribute_value(ConfigParser.Plugin_attribute_valueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#stage_condition}.
	 * @param ctx the parse tree
	 */
	void enterStage_condition(ConfigParser.Stage_conditionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#stage_condition}.
	 * @param ctx the parse tree
	 */
	void exitStage_condition(ConfigParser.Stage_conditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void enterLogical_expression(ConfigParser.Logical_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#logical_expression}.
	 * @param ctx the parse tree
	 */
	void exitLogical_expression(ConfigParser.Logical_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#negative_expression}.
	 * @param ctx the parse tree
	 */
	void enterNegative_expression(ConfigParser.Negative_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#negative_expression}.
	 * @param ctx the parse tree
	 */
	void exitNegative_expression(ConfigParser.Negative_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#compare_expression}.
	 * @param ctx the parse tree
	 */
	void enterCompare_expression(ConfigParser.Compare_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#compare_expression}.
	 * @param ctx the parse tree
	 */
	void exitCompare_expression(ConfigParser.Compare_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#in_expression}.
	 * @param ctx the parse tree
	 */
	void enterIn_expression(ConfigParser.In_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#in_expression}.
	 * @param ctx the parse tree
	 */
	void exitIn_expression(ConfigParser.In_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#match_expression}.
	 * @param ctx the parse tree
	 */
	void enterMatch_expression(ConfigParser.Match_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#match_expression}.
	 * @param ctx the parse tree
	 */
	void exitMatch_expression(ConfigParser.Match_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#rvalue}.
	 * @param ctx the parse tree
	 */
	void enterRvalue(ConfigParser.RvalueContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#rvalue}.
	 * @param ctx the parse tree
	 */
	void exitRvalue(ConfigParser.RvalueContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#fieldref}.
	 * @param ctx the parse tree
	 */
	void enterFieldref(ConfigParser.FieldrefContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#fieldref}.
	 * @param ctx the parse tree
	 */
	void exitFieldref(ConfigParser.FieldrefContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#fieldref_element}.
	 * @param ctx the parse tree
	 */
	void enterFieldref_element(ConfigParser.Fieldref_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#fieldref_element}.
	 * @param ctx the parse tree
	 */
	void exitFieldref_element(ConfigParser.Fieldref_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#array}.
	 * @param ctx the parse tree
	 */
	void enterArray(ConfigParser.ArrayContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#array}.
	 * @param ctx the parse tree
	 */
	void exitArray(ConfigParser.ArrayContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#array_element}.
	 * @param ctx the parse tree
	 */
	void enterArray_element(ConfigParser.Array_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#array_element}.
	 * @param ctx the parse tree
	 */
	void exitArray_element(ConfigParser.Array_elementContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#hash}.
	 * @param ctx the parse tree
	 */
	void enterHash(ConfigParser.HashContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#hash}.
	 * @param ctx the parse tree
	 */
	void exitHash(ConfigParser.HashContext ctx);
	/**
	 * Enter a parse tree produced by {@link ConfigParser#hash_element}.
	 * @param ctx the parse tree
	 */
	void enterHash_element(ConfigParser.Hash_elementContext ctx);
	/**
	 * Exit a parse tree produced by {@link ConfigParser#hash_element}.
	 * @param ctx the parse tree
	 */
	void exitHash_element(ConfigParser.Hash_elementContext ctx);
}