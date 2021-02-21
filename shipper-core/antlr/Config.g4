grammar Config;

@header {
package com.logstash;
}

/* Lexical rules */

INPUT : 'input';
FILTER : 'filter';
OUTPUT : 'output';

LBRACE : '{';
RBRACE : '}';
LPAREN : '(';
RPAREN : ')';
LBRACKET : '[';
RBRACKET : ']';
QMARK : '?';

HASHROCKET : '=>';

GT : '>';
GE : '>=';
LT : '<';
LE : '<=';
EQ : '==';
NEQ : '!=';
BANG : '!';
COMA : ',';
IN : 'in';
NOT: 'not';
MATCH : '=~';
NOT_MATCH : '!~';

AND : 'and';
OR : 'or';
XOR : 'xor';
NAND : 'nand';

IF : 'if';
ELSE : 'else';

fragment NL : '\n';
fragment HASH : '#';
fragment SLASH : '/';
fragment BSLASH : '\\';

// double and single quoted string support
// LUCA: we're not using escaping as per https://www.elastic.co/guide/en/logstash/current/configuration-file-structure.html#string
fragment DQUOTE : '"';
fragment SQUOTE : '\'';
fragment DQ_STRING : DQUOTE (~["])* DQUOTE ;
fragment SQ_STRING : SQUOTE (~['])* SQUOTE ;
STRING : DQ_STRING | SQ_STRING ;
// TODO: (colin) verify REGEX validity, I am unsure about the original treetop grammar definition

/*
 * Regular expressions (delimited by slashes in Annis)
 */
fragment FOCC	     : '{' WS* ( [0-9]* WS* ',' WS* [0-9]+ | [0-9]+ WS* ','? ) WS* '}';
fragment RE_char     : ~('*' | '?' | '+' | '{' | '}' | '[' | ']' | '/'
         	            | '(' | ')' | '|' | '"' | ':' | '\'' | '\\');
fragment RE_alter    : ((RE_char | ('(' REGEX ')') | RE_chgroup) '|' REGEX )+;
fragment RE_chgroup  : '[' (RE_char | RE_escape)+ ']';
fragment RE_quant	 : (RE_star | RE_plus | RE_occ) QMARK?;
fragment RE_opt      : (RE_char | RE_chgroup | RE_escape | ( '(' REGEX ')')) '?';
fragment RE_star     : (RE_char | RE_chgroup | RE_escape | ( '(' REGEX ')')) '*';
fragment RE_plus     : (RE_char | RE_chgroup | RE_escape | ( '(' REGEX ')')) '+';
fragment RE_occ      : (RE_char | RE_chgroup | RE_escape | ( '(' REGEX ')')) FOCC;
fragment RE_group    : '(' REGEX ')';
fragment RE_escape    : BSLASH (BSLASH | SLASH | '.' | 'w');
REGEX     		     : SLASH ('.' | RE_char | RE_alter | RE_chgroup | RE_opt | RE_quant | RE_group | RE_escape)* SLASH;

DECIMAL : '-'?[0-9]+('.'[0-9]+)? ;
IDENTIFIER : [a-zA-Z_][a-zA-Z_0-9-]* ;
// ignore whitespaces and comments
WS : [ \r\t\n\u000C]+ -> skip ;
COMMENT : HASH .+? (NL|EOF) -> skip ;
/* Grammar rules */
config : stage_declaration+ EOF ;
stage_declaration : ( INPUT | FILTER | OUTPUT ) stage_definition ;
stage_definition : LBRACE (plugin_declaration | stage_condition)* RBRACE ;
plugin_declaration : IDENTIFIER plugin_definition ;
plugin_definition : LBRACE plugin_attribute* RBRACE ;
plugin_attribute : IDENTIFIER HASHROCKET plugin_attribute_value ;
// TODO: (colin) verify hash requirement here
plugin_attribute_value : plugin_declaration | IDENTIFIER | STRING | DECIMAL | array | hash ;
stage_condition : IF logical_expression stage_definition (ELSE IF logical_expression stage_definition)* (ELSE stage_definition)? ;
logical_expression
 : logical_expression AND logical_expression
 | logical_expression OR logical_expression
 | logical_expression NAND logical_expression
 | logical_expression XOR logical_expression
 | compare_expression
 | in_expression
 | match_expression
 | negative_expression
 | LPAREN logical_expression RPAREN
 | rvalue
 ;
negative_expression : BANG logical_expression ;
compare_expression
 : rvalue GT rvalue
 | rvalue GE rvalue
 | rvalue LT rvalue
 | rvalue LE rvalue
 | rvalue EQ rvalue
 | rvalue NEQ rvalue
 ;
in_expression : rvalue NOT? IN rvalue ;
match_expression : rvalue (MATCH | NOT_MATCH) (STRING | REGEX) ;
// TODO: (colin) add method_call to rvalue
// TODO: (colin) per original treetop grammar, REGEX is also valid in rvalue, should we add it?
rvalue : STRING | DECIMAL | fieldref | array ;
fieldref : fieldref_element+ ;
fieldref_element : LBRACKET IDENTIFIER RBRACKET ;
// TODO: (colin) plugin_declaration are allowed in array per original treetop grammar and seems related
// TODO: (con't) to never-implemented channable codec. I am not allowing here.
array : LBRACKET  (array_element (COMA array_element)*)?  RBRACKET;
array_element : IDENTIFIER | STRING | DECIMAL | array | hash ;

hash : LBRACE hash_element* RBRACE ;
hash_element : (DECIMAL | IDENTIFIER | STRING) HASHROCKET plugin_attribute_value ;