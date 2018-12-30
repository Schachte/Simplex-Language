package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static core.TokenType.*;

public class Scanner {
    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TokenType> keywords;

    static {
        // Words that are reserved to be checked against identifiers
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }

    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private void scanToken() {
        char c = consumeToken();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '[':
                addToken(LEFT_BRACKET);
                break;
            case ']':
                addToken(RIGHT_BRACKET);
                break;
            case '*':
                addToken(STAR);
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '\\':
                if (match('n')) {
                    addToken(NEWLINE);
                }
                break;
            case '/':
                // Comments can begin with a slash, so adding special handling
                if (match('/')) {
                    // A comment will go until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) consumeToken();
                } else {
                    addToken(SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    Simplex.error(line, "Unexpected character (column " + current + ")");
                }
                break;
        }
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) consumeToken();

        // Check if the current token is a reserved word
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);

        if (type == null) type = IDENTIFIER;

        addToken(type);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    /**
     * Helper method for valid digit check
     */
    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        // Keep consuming digit values until we hit EOL or a decimal delimiter
        while (isDigit(peek())) consumeToken();

        if (peek() == '.' && isDigit(peekNext())) {
            // We want to consume the decimal point
            consumeToken();
            while (isDigit(peek())) consumeToken();
        }

        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 >= source.length()) return '\0';
        return source.charAt(current + 1);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            consumeToken();
        }

        if (isAtEnd()) {
            Simplex.error(line, "Unterminated string.");
            return;
        }

        // Handle closing "
        consumeToken();

        // Trim surrounding quotes
        String value = source.substring(start + 1, current - 1);
        addToken(STRING, value);
    }

    /**
     * Similar to consumeToken, but we do not actually consume the char, we do a lookahead
     */
    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    /**
     * Helper function to differentiate lexemes with binary rulesets
     */
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false;

        current++;
        return true;
    }

    /**
     * Consumes the next character that exists within the source file. This method deals
     * specifically with input.
     *
     * @return the character that was consumed
     */
    private char consumeToken() {
        current++;
        return source.charAt(current - 1);
    }

    /**
     * Takes the text of the current lexeme and creates a new token for it. This method deals specifically with
     * output.
     *
     * @param type
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    /**
     * Obviously not super feasible in practice
     */
    private boolean isAtEnd() {

        // TODO: Slight optimization by assigning this func. call to static const
        return current >= source.length();
    }
}
