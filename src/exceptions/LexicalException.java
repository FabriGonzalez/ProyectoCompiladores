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

}
