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

parser grammar GosuParser;



options {tokenVocab = GosuLexer;}


start
    : packageDeclaration? usesStatementList? typeDeclaration EOF?
    ;

packageDeclaration
    : PACKAGE (namespace semicolon?)?
    ;

usesStatementList
    : (USES usesStatement)+
    ;

usesStatement
    : namespace (DOT MUL)? semicolon?
    | usesFeatureLiteral semicolon?
    ;

usesFeatureLiteral
    : namespace HASH (identifier arguments? | MUL)
    ;

typeDeclaration
    : classDeclaration
    | enhancementDeclaration
    | enumDeclaration
    | interfaceDeclaration
    | structureDeclaration
    | annotationDeclaration
    ;

annotation
    : AT identifier (DOT identifier)* typeParameters? arguments?
    ;

classDeclaration
    : classSignature classBody
    ;

classSignature
    : modifiers? CLASS identifier typeParameters? (EXTENDS classOrInterfaceType)? (IMPLEMENTS classOrInterfaceType (COMMA classOrInterfaceType)*)?
    ;

enhancementDeclaration
    : enhancementSignature enhancementBody
    ;

enhancementSignature
    : modifiers? ENHANCEMENT identifier typeParameters? COLON classOrInterfaceType (LBRACK RBRACK)*
    ;

interfaceDeclaration
    : interfaceSignature interfaceBody
    ;

interfaceSignature
    : modifiers? INTERFACE identifier typeParameters? (EXTENDS classOrInterfaceType (COMMA classOrInterfaceType)*)?
    ;

structureDeclaration
    : structureSignature structureBody
    ;

structureSignature
    : modifiers? STRUCTURE identifier typeParameters? (EXTENDS classOrInterfaceType (COMMA classOrInterfaceType)*)?
    ;

enumDeclaration
    : enumSignature enumBody
    ;

enumSignature
    : modifiers? ENUM identifier (IMPLEMENTS classOrInterfaceType (COMMA classOrInterfaceType)*)?
    ;

annotationDeclaration
    : annotationSignature annotationBody
    ;

annotationSignature
    : modifiers? ANNOTATION identifier
    ;

classBody
    : LBRACE classMembers RBRACE
    ;

enhancementBody
    : LBRACE enhancementMembers RBRACE
    ;

interfaceBody
    : LBRACE interfaceMembers RBRACE
    ;

structureBody
    : LBRACE structureMembers RBRACE
    ;

enumBody
    : LBRACE enumConstants? classMembers RBRACE
    ;

annotationBody
    : LBRACE annotationMembers RBRACE
    ;

enumConstants
    : enumConstant  (COMMA enumConstant)*  COMMA? semicolon?
    ;

enumConstant
    : identifier arguments?
    ;

classMembers
    : ((function
    | constructor
    | property
    | field
    | delegate
    | classDeclaration
    | interfaceDeclaration
    | structureDeclaration
    | enumDeclaration) semicolon?)*
    ;

enhancementMembers
    : ((function
    | propertySignature functionBody) semicolon?)*
    ;

interfaceMembers
    : ((function
    | property
    | field
    | classDeclaration
    | interfaceDeclaration
    | structureDeclaration
    | enumDeclaration) semicolon?)*
    ;

structureMembers
    : ((function
    | property
    | field
    | classDeclaration
    | interfaceDeclaration
    | structureDeclaration
    | enumDeclaration) semicolon?)*
    ;

annotationMembers
    : ((function
    | constructor
    | property
    | field
    | delegate
    | classDeclaration
    | interfaceDeclaration
    | structureDeclaration
    | enumDeclaration
    | defaultValueFunction) semicolon?)*
    ;

function
    : functionSignature functionBody?
    ;

constructor
    : constructorSignature functionBody
    ;

property
    : propertySignature functionBody?
    ;

defaultValueFunction
    : functionSignature EQUALS statement
    ;

functionSignature
    : modifiers? FUNCTION identifier typeParameters? LPAREN parameterDeclarationList? RPAREN (COLON typeLiteral)?
    ;

constructorSignature
    : modifiers? CONSTRUCT LPAREN parameterDeclarationList? RPAREN (COLON typeLiteral)?
    ;

functionBody
    : statementBlock
    ;

statementBlock
    : LBRACE statement* RBRACE
    ;

statement
    : (assignStatement
    | ifStatement
    | tryCatchFinallyStatement
    | throwStatement
    | continueStatement
    | breakStatement
    | returnStatement
    | forEachStatement
    | whileStatement
    | doWhileStatement
    | switchStatement
    | usingStatement
    | assertStatement
    | localVarStatement
    | statementBlock
    | expression) semicolon?
    | semicolon
    ;

