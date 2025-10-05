package model;

public class Constructor extends GenericMethod{
    Classs associatedClass;

    public Constructor(Token tk, Classs aC){
        super(tk.getLexeme());

        associatedClass = aC;
    }

}
