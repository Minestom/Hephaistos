grammar SNBT;

snbt: element
    EOF
    ;

element: byteNBT
             | floatNBT
             | doubleNBT
             | shortNBT
             | longNBT
             | intNBT
             | stringNBT
             | byteArray
             | intArray
             | longArray
             | list
             | compound
;

compound: '{' (namedElement (',' namedElement)*)? ','? '}';
namedElement: name=stringNBT ':' value=element;

list: '[' (element (',' element)*)? ','? ']';
byteArray: '[' 'B' ';' (byteNBT (',' byteNBT)* ','?)? ']';
intArray: '[' 'I' ';' (intNBT (',' intNBT)* ','?)? ']';
longArray: '[' 'L' ';' (longNBT (',' longNBT)* ','?)? ']';
doubleNBT: integerPart=integralNumber ('.' fractionalPart=INTEGER)? ('d'|'D');
floatNBT: integerPart=integralNumber ('.' fractionalPart=INTEGER)? ('f'|'F');
longNBT: LONG;
byteNBT: BYTE | BOOLEAN;
shortNBT: SHORT;
intNBT: integralNumber;
stringNBT: identifier | DoubleQuoteText | SingleQuoteText;
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