package exceptions;

public class LexicalException extends Exception{

    public LexicalException(String lexeme, int lineNumber) {
        super("Error léxico en línea "
                + lineNumber
                + ": " + lexeme
                + " no es un símbolo válido \n\n"
                + "[Error:" + lexeme + "|" + lineNumber + "]");
    }

    public LexicalException(String msg){
        super(msg);
    }

    public LexicalException(String lexeme, int lineNumber, int columnNumber, String currentLine) {
        super("Error léxico en línea "
                + lineNumber + " y en columna " + columnNumber
                + ": " + lexeme
                + " no es un símbolo válido \n" +
                "Detalle: " + currentLine + "\n" +
                " ".repeat(8 + columnNumber) + "^\n"
                + "[Error:" + lexeme + "|" + lineNumber + "]\n");
    }

}
