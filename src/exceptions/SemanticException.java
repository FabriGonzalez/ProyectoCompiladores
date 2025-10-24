package exceptions;

public class SemanticException extends RuntimeException {
    public SemanticException(String message) {
        super(message);
    }

    public SemanticException(int lineNumber, String mensaje, String nombre){
        super("Error semántico en linea " + lineNumber +
                ": " + mensaje + "\n\n" +
                "[Error:" + nombre + "|" + lineNumber + "]"
        );
    }
}
