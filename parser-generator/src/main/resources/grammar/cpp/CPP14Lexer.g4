// $antlr-format alignTrailingComments true, columnLimit 150, maxEmptyLinesToKeep 1, reflowComments false, useTab false
// $antlr-format allowShortRulesOnASingleLine true, allowShortBlocksOnASingleLine true, minEmptyLines 0, alignSemicolons ownLine
// $antlr-format alignColons trailing, singleLineOverrulesHangingColon true, alignLexerCommands true, alignLabels true, alignTrailers true

lexer grammar CPP14Lexer;

INTEGER_LITERAL:
    DECIMAL_LITERAL INTEGERSUFFIX?
    | OCTAL_LITERAL INTEGERSUFFIX?
    | HEXADECIMAL_LITERAL INTEGERSUFFIX?
    | BINARY_LITERAL INTEGERSUFFIX?
;

CHARACTER_LITERAL: ('u' | 'U' | 'L')? '\'' Cchar+ '\'';

FLOATING_LITERAL:
    Fractionalconstant Exponentpart? Floatingsuffix?
    | Digitsequence Exponentpart Floatingsuffix?
;

STRING_LITERAL: Encodingprefix? (Rawstring | '"' Schar* '"');

BOOLEAN_LITERAL: FALSE_ | TRUE_;

POINTER_LITERAL: NULLPTR;

USER_DEFINED_LITERAL:
    USER_DEFINED_INTEGER_LITERAL
    | USER_DEFINED_FLOATING_LITERAL
    | USER_DEFINED_STRING_LITERAL
    | USER_DEFINED_CHARACTER_LITERAL
;

MULTI_LINE_MACRO: '#' (~[\n]*? '\\' '\r'? '\n')+ ~ [\n]+ -> channel (HIDDEN);

DIRECTIVE: '#' ~ [\n]* -> channel (HIDDEN);
/*Keywords*/

ALIGNAS: 'alignas';

ALIGNOF: 'alignof';

ASM: 'asm';

AUTO: 'auto';

BOOL: 'bool';

BREAK: 'break';

CASE: 'case';

CATCH: 'catch';

CHAR: 'char';

CHAR16: 'char16_t';

CHAR32: 'char32_t';

CLASS: 'class';

CONST: 'const';

CONSTEXPR: 'constexpr';

CONST_CAST: 'const_cast';

CONTINUE: 'continue';

DECLTYPE: 'decltype';

DEFAULT: 'default';

DELETE: 'delete';

DO: 'do';

DOUBLE: 'double';

DYNAMIC_CAST: 'dynamic_cast';

ELSE: 'else';

ENUM: 'enum';

EXPLICIT: 'explicit';

EXPORT: 'export';

EXTERN: 'extern';

//DO NOT RENAME - PYTHON NEEDS True and False
FALSE_: 'false';

FINAL: 'final';

FLOAT: 'float';

FOR: 'for';

FRIEND: 'friend';

GOTO: 'goto';

IF: 'if';

INLINE: 'inline';

INT: 'int';

LONG: 'long';

MUTABLE: 'mutable';

NAMESPACE: 'namespace';

NEW: 'new';

NOEXCEPT: 'noexcept';

NULLPTR: 'nullptr';

OPERATOR: 'operator';

OVERRIDE: 'override';

PRIVATE: 'private';

PROTECTED: 'protected';

PUBLIC: 'public';

REGISTER: 'register';

REINTERPRET_CAST: 'reinterpret_cast';

RETURN: 'return';

SHORT: 'short';

SIGNED: 'signed';

SIZEOF: 'sizeof';

STATIC: 'static';

STATIC_ASSERT: 'static_assert';

STATIC_CAST: 'static_cast';

STRUCT: 'struct';

SWITCH: 'switch';

TEMPLATE: 'template';

THIS: 'this';

THREAD_LOCAL: 'thread_local';

THROW: 'throw';

//DO NOT RENAME - PYTHON NEEDS True and False
TRUE_: 'true';

TRY: 'try';

TYPEDEF: 'typedef';

TYPEID_: 'typeid';

TYPENAME_: 'typename';

UNION: 'union';

UNSIGNED: 'unsigned';

USING: 'using';

VIRTUAL: 'virtual';

VOID: 'void';

VOLATILE: 'volatile';

WCHAR: 'wchar_t';

WHILE: 'while';
/*Operators*/

LEFT_PAREN: '(';

RIGHT_PAREN: ')';

LEFT_BRACKET: '[';

RIGHT_BRACKET: ']';

LEFT_BRACE: '{';

RIGHT_BRACE: '}';

PLUS: '+';

MINUS: '-';

STAR: '*';

DIV: '/';

MOD: '%';

CARET: '^';

AND: '&';

OR: '|';

TILDE: '~';

NOT: '!' | 'not';

ASSIGN: '=';

LESS: '<';

GREATER: '>';

PLUS_ASSIGN: '+=';

MINUS_ASSIGN: '-=';

STAR_ASSIGN: '*=';

DIV_ASSIGN: '/=';

