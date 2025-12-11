package model.ast;

import Analyzers.SymbolTable;
import exceptions.SemanticException;
import model.*;
import sourcemanager.OutputManager;

public class VarChainedNode extends ChainedNode{
    Token varTk;
    VarEntry eVar;

    public VarChainedNode(Token tk){
        varTk = tk;
    }

    @Override
    public Type check(Type baseType) {
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
        eVar = baseAttribute;
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

    @Override
    public void generate(boolean isLeftAssignment) {
        if(!isLeftAssignment || chain != null){
            OutputManager.gen("LOADREF " + eVar.getOffset());
        } else{
            OutputManager.gen("SWAP");
            OutputManager.gen("STOREREF " + eVar.getOffset());
        }
        if(chain != null){
            chain.generate(isLeftAssignment);
        }
    }

    public int getOffset(){
        return eVar.getOffset();
    }
}
