import java.io.FileReader;
import java.io.IOException;

public class JFlexRunner {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java JFlexRunner <test_file>");
            return;
        }
        try {
            Yylex lexer = new Yylex(new FileReader(args[0]));
            Token t;
            while ((t = lexer.yylex()) != null) {
                if (t.type != TokenType.WHITESPACE && t.type != TokenType.COMMENT && t.type != TokenType.EOF) {
                    System.out.println(t.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}