MOD_ASSIGN: '%=';

XOR_ASSIGN: '^=';

AND_ASSIGN: '&=';

OR_ASSIGN: '|=';

LEFT_SHIFT_ASSIGN: '<<=';

RIGHT_SHIFT_ASSIGN: '>>=';

EQUAL: '==';

NOT_EQUAL: '!=';

LESS_EQUAL: '<=';

GREATER_EQUAL: '>=';

AND_AND: '&&' | 'and';

OR_OR: '||' | 'or';

PLUS_PLUS: '++';

MINUS_MINUS: '--';

COMMA: ',';

ARROW_STAR: '->*';

ARROW: '->';

QUESTION: '?';

COLON: ':';

DOUBLECOLON: '::';

SEMI: ';';

DOT: '.';

DOT_STAR: '.*';

ELLIPSIS: '...';

fragment Hexquad: Hexadecimaldigit Hexadecimaldigit Hexadecimaldigit Hexadecimaldigit;

fragment Universalcharactername: '\\u' Hexquad | '\\U' Hexquad Hexquad;

IDENTIFIER:
    /*
	 Identifiernondigit | IDENTIFIER Identifiernondigit | IDENTIFIER Digit
	 */ Identifiernondigit (Identifiernondigit | Digit)*
;

fragment Identifiernondigit: Nondigit | Universalcharactername;

fragment Nondigit: [a-zA-Z_];

fragment Digit: [0-9];

DECIMAL_LITERAL: Nonzerodigit ('\''? Digit)*;

OCTAL_LITERAL: '0' ('\''? Octaldigit)*;

HEXADECIMAL_LITERAL: ('0x' | '0X') Hexadecimaldigit ( '\''? Hexadecimaldigit)*;

BINARY_LITERAL: ('0b' | '0B') Binarydigit ('\''? Binarydigit)*;

fragment Nonzerodigit: [1-9];

fragment Octaldigit: [0-7];

fragment Hexadecimaldigit: [0-9a-fA-F];

fragment Binarydigit: [01];

INTEGERSUFFIX:
    Unsignedsuffix Longsuffix?
    | Unsignedsuffix Longlongsuffix?
    | Longsuffix Unsignedsuffix?
    | Longlongsuffix Unsignedsuffix?
;

fragment Unsignedsuffix: [uU];

fragment Longsuffix: [lL];

fragment Longlongsuffix: 'll' | 'LL';

fragment Cchar: ~ ['\\\r\n] | Escapesequence | Universalcharactername;

fragment Escapesequence: Simpleescapesequence | Octalescapesequence | Hexadecimalescapesequence;

fragment Simpleescapesequence:
    '\\\''
    | '\\"'
    | '\\?'
    | '\\\\'
    | '\\a'
    | '\\b'
    | '\\f'
    | '\\n'
    | '\\r'
    | '\\' ('\r' '\n'? | '\n')
    | '\\t'
    | '\\v'
;

fragment Octalescapesequence:
    '\\' Octaldigit
    | '\\' Octaldigit Octaldigit
    | '\\' Octaldigit Octaldigit Octaldigit
;

fragment Hexadecimalescapesequence: '\\x' Hexadecimaldigit+;

fragment Fractionalconstant: Digitsequence? '.' Digitsequence | Digitsequence '.';

fragment Exponentpart: 'e' Sign? Digitsequence | 'E' Sign? Digitsequence;

fragment Sign: [+-];

fragment Digitsequence: Digit ('\''? Digit)*;

fragment Floatingsuffix: [flFL];

fragment Encodingprefix: 'u8' | 'u' | 'U' | 'L';

fragment Schar: ~ ["\\\r\n] | Escapesequence | Universalcharactername;

fragment Rawstring: 'R"' ( '\\' ["()] | ~[\r\n (])*? '(' ~[)]*? ')' ( '\\' ["()] | ~[\r\n "])*? '"';

USER_DEFINED_INTEGER_LITERAL:
    DECIMAL_LITERAL Udsuffix
    | OCTAL_LITERAL Udsuffix
    | HEXADECIMAL_LITERAL Udsuffix
    | BINARY_LITERAL Udsuffix
;

USER_DEFINED_FLOATING_LITERAL:
    Fractionalconstant Exponentpart? Udsuffix
    | Digitsequence Exponentpart Udsuffix
;

USER_DEFINED_STRING_LITERAL: STRING_LITERAL Udsuffix;

USER_DEFINED_CHARACTER_LITERAL: CHARACTER_LITERAL Udsuffix;

fragment Udsuffix: IDENTIFIER;

WHITESPACE: [ \t]+ -> channel(HIDDEN);

NEW_LINE: ('\r\n' | '\r' | '\n') -> channel(HIDDEN);

DOCUMENTATION: '/**' .*? '*/' -> channel(HIDDEN);

BLOCK_COMMENT: '/*' .*? '*/' -> channel(HIDDEN);

LINE_COMMENT: '//' ~ [\r\n]* -> channel(HIDDEN);

ERROR:              . -> channel(HIDDEN);
