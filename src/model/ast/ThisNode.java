package model.ast;

import exceptions.SemanticException;
import model.*;
import sourcemanager.OutputManager;

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
            throw new SemanticException(thisTk.getLineNumber(),
                    "No se puede usar 'this' dentro de un método estático",
                    thisTk.getLexeme());
        }
        ReferenceType refType = new ReferenceType(new Token("idClase", associatedClass.getName(), associatedMethod.getDeclaredLine()));
        refType.setAssociatedClass(associatedClass);
        if(chain != null){
            return chain.check(refType);
        } else {
            return refType;
        }
    }

    @Override
    public void generate(boolean isLeftAssignment) {
        OutputManager.gen("LOAD 3 ; cargo this");
        if(chain != null){
            chain.generate(isLeftAssignment);
        }
    }
}
