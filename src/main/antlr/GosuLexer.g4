//
// Copyright (C) 2023 FRIDAY Insurance S.A.
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU Affero General Public License as published
// by the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Affero General Public License for more details.
//
// You should have received a copy of the GNU Affero General Public License
// along with this program. If not, see <https://www.gnu.org/licenses/>.
//

lexer grammar GosuLexer;


channels { COMMENTS }

//KEYWORDS

ABSTRACT:       {_input.LA(-1) != '.'}?'abstract';
AND:            {_input.LA(-1) != '.'}?'and';
ANNOTATION:     {_input.LA(-1) != '.'}?'annotation';
AS:             {_input.LA(-1) != '.'}?'as';
ASSERT:         {_input.LA(-1) != '.'}?'assert';
BLOCK:          {_input.LA(-1) != '.'}?'block';
BREAK:          {_input.LA(-1) != '.'}?'break';
CASE:           {_input.LA(-1) != '.'}?'case';
CATCH:          {_input.LA(-1) != '.'}?'catch';
CLASS:          {_input.LA(-1) != '.'}?'class';
CONSTRUCT:      {_input.LA(-1) != '.'}?'construct';
CONTINUE:       {_input.LA(-1) != '.'}?'continue';
DEFAULT:        {_input.LA(-1) != '.'}?'default';
DELEGATE:       {_input.LA(-1) != '.'}?'delegate';
DO:             {_input.LA(-1) != '.'}?'do';
ELSE:           {_input.LA(-1) != '.'}?'else';
ENHANCEMENT:    {_input.LA(-1) != '.'}?'enhancement';
ENUM:           {_input.LA(-1) != '.'}?'enum';
EVAL:           {_input.LA(-1) != '.'}?'eval';
EXTENDS:        {_input.LA(-1) != '.'}?'extends';
FALSE:          {_input.LA(-1) != '.'}?'false';
FINAL:          {_input.LA(-1) != '.'}?'final';
FINALLY:        {_input.LA(-1) != '.'}?'finally';
FOR:            {_input.LA(-1) != '.'}?'for';
FOREACH:        {_input.LA(-1) != '.'}?'foreach';
FUNCTION:       {_input.LA(-1) != '.'}?'function';
GET:            {_input.LA(-1) != '.'}?'get';
IF:             {_input.LA(-1) != '.'}?'if';
IMPLEMENTS:     {_input.LA(-1) != '.'}?'implements';
IN:             {_input.LA(-1) != '.'}?'in';
INDEX:          {_input.LA(-1) != '.'}?'index';
INTERFACE:      {_input.LA(-1) != '.'}?'interface';
INTERNAL:       {_input.LA(-1) != '.'}?'internal';
ITERATOR:       {_input.LA(-1) != '.'}?'iterator';
NEW:            {_input.LA(-1) != '.'}?'new';
NOT:            {_input.LA(-1) != '.'}?'not';
OR:             {_input.LA(-1) != '.'}?'or';
OVERRIDE:       {_input.LA(-1) != '.'}?'override';
PACKAGE:        {_input.LA(-1) != '.'}?'package';
PRIVATE:        {_input.LA(-1) != '.'}?'private';
PROPERTY:       {_input.LA(-1) != '.'}?'property';
PROTECTED:      {_input.LA(-1) != '.'}?'protected';
PUBLIC:         {_input.LA(-1) != '.'}?'public';
READONLY:       {_input.LA(-1) != '.'}?'readonly';
REIFIED:        {_input.LA(-1) != '.'}?'reified';
REPRESENTS:     {_input.LA(-1) != '.'}?'represents';
RETURN:         {_input.LA(-1) != '.'}?'return';
SET:            {_input.LA(-1) != '.'}?'set';
STATIC:         {_input.LA(-1) != '.'}?'static';
STATICTYPEOF:   {_input.LA(-1) != '.'}?'statictypeof';
STRUCTURE:      {_input.LA(-1) != '.'}?'structure';
SUPER:          {_input.LA(-1) != '.'}?'super';
SWITCH:         {_input.LA(-1) != '.'}?'switch';
THIS:           {_input.LA(-1) != '.'}?'this';
THROW:          {_input.LA(-1) != '.'}?'throw';
TRANSIENT:      {_input.LA(-1) != '.'}?'transient';
TRUE:           {_input.LA(-1) != '.'}?'true';
TRY:            {_input.LA(-1) != '.'}?'try';
TYPEAS:         {_input.LA(-1) != '.'}?'typeas';
TYPEIS:         {_input.LA(-1) != '.'}?'typeis';
TYPEOF:         {_input.LA(-1) != '.'}?'typeof';
USES:           {_input.LA(-1) != '.'}?'uses';
USING:          {_input.LA(-1) != '.'}?'using';
VAR:            {_input.LA(-1) != '.'}?'var';
VOID:           {_input.LA(-1) != '.'}?'void';
WHILE:          {_input.LA(-1) != '.'}?'while';
NULL:           {_input.LA(-1) != '.'}?'null';
NAN:            {_input.LA(-1) != '.'}?'NaN';
INFINITY:       {_input.LA(-1) != '.'}?'Infinity';
// Separators --------------------------------------------- ADD GOSU SPECIFIC OPERATORS

