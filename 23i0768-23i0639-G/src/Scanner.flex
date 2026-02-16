/* src/Scanner.flex */
/* JFlex Specification File */

import java.io.*;

%%

%class Yylex
%type Token
%line
%column
%unicode

/* Macro Definitions */
LineTerminator = \r|\n|\r\n
Whitespace     = [ \t\f\r\n]+
Digit          = [0-9]
Letter         = [a-zA-Z]

/* Token Patterns */
SingleComment  = "##" [^\r\n]*
MultiComment   = "#*" [^*]* "*#" | "#*" ([^*] | "*"+[^#*])* "*#"

Keyword        = "start"|"finish"|"loop"|"condition"|"declare"|"output"|"input"|"function"|"return"|"break"|"continue"|"else"
Boolean        = "true"|"false"

Identifier     = [A-Z] [a-z0-9_]{0,30}
Integer        = [+-]? {Digit}+
Float          = [+-]? {Digit}+ \. {Digit}{1,6} ([eE] [+-]? {Digit}+)?

String         = \" ([^\"\\] | \\[\\'\"ntr])* \"
Char           = \' ([^\'\\] | \\[\\'\"ntr]) \'

OperatorMulti  = "**" | "==" | "!=" | "<=" | ">=" | "&&" | "||" | "+=" | "-=" | "*=" | "/=" | "++" | "--"
OperatorSingle = [+\-*/%=<>!]
Punctuator     = [(){}\[\]\,\;\:]

%%

/* Lexical Rules ordered by Priority */

{MultiComment}   { /* Skip */ }
{SingleComment}  { /* Skip */ }

{Whitespace}     { /* Skip */ }

{Keyword}        { return new Token(TokenType.KEYWORD, yytext(), yyline+1, yycolumn+1); }
{Boolean}        { return new Token(TokenType.BOOLEAN_LITERAL, yytext(), yyline+1, yycolumn+1); }

{Identifier}     { return new Token(TokenType.IDENTIFIER, yytext(), yyline+1, yycolumn+1); }

{Float}          { return new Token(TokenType.FLOAT_LITERAL, yytext(), yyline+1, yycolumn+1); }
{Integer}        { return new Token(TokenType.INTEGER_LITERAL, yytext(), yyline+1, yycolumn+1); }

{String}         { return new Token(TokenType.STRING_LITERAL, yytext(), yyline+1, yycolumn+1); }
{Char}           { return new Token(TokenType.CHAR_LITERAL, yytext(), yyline+1, yycolumn+1); }

{OperatorMulti}  { return new Token(TokenType.OPERATOR, yytext(), yyline+1, yycolumn+1); }
{OperatorSingle} { return new Token(TokenType.OPERATOR, yytext(), yyline+1, yycolumn+1); }
{Punctuator}     { return new Token(TokenType.PUNCTUATOR, yytext(), yyline+1, yycolumn+1); }

/* Error Handling Fallback */
.                { return new Token(TokenType.ERROR, yytext(), yyline+1, yycolumn+1); }