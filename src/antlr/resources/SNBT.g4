grammar SNBT;

snbt: element
    EOF
    ;

element: byte
             | float
             | double
             | short
             | long
             | int
             | string
             | byteArray
             | intArray
             | longArray
             | list
             | compound
;

compound: '{' (namedElement (',' namedElement)*)? ','? '}';
namedElement: name=string ':' value=element;

list: '[' (element (',' element)*)? ','? ']';
byteArray: '[' 'B' ';' (byte (',' byte)* ','?)? ']';
intArray: '[' 'I' ';' (int (',' int)* ','?)? ']';
longArray: '[' 'L' ';' (long (',' long)* ','?)? ']';
double: integerPart=integralNumber ('.' fractionalPart=INTEGER)? ('d'|'D');
float: integerPart=integralNumber ('.' fractionalPart=INTEGER)? ('f'|'F');
long: LONG;
byte: BYTE | BOOLEAN;
short: SHORT;
int: integralNumber;
string: identifier | DoubleQuoteText | SingleQuoteText;
identifier: IDENTIFIER_LETTERS+;

integralNumber: NEGATIVE_SIGN? INTEGER;

DoubleQuoteText: '"' ((~('"')| ('\\' '"'))*) '"';
SingleQuoteText: '\'' ((~('\'')| ('\\' '\''))*) '\'';

BOOLEAN: 'false' | 'true';
NEGATIVE_SIGN: '-';
INTEGER: [0-9]+;
LONG: [0-9]+ ('l'|'L');
BYTE: [0-9]+ ('b'|'B');
SHORT: [0-9]+ ('s'|'S');
IDENTIFIER_LETTERS: [a-zA-Z0-9_]+;
WS: (' ' | '\t' | '\r' | '\n')+ -> channel(HIDDEN);