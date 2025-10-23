package model.ast;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import model.*;

import java.util.List;

public class ConstructorCallNode extends PrimaryNode{
    Token ctorTk;
    List<ExpressionNode> params;

    public ConstructorCallNode(Token tk, List<ExpressionNode> l){
        ctorTk = tk;
        params = l;
    }

    @Override
    public Token getExpressionToken() {
        return ctorTk;
    }

    public Type check(){
        SymbolTable ts = SymbolTable.getInstance();
        Classs associatedClass = ts.getClass(ctorTk.getLexeme());

        if(associatedClass == null){
            throw new SemanticException(ctorTk.getLineNumber(), "No hay una clase con nombre " + ctorTk.getLexeme(), ctorTk.getLexeme());
        }
        Constructor associatedCtor = associatedClass.getCtor();
        if(associatedCtor == null){
            throw new SemanticException(ctorTk.getLineNumber(), "La clase de nombre " + ctorTk.getLexeme() + " no tiene constructor", ctorTk.getLexeme());
        }

        List<Param> paramsExpected = associatedCtor.getParams();
        if(paramsExpected.size() != params.size()){
            throw new SemanticException(ctorTk.getLineNumber(), "Cantidad de parametros incorrecta en la llamada al constructor de nombre " + ctorTk.getLexeme(), ctorTk.getLexeme());
        }

        for(int i = 0; i < params.size(); i++){
            Type argType = params.get(i).check();
            Type expectedType = paramsExpected.get(i).getType();
            if(!argType.isCompatible(expectedType)){
                throw new SemanticException(ctorTk.getLineNumber(), "El argumento " + (i + 1) + " es de tipo " + argType + " y se esperaba " + expectedType, ctorTk.getLexeme());
            }
        }


        ReferenceType refType = new ReferenceType(new Token("idClase", associatedClass.getName(), associatedClass.getDeclaredLine()));
        refType.setAssociatedClass(associatedClass);

        if(chain != null){
            return chain.check(refType);
        } else{
            return refType;
        }
    }


}
