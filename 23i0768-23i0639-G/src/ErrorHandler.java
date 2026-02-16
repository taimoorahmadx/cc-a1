// src/ErrorHandler.java
import java.util.ArrayList;
import java.util.List;

public class ErrorHandler {
    private static class ErrorRecord {
        String type;
        int line;
        int column;
        String lexeme;
        String reason;

        ErrorRecord(String type, int line, int column, String lexeme, String reason) {
            this.type = type;
            this.line = line;
            this.column = column;
            this.lexeme = lexeme;
            this.reason = reason;
        }
    }

    private List<ErrorRecord> errors = new ArrayList<>();

    public void reportError(String type, int line, int column, String lexeme, String reason) {
        errors.add(new ErrorRecord(type, line, column, lexeme, reason));
    }

    public void printErrors() {
        if (errors.isEmpty()) return;
        System.out.println("\n--- Lexical Errors ---");
        for (ErrorRecord e : errors) {
            System.out.printf("[%s] Line %d, Col %d: '%s' -> %s\n",
                e.type, e.line, e.column, e.lexeme, e.reason);
        }
    }
    
    public boolean hasErrors() {
        return !errors.isEmpty();
    }
}