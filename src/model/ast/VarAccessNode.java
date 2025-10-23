package model.ast;

import exceptions.SemanticException;
import model.*;

public class VarAccessNode extends PrimaryNode{
    Token varTk;
    Classs associatedClass;
    GenericMethod associatedMethod;
    BlockNode associatedBlock;

    public VarAccessNode(Token tk, Classs c, GenericMethod m, BlockNode b){
        varTk = tk;
        associatedClass = c;
        associatedMethod = m;
        associatedBlock = b;
    }

    @Override
    public Token getExpressionToken() {
        return varTk;
    }

    @Override
    public Type check() {
        Param param = associatedMethod.getParamByName(varTk.getLexeme());
        if(param != null){
            Type baseType = param.getType();
            if(chain != null){
                chain.check(baseType);
            } else {
                return baseType;
            }
        }

        Attribute attr = associatedClass.getAttributeByName(varTk.getLexeme());
        if(attr != null){
            Type baseType = attr.getType();
            if(chain != null){
                return chain.check(baseType);
            } else {
                return baseType;
            }
        }

        LocalVarNode localVar = associatedBlock.getLocalVar(varTk.getLexeme());
        if(localVar != null){
            Type baseType = localVar.getLocalVarType();
            if(chain != null){
                return chain.check(baseType);
            } else {
                return baseType;
            }
        }
        throw new SemanticException(varTk.getLineNumber(), "La variabe " + varTk.getLexeme() + " no esta declarada", varTk.getLexeme());

    }
}
