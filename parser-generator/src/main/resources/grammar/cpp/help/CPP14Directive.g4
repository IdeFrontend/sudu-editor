grammar CPP14Directive;

directive
    : include EOF
    | error EOF
    | dir EOF
    ;

include: HASH INCLUDE (STRING | (LEFT (IDENTIFIER | '/' | '.')+ RIGHT));
error: HASH ERROR_DIRECTIVE other*;
dir: HASH (IDENTIFIER | KEYWORD) other*;

other
    : (STRING | RAW_STRING) | LEFT | RIGHT | KEYWORD | (OPERATORS | DOT | SLASH) | IDENTIFIER
    | INTEGER_LITERAL | DECIMAL_LITERAL | OCTAL_LITERAL | HEXADECIMAL_LITERAL | BINARY_LITERAL
    | DIR_CHAR
    | INCLUDE | ERROR_DIRECTIVE
    | HASH
    ;

STRING : Quote Schar* Quote;
RAW_STRING
    : 'R"' Schar* '"'
    | 'u8R"' Schar* '"'
    | 'uR"'  Schar* '"'
    | 'UR"'  Schar* '"'
    | 'LR"'  Schar* '"'
    ;

HASH: '#';
INCLUDE: 'include';
ERROR_DIRECTIVE: 'error';

WHITESPACE: [ \t]+ -> channel(HIDDEN);
BLOCK_COMMENT: '/*' .*? '*/' -> channel(HIDDEN);
LINE_COMMENT: '//' ~ [\r\n]* -> channel(HIDDEN);
NEW_LINE_SLASH: '\\' -> channel(HIDDEN);
NEW_LINE: ('\r\n' | '\r' | '\n') -> channel(HIDDEN);

fragment Quote: '"';
LEFT: '<';
RIGHT: '>';

INTEGER_LITERAL:
	DECIMAL_LITERAL Integersuffix?
	| OCTAL_LITERAL Integersuffix?
	| HEXADECIMAL_LITERAL Integersuffix?
	| BINARY_LITERAL Integersuffix?
	;
DECIMAL_LITERAL: Nonzerodigit ('\''? Digit)*;
OCTAL_LITERAL: '0' ('\''? Octaldigit)*;
HEXADECIMAL_LITERAL: ('0x' | '0X') Hexadecimaldigit ('\''? Hexadecimaldigit)*;
BINARY_LITERAL: ('0b' | '0B') Binarydigit ('\''? Binarydigit)*;

KEYWORD
    : 'alignas'
    | 'alignof'
    | 'asm'
    | 'auto'
    | 'bool'
    | 'break'
    | 'case'
    | 'catch'
    | 'char'
    | 'char16_t'
    | 'char32_t'
    | 'class'
    | 'const'
    | 'constexpr'
    | 'const_cast'
    | 'continue'
    | 'decltype'
    | 'default'
    | 'delete'
    | 'do'
    | 'double'
    | 'dynamic_cast'
    | 'else'
    | 'enum'
    | 'explicit'
    | 'export'
    | 'extern'
    | 'false'
    | 'final'
    | 'float'
    | 'for'
    | 'friend'
    | 'goto'
    | 'if'
    | 'inline'
    | 'int'
    | 'long'
    | 'mutable'
    | 'namespace'
    | 'new'
    | 'noexcept'
    | 'nullptr'
    | 'operator'
    | 'override'
    | 'private'
    | 'protected'
    | 'public'
    | 'register'
    | 'reinterpret_cast'
    | 'return'
    | 'short'
    | 'signed'
    | 'sizeof'
    | 'static'
    | 'static_assert'
    | 'static_cast'
    | 'struct'
    | 'switch'
    | 'template'
    | 'this'
    | 'thread_local'
    | 'throw'
    | 'true'
    | 'try'
    | 'typedef'
    | 'typeid'
    | 'typename'
    | 'union'
    | 'unsigned'
    | 'using'
    | 'virtual'
    | 'void'
    | 'volatile'
    | 'wchar_t'
    | 'while'
    ;
OPERATORS
    : '('
    | ')'
    | '['
    | ']'
    | '{'
    | '}'
    | '+'
    | '-'
    | '*'
    | '%'
    | '^'
    | '&'
    | '|'
    | '~'
    | '!'
    | 'not'
    | '='
    | '+='
    | '-='
    | '*='
    | '/='
    | '%='
    | '^='
    | '&='
    | '|='
    | '<<='
    | '>>='
    | '=='
    | '!='
    | '<='
    | '>='
    | '&&'
    | 'and'
    | '||'
    | 'or'
    | '++'
    | '--'
    | ','
    | '->*'
    | '->'
    | '?'
    | ':'
    | '::'
    | ';'
    | '.*'
    | '...'
    ;

SLASH: '/';
DOT  : '.';

IDENTIFIER: Identifiernondigit (Identifiernondigit | Digit)*;
DIR_CHAR: ~[#\r\n];
ERROR: . -> channel(HIDDEN);

fragment Schar:
	~ ["\\\r\n]
	| Escapesequence
	| Universalcharactername;
fragment Integersuffix:
	Unsignedsuffix Longsuffix?
	| Unsignedsuffix Longlongsuffix?
	| Longsuffix Unsignedsuffix?
	| Longlongsuffix Unsignedsuffix?;
fragment Identifiernondigit: Nondigit | Universalcharactername;
fragment Nondigit: [a-zA-Z_];
fragment Digit: [0-9];
fragment Universalcharactername
    : '\\u' Hexquad
	| '\\U' Hexquad Hexquad;
fragment Hexquad: Hexadecimaldigit Hexadecimaldigit Hexadecimaldigit Hexadecimaldigit;
fragment Hexadecimaldigit: [0-9a-fA-F];
fragment Escapesequence:
	Simpleescapesequence
	| Octalescapesequence
	| Hexadecimalescapesequence;
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
	| '\\t'
	| '\\v';
fragment Octalescapesequence:
	'\\' Octaldigit
	| '\\' Octaldigit Octaldigit
	| '\\' Octaldigit Octaldigit Octaldigit;
fragment Hexadecimalescapesequence: '\\x' Hexadecimaldigit+;
fragment Octaldigit: [0-7];
fragment Nonzerodigit: [1-9];
fragment Binarydigit: [01];
fragment Unsignedsuffix: [uU];
fragment Longsuffix: [lL];
fragment Longlongsuffix: 'll' | 'LL';