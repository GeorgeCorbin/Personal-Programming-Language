package com.corbin;

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer {

    // Instance Variables
    private final String source;
    private final ArrayList<Lexeme> lexemes = new ArrayList<>();

    private int currentPosition = 0;
    private int startOfCurrentLexeme = 0;
    private int lineNumber = 1;

    // Static Variables
    private static final HashMap<String, TokenType> keywords;

    // Keyword HashMap initialization
    static {
        keywords = new HashMap<>();

        keywords.put("var", TokenType.VAR);
        keywords.put("const", TokenType.CONST);

        keywords.put("int", TokenType.INT);
        keywords.put("float", TokenType.FLOAT);
        keywords.put("String", TokenType.KW_STRING);

        keywords.put("true", TokenType.TRUE);
        keywords.put("false", TokenType.FALSE);

        keywords.put("if", TokenType.IF);
        keywords.put("else", TokenType.ELSE);
        keywords.put("switch", TokenType.SWITCH);
        keywords.put("case", TokenType.CASE);
        keywords.put("default", TokenType.DEFAULT);

        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("not", TokenType.NOT);
        keywords.put("notequal", TokenType.NOTEQUAL);
        keywords.put("greater", TokenType.GREATER);
        keywords.put("greaterequal", TokenType.GREATEREQUAL);
        keywords.put("less", TokenType.LESS);
        keywords.put("lessequal", TokenType.LESSEQUAL);
        keywords.put("equal", TokenType.EQUAL);

        keywords.put("func", TokenType.FUNC);
        keywords.put("void", TokenType.VOID);
        keywords.put("for", TokenType.FOR);
        keywords.put("in", TokenType.IN);
        keywords.put("while", TokenType.WHILE);

    }

    // Lexer
    public Lexer(String source) {
        this.source = source;
    }

    // Utility Methods
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(currentPosition);
    }

    private char peekNext() {
        if (currentPosition + 1 >= source.length()) return '\0';
        return source.charAt(currentPosition + 1);
    }

    private boolean match(char expected) {
        if (isAtEnd() || source.charAt(currentPosition) != expected) return false;
        currentPosition++;
        return true;
    }

    private char advance() {
        char currentChar = source.charAt(currentPosition);
        if (currentChar == '\n' || currentChar == '\r') lineNumber++;
        currentPosition++;
        return currentChar;
    }

    private boolean isAtEnd() {
        return currentPosition >= source.length();
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    public ArrayList<Lexeme> lex() {
        while (!isAtEnd()) {
            startOfCurrentLexeme = currentPosition;
            Lexeme nextLexeme = getNextLexeme();
            if (nextLexeme != null) lexemes.add(nextLexeme);
        }
        lexemes.add(new Lexeme(TokenType.EOF, lineNumber));
        return lexemes;
    }

    private Lexeme getNextLexeme() {    // TODO
        char c = advance();

        switch (c) {
            // Ignore whitespace
            case ' ':
            case '\t':
            case '\n':
            case '\r':
                return null;

            // Single-Character Tokens
            case '(':
                return new Lexeme(TokenType.OPENPAREN, lineNumber);
            case ')':
                return new Lexeme(TokenType.CLOSEPAREN, lineNumber);
            case '{':
                return new Lexeme(TokenType.OPENBRACE, lineNumber);
            case '}':
                return new Lexeme(TokenType.CLOSEBRACE, lineNumber);
            case '[':
                return new Lexeme(TokenType.OPENBRACKET, lineNumber);
            case ']':
                return new Lexeme(TokenType.CLOSEBRACKET, lineNumber);
            case '=':
                return new Lexeme(TokenType.ASSIGN, lineNumber);
            case '>':
                return new Lexeme(TokenType.GREATER, lineNumber);
            case '<':
                return new Lexeme(TokenType.LESS, lineNumber);

            // Strictly-Two Character Tokens
            case '|':
                if (match('|')) return new Lexeme(DOUBLE_PIPE, lineNumber);
                else Z.error(lineNumber, "Missing second '|");
                break;

            // One- Or Two-Character Tokens
            case '*':
                return new Lexeme(match('=') ? TokenType.TIMESASSIGN : TokenType.TIMES, lineNumber);
            case '/':
                return new Lexeme(match('=') ? TokenType.DIVIDEASSIGN : TokenType.DIVIDE, lineNumber);
            case '&':
                return new Lexeme(match('&') ? TokenType.AND : TokenType.AND, lineNumber);
//            case '!':
//                return new Lexeme(match('!') ? TIMES_EQUAL : TokenType.BANG, lineNumber);
            case '+':
                return new Lexeme(match('+') ? TokenType.PLUSASSIGN : TokenType.PLUS, lineNumber);
//                if (match('+')) return new Lexeme(TokenType.PLUSASSIGN, lineNumber);
//                else return new Lexeme(TokenType.PLUS, lineNumber);

            // Strings
            case '"':
                return lexString();

            default:
                if (isDigit(c)) return lexNumber();
                else if (isAlpha(c)) return lexIdentifierOrKeyword();
                else Z.error(lineNumber, "Unexpected character: " + c);
        }
    }

    private Lexeme lexNumber() {
        boolean isInteger = true;
        while (isDigit(peek())) advance();

        // Look for a fractional part
        if (peek() == '.') {
            // Ensure there is a digit following the decimal point
            if (!isDigit(peekNext())) Z.error(lineNumber, "Malformed real number (ends in decimal point).");

            isInteger = false;
            // Consume the '.'
            advance();
            while (isDigit(peek())) advance();
        }
        String numberString = source.substring(startOfCurrentLexeme, currentPosition);
        if (isInteger) {
            int number = Integer.parseInt(numberString);
            return new Lexeme(TokenType.INT, number, lineNumber);
        } else {
            float number = Float.parseFloat(numberString);
            return new Lexeme(TokenType.FLOAT, number, lineNumber);
        }
    }

    private Lexeme lexString() {    // TODO
        return new Lexeme(TokenType.STRING, lineNumber);
    }


    private Lexeme lexIdentifierOrKeyword() {
        // Read until we run out of adjacent alphanumerics (these form an identifier)
        while (isAlphaNumeric(peek())) advance();
        String text = source.substring(startOfCurrentLexeme, currentPosition);

        // See if the suspected identifier is actually a keyword
        TokenType type = keywords.get(text);

        // If not, it is a user-defined identifier
        if (type == null)
            return new Lexeme(TokenType.IDENTIFIER, text, lineNumber);
        else
            return new Lexeme(type, lineNumber);
    }


}
