grammar Py;

file: file_ EOF;

file_: (line NEWLINE)* line NEWLINE*;

line: print
    | if_ (else_ | )
    | assign
    | expr
    | while_
    | fun
    | return_
    | post;

post: 'post' '{' body '}' arithmetic;

return_: 'return' simpleExpr;

fun: 'fun' NAME '|' NAME+ '{' body '}';

body: NEWLINE* (file_ | );

while_: 'for' arithmetic '{' body '}';

if_: 'if' arithmetic '{' body '}';

else_: 'else' '{' body '}';

print: 'print' simpleExpr;

assign: id '=' expr;

id: NAME
    | '(' (id ',')+ id ')'
    ;

simpleExpr: read | arithmetic;

expr: simpleExpr
    | '(' (expr ',')+ expr ')';

arithmetic: NAME
    | NUM
    | BOOL
    | '(' arithmetic ')'
    | ('-' | '!' | '~') arithmetic
    | arithmetic ('*' | '/' | '%') arithmetic
    | arithmetic ('+' | '-') arithmetic
    | arithmetic ('<<' | '>>') arithmetic
    | arithmetic ('<' | '<=' | '>' | '>=') arithmetic
    | arithmetic ('==' | '!=') arithmetic
    | arithmetic '&' arithmetic
    | arithmetic '^' arithmetic
    | arithmetic '|' arithmetic
    | arithmetic '&&' arithmetic
    | arithmetic '||' arithmetic
    ;

read: 'readInt()';

WS: [ \t]+ -> skip;
NAME: [a-zA-Z] [a-zA-Z0-9]*;
NUM: '0' | [1-9] [0-9]*;
NEWLINE : [\r\n]+;
BOOL: 'true' | 'false';