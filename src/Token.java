public class Token {
    String id;
    String lexeme;
    int lineNumber;

    public Token(String id, String lexeme, int lineNumber){
        this.id = id;
        this.lexeme = lexeme;
        this.lineNumber = lineNumber;
    }
}
