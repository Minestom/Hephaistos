grammar SNBT;

snbt: element
    EOF
    ;

element: byteNBT
             | floatNBT
             | shortNBT
             | longNBT
             | intNBT
             | doubleNBT
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
doubleNBT: DOUBLE;
floatNBT: FLOAT;
longNBT: LONG;
byteNBT: BYTE | BOOLEAN;
shortNBT: SHORT;
intNBT: INTEGER;
stringNBT: identifier | DoubleQuoteText | SingleQuoteText;
identifier: IDENTIFIER_LETTERS+;

DoubleQuoteText: '"' ((~('"')| ('\\' '"'))*) '"';
SingleQuoteText: '\'' ((~('\'')| ('\\' '\''))*) '\'';

BOOLEAN: 'false' | 'true';
NEGATIVE_SIGN: '-';
FLOAT: '-'? ([0-9]* ('.' [0-9]*)? | [0-9]+) ('f'|'F');
DOUBLE: '-'? ([0-9]* ('.' [0-9]*)? | [0-9]+) ('d'|'D')
        | '-'? [0-9]* '.' [0-9]+
        | '-'? [0-9]+ '.' [0-9]*;
INTEGER: '-'? [0-9]+;
LONG: '-'? [0-9]+ ('l'|'L');
BYTE: '-'? [0-9]+ ('b'|'B');
SHORT: '-'? [0-9]+ ('s'|'S');
WS: (' ' | '\t' | '\r' | '\n')+ -> channel(HIDDEN);
IDENTIFIER_LETTERS: [a-zA-Z0-9_]+;