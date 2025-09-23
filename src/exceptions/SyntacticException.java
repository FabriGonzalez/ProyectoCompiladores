package exceptions;
import model.Token;

public class SyntacticException extends Exception{
    public SyntacticException(Token tok, String tokenExpected){
        super("Error sintactico en linea " + tok.getLineNumber() +
                        ": se esperaba " +tokenExpected + " y se encontro: " + tok.getId() + "\n\n" +
                "[Error:" + tok.getLexeme() + "|" + tok.getLineNumber() + "]"
                );
    }
}
