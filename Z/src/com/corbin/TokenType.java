package com.corbin;

public enum TokenType {

    IDENTIFIER, NUMBER, STRING,

    // Keywords
    INT, FLOAT, KW_STRING, TRUE, FALSE, FUNC, VOID, VAR, CONST, FOR, IN, WHILE, IF,
    ELSE, SWITCH, CASE, DEFAULT,

    // Operators
    NOT, PLUS, MINUS, TIMES, DIVIDE, EXP, MOD, INCREMENT, DECREMENT, GREATER, GREATEREQUAL,
    EQUAL, NOTEQUAL, LESS, LESSEQUAL, RETURNS, ASSIGN, PLUSASSIGN, MINUSASSIGN, TIMESASSIGN,
    DIVIDEASSIGN, MODASSIGN, EXPASSIGN, AND, OR, ELLIPSIS,

    // Punctuation
    OPENPAREN, CLOSEPAREN, OPENBRACE, CLOSEBRACE, COMMA, COLON, SEMICOLON,
    OPENBRACKET, CLOSEBRACKET,

    // End of file
    EOF
}