LPAREN : '(';
RPAREN : ')';
LBRACE : '{' -> pushMode(DEFAULT_MODE);
RBRACE : '}' -> popMode;
LBRACK : '[';
RBRACK : ']';
SEMICOLON : ';';
COMMA : ',';
DOT : {_input.LA(2) != '.' }?'.';

// Operators

EQUALS : '=';
GT : '>';
LT : '<';
EXCLAMATION : '!';
TILDE : '~';
QUESTION : '?';
QUESTION_COLON : '?:';
COLON : ':';
EQUAL : '==' | '===';
LE : '<=';
NOTEQUAL : '!=' | '!==';
CONJ : '&&';
DISJ : '||';
INC : '++';
DEC : '--';
PLUS : '+';
MINUS : '-';
MUL : '*';
DIV : '/';
BITAND : '&';
BITOR : '|';
CARET : '^';
MOD : '%';
ARROW : '->';
AT : '@';
INTERVALOP : '..' | '|..' | '..|' | '|..|';
HASH : '#';
BACKSLASH : '\\';

ADD_ASSIGN : '+=';
SUB_ASSIGN : '-=';
MUL_ASSIGN : '*=';
DIV_ASSIGN : '/=';
AND_ASSIGN : '&=';
LOGICAL_AND_ASSIGN : '&&=';
OR_ASSIGN : '|=' | '||=';
XOR_ASSIGN : '^=';
MOD_ASSIGN : '%=';
LSHIFT_ASSIGN : '<<=';
RSHIFT_ASSIGN : '>>=';
URSHIFT_ASSIGN : '>>>=';

IDENTIFIER : Letter (Digit | Letter)* ;

NumberLiteral :  NAN                  |
                 INFINITY             |
                 HexLiteral           |
                 BinLiteral           |
                 IntOrFloatPointLiteral
              ;

fragment
BinLiteral : ('0b'|'0B') ('0' | '1')+ IntegerTypeSuffix? ;

fragment
HexLiteral : ('0x'|'0X') HexDigit+ ('s'|'S'|'l'|'L')? ;

fragment
IntOrFloatPointLiteral :    ('.' Digit+ Exponent? FloatTypeSuffix?                  |
                            Digit+ Exponent? FloatTypeSuffix?                       |
                            Digit+ {_input.LA(2) != '.' }?'.'                                                |
                            Digit+ (('.' Digit)? Digit* Exponent? FloatTypeSuffix?  |
                            Exponent FloatTypeSuffix?                               |
                            FloatTypeSuffix                                         |
                            IntegerTypeSuffix))
                            ;

// If need of interpolation -> https://thosakwe.com/parsing-string-interpolations-with-antlr4/
CHAR_LITERAL :  '\'' ( EscapeSequence | ~('\''|'\\'|'\r'|'\n') ) '\'' ;

//STRING_LITERAL : '"' ( EscapeSequence | EOL | ~('\\'|'"'|'$'|'\r'|'\n') | '$'('{' ~('}')* '}'| ~('{'|'"'|'\r'|'\n') ) )* ('"'|'$"')  |
//                '\'' ( EscapeSequence | EOL | ~('\\'|'\''|'$'|'\r'|'\n')| '$'('{' ~('}')* '}' ) )* ('\''|'$\'')
//              ;

OPEN_STRING_DQ : '"' -> pushMode(DQ_STRING);
OPEN_STRING_SQ : '\'' -> pushMode(SQ_STRING);

fragment
HexDigit : Digit | 'a'..'f' | 'A'..'F' ;

fragment
IntegerTypeSuffix : ('l'|'L'|'s'|'S'|'bi'|'BI'|'b'|'B') ;

fragment
Letter : 'A' .. 'Z' | 'a' .. 'z' | '_' | '$' ;

fragment
Digit : '0'..'9' ;

fragment
NonZeroDigit : '1'..'9' ;

fragment
Exponent : ('e'|'E') ('+'|'-')? Digit+ ;

fragment
FloatTypeSuffix : ('f'|'F'|'d'|'D'|'bd'|'BD') ;

fragment EOL     : '\\' '\r'? '\n' ;

fragment
EscapeSequence :   '\\' (' '|'v'|'a'|'b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\'|'$'|'<')     |
                   UnicodeEscape                                                    |
                   OctalEscape
               ;

fragment
OctalEscape : '\\' ('0'..'3') ('0'..'7') ('0'..'7')  |
              '\\' ('0'..'7') ('0'..'7')             |
              '\\' ('0'..'7')
            ;

fragment
UnicodeEscape : '\\' 'u' HexDigit HexDigit HexDigit HexDigit ;

WS :   (' ' | '\r' | '\t' | '\n' )+  -> skip;

COMMENT :   '/*'  (COMMENT|.)*? '*/'  -> channel(COMMENTS);

LINE_COMMENT :  '//' ~('\n'|'\r')* -> channel(COMMENTS) ;

mode DQ_STRING;

DQ_INTERP: '${' -> pushMode(DEFAULT_MODE);

DQ_TEXT: (EscapeSequence | EOL | ~('\\'|'"'|'$') | Dollar)+ ;

CLOSE_STRING_DQ: '"' -> popMode;

mode SQ_STRING;

SQ_INTERP: '${' -> pushMode(DEFAULT_MODE);

SQ_TEXT: ( EscapeSequence | EOL | ~('\\'|'\''|'$') | Dollar)+;

CLOSE_STRING_SQ: '\'' -> popMode;

fragment Dollar: '$'{_input.LA(1) != '{'}?;
