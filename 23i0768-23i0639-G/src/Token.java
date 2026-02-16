// src/Token.java
public class Token {
    public TokenType type;
    public String lexeme;
    public int line;
    public int column;

    public Token(TokenType type, String lexeme, int line, int column) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return String.format("<%s, \"%s\", Line: %d, Col: %d>", type.name(), lexeme, line, column);
    }
}