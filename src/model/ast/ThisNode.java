package model.ast;

import exceptions.SemanticException;
import model.*;

public class ThisNode extends PrimaryNode{
    Token thisTk;
    Classs associatedClass;
    GenericMethod associatedMethod;

    public ThisNode(Token t, Classs c, GenericMethod m){
        thisTk = t;
        associatedClass = c;
        associatedMethod = m;
    }

    @Override
    public Token getExpressionToken() {
        return thisTk;
    }

    @Override
    public Type check() {
        if (associatedMethod instanceof Method m && m.isStatic()) {
            throw new SemanticException(0,
                    "No se puede usar 'this' dentro de un método estático",
                    "this");
        }
        ReferenceType refType = new ReferenceType(new Token("idClase", associatedClass.getName(), associatedMethod.getDeclaredLine()));
        refType.setAssociatedClass(associatedClass);
        if(chain != null){
            return chain.check(refType);
        } else {
            return refType;
        }
    }
}