ifStatement
    : IF LPAREN expression RPAREN statement semicolon? elseStatement?
    ;

elseStatement
    : ELSE statement
    ;

tryCatchFinallyStatement
    : TRY statementBlock (catchClause+ finallyStatement? | finallyStatement)
    ;

catchClause
    : CATCH LPAREN VAR? identifier (COLON typeLiteral)? RPAREN statementBlock
    ;

throwStatement
    : THROW expression
    ;

returnStatement
    : RETURN expression?
    ;

whileStatement
    : WHILE LPAREN expression RPAREN statement
    ;

doWhileStatement
    : DO statement WHILE LPAREN expression RPAREN
    ;

switchStatement
    : SWITCH LPAREN expression RPAREN LBRACE switchBlockStatement* RBRACE
    ;

switchBlockStatement
    : caseOrDefaultStatement COLON statement*
    ;

caseOrDefaultStatement
    : (CASE expression | DEFAULT)
    ;

usingStatement
    : USING LPAREN (localVarStatement (COMMA localVarStatement)* | expression) RPAREN statementBlock finallyStatement?
    ;

assertStatement
    : ASSERT expression (COLON expression)?
    ;

localVarStatement
    : FINAL? VAR identifier optionalType? (EQUALS expression)?
    ;

breakStatement
    : BREAK
    ;

continueStatement
    : CONTINUE
    ;

assignStatement
    : expression (EQUALS
    | ADD_ASSIGN
    | SUB_ASSIGN
    | MUL_ASSIGN
    | DIV_ASSIGN
    | AND_ASSIGN
    | LOGICAL_AND_ASSIGN
    | OR_ASSIGN
    | XOR_ASSIGN
    | MOD_ASSIGN
    | LSHIFT_ASSIGN
    | RSHIFT_ASSIGN
    | URSHIFT_ASSIGN) expression
    ;

forEachStatement
    : (FOREACH | FOR) LPAREN (expression indexVar? | VAR? identifier IN expression indexRest?) RPAREN statement
    ;

finallyStatement
    : FINALLY statementBlock
    ;

indexRest
    : indexVar iteratorVar
    | iteratorVar indexVar
    | indexVar
    | iteratorVar
    ;

indexVar
    : INDEX identifier
    ;

iteratorVar
    : ITERATOR identifier
    ;

propertySignature
    : modifiers? PROPERTY (GET | SET) identifier LPAREN parameterDeclarationList? RPAREN (COLON typeLiteral)?
    ;

field
    : modifiers? VAR identifier optionalType? (AS READONLY? identifier)? (EQUALS expression)?
    ;

delegate
    : modifiers? DELEGATE identifier delegateStatement
    ;

delegateStatement
    : (COLON typeLiteral)? REPRESENTS typeLiteral (COMMA typeLiteral)* (EQUALS expression)?
    ;

modifiers
    : (annotation
    | ANNOTATION
    | PRIVATE
    | INTERNAL
    | PROTECTED
    | PUBLIC
    | STATIC
    | ABSTRACT
    | OVERRIDE
    | FINAL
    | TRANSIENT
    | REIFIED)+
    ;

classOrInterfaceType
    : namespace typeArguments? (DOT identifier typeArguments )*
    ;

parameterDeclarationList //parameters
    :   parameterDeclaration (COMMA parameterDeclaration)*
    ;

parameterDeclaration
    :  annotation* FINAL? (identifier | blockType) (COLON (typeLiteral | blockLiteral))? (EQUALS expression )?
    ;

blockType
    : (BLOCK | identifier) blockLiteral
    ;

typeParameters //typeVariableDefs
    : LT typeParameter (COMMA typeParameter)* GT
    ;

typeParameter
    : identifier (EXTENDS typeLiteral)?
    ;

typeArguments
    : LT typeArgument (COMMA typeArgument)* GT
    ;

typeArgument
    : typeLiteral
    | QUESTION ((EXTENDS | SUPER) typeLiteral)?
    ;

optionalType
    : COLON typeLiteral | blockLiteral
    ;

typeLiteral
    : type (BITAND type)*
    ;

type
    : classOrInterfaceType (LBRACK RBRACK)*
    | BLOCK blockLiteral
    | VOID //?
    ;

blockLiteral
    :  LPAREN (blockLiteralArg (COMMA blockLiteralArg)*)? RPAREN (COLON typeLiteral )?
    ;

blockLiteralArg
    : identifier (EQUALS expression | blockLiteral)
    | (identifier COLON)? typeLiteral (EQUALS expression)?
    ;

arguments //optionalArguments
    : LPAREN (argExpression (COMMA argExpression)*)? RPAREN
    ;

