// src/ManualScanner.java
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ManualScanner {
    private String input;
    private int pos = 0;
    private int line = 1;
    private int col = 1;
    private int startLine = 1;
    private int startCol = 1;

    // Statistics
    private int totalTokens = 0;
    private int[] tokenCounts = new int[TokenType.values().length];
    private int commentsRemoved = 0;

    private ErrorHandler errorHandler;
    private SymbolTable symbolTable;
    
    // Keywords
    private static final Set<String> KEYWORDS = Set.of(
        "start", "finish", "loop", "condition", "declare", "output", 
        "input", "function", "return", "break", "continue", "else"
    );

    // Booleans
    private static final Set<String> BOOLEANS = Set.of("true", "false");

    public ManualScanner(String sourceCode, ErrorHandler errorHandler, SymbolTable symbolTable) {
        this.input = sourceCode;
        this.errorHandler = errorHandler;
        this.symbolTable = symbolTable;
    }

    private char peek() {
        if (pos >= input.length()) return '\0';
        return input.charAt(pos);
    }

    private char peekNext() {
        if (pos + 1 >= input.length()) return '\0';
        return input.charAt(pos + 1);
    }

    private char advance() {
        char c = peek();
        pos++;
        if (c == '\n') {
            line++;
            col = 1;
        } else {
            col++;
        }
        return c;
    }
    
    private void recordTokenCount(TokenType type) {
        if(type != TokenType.WHITESPACE && type != TokenType.COMMENT && type != TokenType.ERROR) {
            totalTokens++;
        }
        tokenCounts[type.ordinal()]++;
    }

    public Token getNextToken() {
        while (pos < input.length()) {
            startLine = line;
            startCol = col;
            char c = peek();

            // 1. Whitespace
            if (Character.isWhitespace(c)) {
                advance();
                continue;
            }

            // 2. Comments
            if (c == '#') {
                if (peekNext() == '#') {
                    String comment = scanSingleLineComment();
                    commentsRemoved++;
                    return new Token(TokenType.COMMENT, comment, startLine, startCol);
                } else if (peekNext() == '*') {
                    String comment = scanMultiLineComment();
                    if(comment != null) {
                        commentsRemoved++;
                        return new Token(TokenType.COMMENT, comment, startLine, startCol);
                    }
                }
            }

            // 3. String Literal
            if (c == '"') return scanStringLiteral();

            // 4. Character Literal
            if (c == '\'') return scanCharLiteral();

            // 5. Operators (Multi-char first)
            if (isOperatorStart(c)) {
                Token opToken = scanOperator();
                if (opToken != null) return opToken;
            }

            // 6. Punctuators
            if ("(){}[],;:".indexOf(c) != -1) {
                advance();
                recordTokenCount(TokenType.PUNCTUATOR);
                return new Token(TokenType.PUNCTUATOR, String.valueOf(c), startLine, startCol);
            }

            // 7. Identifiers, Keywords, Booleans
            if (Character.isLetter(c)) {
                return scanIdentifierOrKeyword();
            }

            // 8. Numbers (Integer and Float)
            if (Character.isDigit(c) || ((c == '+' || c == '-') && Character.isDigit(peekNext()))) {
                return scanNumber();
            }

            // Error: Invalid Character
            char badChar = advance();
            errorHandler.reportError("Invalid Character", startLine, startCol, String.valueOf(badChar), "Character not recognized in language");
            return new Token(TokenType.ERROR, String.valueOf(badChar), startLine, startCol);
        }
        return new Token(TokenType.EOF, "EOF", line, col);
    }

    private String scanSingleLineComment() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && peek() != '\n') {
            sb.append(advance());
        }
        return sb.toString();
    }

    private String scanMultiLineComment() {
        StringBuilder sb = new StringBuilder();
        sb.append(advance()); // #
        sb.append(advance()); // *
        boolean closed = false;
        
        while (pos < input.length()) {
            char c = advance();
            sb.append(c);
            if (c == '*' && peek() == '#') {
                sb.append(advance()); // #
                closed = true;
                break;
            }
        }
        if (!closed) {
            errorHandler.reportError("Unclosed Comment", startLine, startCol, sb.toString(), "Multi-line comment lacks closing *#");
        }
        return sb.toString();
    }

    private Token scanStringLiteral() {
        StringBuilder sb = new StringBuilder();
        sb.append(advance()); // "
        boolean closed = false;
        
        while (pos < input.length()) {
            char c = peek();
            if (c == '"') {
                sb.append(advance());
                closed = true;
                break;
            } else if (c == '\\') {
                sb.append(advance()); // \
                sb.append(advance()); // escaped char
            } else if (c == '\n') {
                break; // Unterminated
            } else {
                sb.append(advance());
            }
        }
        
        if (!closed) {
            errorHandler.reportError("Malformed Literal", startLine, startCol, sb.toString(), "Unterminated string literal");
            return new Token(TokenType.ERROR, sb.toString(), startLine, startCol);
        }
        recordTokenCount(TokenType.STRING_LITERAL);
        return new Token(TokenType.STRING_LITERAL, sb.toString(), startLine, startCol);
    }

    private Token scanCharLiteral() {
        StringBuilder sb = new StringBuilder();
        sb.append(advance()); // '
        if (peek() == '\\') {
            sb.append(advance());
            sb.append(advance());
        } else {
            sb.append(advance());
        }
        
        if (peek() == '\'') {
            sb.append(advance());
            recordTokenCount(TokenType.CHAR_LITERAL);
            return new Token(TokenType.CHAR_LITERAL, sb.toString(), startLine, startCol);
        } else {
            while (pos < input.length() && peek() != '\'' && peek() != '\n') {
                sb.append(advance());
            }
            if (peek() == '\'') sb.append(advance());
            errorHandler.reportError("Malformed Literal", startLine, startCol, sb.toString(), "Invalid character literal format");
            return new Token(TokenType.ERROR, sb.toString(), startLine, startCol);
        }
    }

    private Token scanIdentifierOrKeyword() {
        StringBuilder sb = new StringBuilder();
        char first = advance();
        sb.append(first);
        
        while (pos < input.length() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            sb.append(advance());
        }
        
        String lexeme = sb.toString();
        
        if (KEYWORDS.contains(lexeme)) {
            recordTokenCount(TokenType.KEYWORD);
            return new Token(TokenType.KEYWORD, lexeme, startLine, startCol);
        }
        if (BOOLEANS.contains(lexeme)) {
            recordTokenCount(TokenType.BOOLEAN_LITERAL);
            return new Token(TokenType.BOOLEAN_LITERAL, lexeme, startLine, startCol);
        }
        
        // Identifier rules: Starts with Uppercase, length <= 31
        if (!Character.isUpperCase(first)) {
            errorHandler.reportError("Invalid Identifier", startLine, startCol, lexeme, "Must start with uppercase letter");
            return new Token(TokenType.ERROR, lexeme, startLine, startCol);
        }
        if (lexeme.length() > 31) {
            errorHandler.reportError("Invalid Identifier", startLine, startCol, lexeme, "Exceeds 31 characters");
            return new Token(TokenType.ERROR, lexeme, startLine, startCol);
        }
        
        recordTokenCount(TokenType.IDENTIFIER);
        symbolTable.insert(lexeme, "Identifier", startLine);
        return new Token(TokenType.IDENTIFIER, lexeme, startLine, startCol);
    }

    private Token scanNumber() {
        StringBuilder sb = new StringBuilder();
        if (peek() == '+' || peek() == '-') {
            sb.append(advance());
        }
        
        while (pos < input.length() && Character.isDigit(peek())) {
            sb.append(advance());
        }
        
        boolean isFloat = false;
        if (peek() == '.') {
            isFloat = true;
            sb.append(advance());
            int fracCount = 0;
            while (pos < input.length() && Character.isDigit(peek())) {
                sb.append(advance());
                fracCount++;
            }
            if (fracCount == 0 || fracCount > 6) {
                errorHandler.reportError("Malformed Literal", startLine, startCol, sb.toString(), "Float must have 1-6 decimal digits");
            }
        }
        
        if (peek() == 'e' || peek() == 'E') {
            isFloat = true;
            sb.append(advance());
            if (peek() == '+' || peek() == '-') {
                sb.append(advance());
            }
            if (!Character.isDigit(peek())) {
                errorHandler.reportError("Malformed Literal", startLine, startCol, sb.toString(), "Invalid exponent in float");
            }
            while (pos < input.length() && Character.isDigit(peek())) {
                sb.append(advance());
            }
        }
        
        // Check for multiple decimals (error)
        if (peek() == '.') {
            while(pos < input.length() && (Character.isDigit(peek()) || peek() == '.')) {
                sb.append(advance());
            }
            errorHandler.reportError("Malformed Literal", startLine, startCol, sb.toString(), "Multiple decimals in literal");
            return new Token(TokenType.ERROR, sb.toString(), startLine, startCol);
        }

        TokenType type = isFloat ? TokenType.FLOAT_LITERAL : TokenType.INTEGER_LITERAL;
        recordTokenCount(type);
        return new Token(type, sb.toString(), startLine, startCol);
    }

    private boolean isOperatorStart(char c) {
        return "+-*/%!=<>&|".indexOf(c) != -1;
    }

    private Token scanOperator() {
        char c = advance();
        String op = String.valueOf(c);
        char next = peek();
        
        if (c == '*' && next == '*') { op = "**"; advance(); }
        else if (c == '=' && next == '=') { op = "=="; advance(); }
        else if (c == '!' && next == '=') { op = "!="; advance(); }
        else if (c == '<' && next == '=') { op = "<="; advance(); }
        else if (c == '>' && next == '=') { op = ">="; advance(); }
        else if (c == '&' && next == '&') { op = "&&"; advance(); }
        else if (c == '|' && next == '|') { op = "||"; advance(); }
        else if (c == '+' && next == '+') { op = "++"; advance(); }
        else if (c == '-' && next == '-') { op = "--"; advance(); }
        else if (c == '+' && next == '=') { op = "+="; advance(); }
        else if (c == '-' && next == '=') { op = "-="; advance(); }
        else if (c == '*' && next == '=') { op = "*="; advance(); }
        else if (c == '/' && next == '=') { op = "/="; advance(); }
        
        recordTokenCount(TokenType.OPERATOR);
        return new Token(TokenType.OPERATOR, op, startLine, startCol);
    }

    public void displayStatistics() {
        System.out.println("\n--- Scanner Statistics ---");
        System.out.println("Lines processed: " + (line - 1));
        System.out.println("Total valid tokens: " + totalTokens);
        System.out.println("Comments removed: " + commentsRemoved);
        for (TokenType type : TokenType.values()) {
            if (tokenCounts[type.ordinal()] > 0) {
                System.out.printf("  %s: %d\n", type.name(), tokenCounts[type.ordinal()]);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("Usage: java ManualScanner <source_file>");
            return;
        }
        String sourceCode = new String(Files.readAllBytes(Paths.get(args[0])));
        ErrorHandler errorHandler = new ErrorHandler();
        SymbolTable symbolTable = new SymbolTable();
        ManualScanner scanner = new ManualScanner(sourceCode, errorHandler, symbolTable);
        
        List<Token> tokens = new ArrayList<>();
        Token t;
        do {
            t = scanner.getNextToken();
            if (t.type != TokenType.WHITESPACE && t.type != TokenType.COMMENT && t.type != TokenType.EOF) {
                tokens.add(t);
                System.out.println(t.toString());
            }
        } while (t.type != TokenType.EOF);
        
        scanner.displayStatistics();
        symbolTable.printTable();
        errorHandler.printErrors();
    }
}