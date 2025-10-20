package model.ast;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import model.*;

public class VarChainedNode extends ChainedNode{
    Token varTk;

    public VarChainedNode(Token tk){
        varTk = tk;
    }

    @Override
    public Type check(Type baseType) {
        /*Aca habria que ver si a la variable que se esta intentando acceder existe.
        Y tambien faltaria algun chequeo de si la clase a la que esta variable esta encadenada
        tiene esta misma variable.
         */
        if(!(baseType instanceof ReferenceType)){
            throw new SemanticException(varTk.getLineNumber(),
                    "No se puede invocar un método sobre un tipo no referencial: " + baseType,
                    varTk.getLexeme());
        }

        ReferenceType refType = (ReferenceType) baseType;
        Classs baseClass = SymbolTable.getInstance().getClass(refType.getName());

        if (baseClass == null) {
            throw new SemanticException(varTk.getLineNumber(),
                    "La clase " + refType.getName() + " no está definida.",
                    varTk.getLexeme());
        }

        Attribute baseAttribute = baseClass.getAttributeByName(varTk.getLexeme());
        if(baseAttribute != null){
            Type attrType = baseAttribute.getType();
            if(chain != null){
                return chain.check(attrType);
            } else {
                return attrType;
            }
        }

        throw new SemanticException(varTk.getLineNumber(),
                "La clase " + baseClass.getName() + " no tiene un atributo llamado '" + varTk.getLexeme() + "'.",
                varTk.getLexeme());

    }
}