argExpression
    : namedArgumentExpression
    | expression
    | assignStatement
    ;

namedArgumentExpression
    : COLON identifier EQUALS expression
    ;

expression //conditionalExpr
    : expression (MUL | DIV | MOD) expression                                                                       #multiplicationExpression
    | expression typeArguments? arguments                                                                           #methodCall
    | expression (QUESTION | MUL)? DOT expression                                                                   #memberAccess
    | expression (TYPEAS | AS) expression                                                                           #typeCastExpression
    | expression (EQUAL | GT | LT | LE | greaterEqual | NOTEQUAL) expression                                        #relationalExpression
    | expression (LT LT | GT GT | GT GT GT) expression                                                              #bitwiseExpressison
    |  lambdaSignature  lambdaBody                                                                                  #lambdaExpression
    | expression (INC | DEC)                                                                                        #preIncrementExpression
    | (TILDE | EXCLAMATION | NOT) expression                                                                        #unaryExpressionNot
    | expression QUESTION_COLON expression                                                                          #safeTernaryExpression
    | primary                                                                                                       #primaryExpression
    | (THIS | SUPER) expression                                                                                     #thisSuperExpression
    | NEW classOrInterfaceType? arguments? arrayCreator? (LBRACE (initializer | anonymousInnerClass) RBRACE)?       #newExpression
    | <assoc=right> expression (PLUS | MINUS) expression                                                            #unaryExpression
    | expression TYPEIS type                                                                                        #typeisExpression
    | (TYPEOF | STATICTYPEOF) expression                                                                            #typeofExpression
    | <assoc=right> expression (AND | OR | CONJ | DISJ | BITAND | BITOR | CARET) expression                         #logicalExpression
    | expression QUESTION? LBRACK expression RBRACK                                                                 #elementAccessExpression
    | MINUS expression                                                                                              #unaryExpression
    | <assoc=right> expression QUESTION expression COLON expression                                                 #ternaryExpression
    | expression INTERVALOP expression                                                                              #intervalExpression
    | EVAL LPAREN expression RPAREN                                                                                 #evalExpression
    ;

anonymousInnerClass
    : classMembers
    ;

lambdaSignature
    : BACKSLASH parameterDeclarationList? ARROW
    ;

lambdaBody
    : (expression | statementBlock)
    ;

arrayCreator
    : (LBRACK expression? RBRACK)+
    ;

initializer
    : (initializerExpression | objectInitializer)?
    ;

initializerExpression
    : mapInitializerList
    | arrayValueList
    ;

arrayValueList
    : expression (COMMA expression)*
    ;

mapInitializerList
    : expression ARROW expression (COMMA expression ARROW expression)*
    ;

objectInitializer
    : initializerAssignment (COMMA initializerAssignment)*
    ;

initializerAssignment
    : COLON identifier EQUALS expression
    ;

primary
    : literal
    | HASH? identifier
    | type
    | LPAREN expression RPAREN
    | THIS
    | SUPER
    ;

namespace
    : identifier (DOT identifier)*
    ;

identifier //id
    : IDENTIFIER
    | TRUE
    | FALSE
    | AS
    | INDEX
    | ITERATOR
    | GET
    | SET
    | ASSERT
    | PRIVATE
    | INTERNAL
    | PROTECTED
    | PUBLIC
    | ABSTRACT
    | FINAL
    | STATIC
    | READONLY
    | VOID
    | ENHANCEMENT
    | CONSTRUCT
    | ANNOTATION
    | THIS
    | SUPER
    ;

semicolon
    : SEMICOLON+
    ;

literal
    : numberLiteral
    | stringLiteral
    | arrayLiteral
    | featureLiteral
    | booleanLiteral
    | mapLiteral
    ;

stringLiteral
    : OPEN_STRING_DQ stringPart* CLOSE_STRING_DQ
    | OPEN_STRING_SQ stringPart* CLOSE_STRING_SQ
    | CHAR_LITERAL
    ;

stringPart
    : interpolation | SQ_TEXT | DQ_TEXT
    ;

interpolation
    : DQ_INTERP expression RBRACE
    | SQ_INTERP expression RBRACE
    ;

numberLiteral
    : NumberLiteral
    ;

arrayLiteral
    : LBRACE arrayValueList? RBRACE
    ;

featureLiteral
    : identifier HASH (identifier | CONSTRUCT) typeArguments? arguments?
    ;

booleanLiteral
    : TRUE
    | FALSE
    | NULL
    ;

mapLiteral
    : LBRACE (mapInitializerList)? RBRACE
    ;

greaterEqual
    : GT EQUALS
    ;
