package model.ast;

import exceptions.SemanticException;
import model.*;
import sourcemanager.OutputManager;

public class VarAccessNode extends PrimaryNode{
    Token varTk;
    Classs associatedClass;
    GenericMethod associatedMethod;
    BlockNode associatedBlock;
    VarEntry evar;

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
            evar = param;
            Type baseType = param.getType();
            if(chain != null){
                return chain.check(baseType);
            } else {
                return baseType;
            }
        }

        Attribute attr = associatedClass.getAttributeByName(varTk.getLexeme());
        if(attr != null){
            evar = attr;
            if (associatedMethod instanceof Method m && m.isStatic()) {
                throw new SemanticException(varTk.getLineNumber(),
                        "No se puede acceder a variables de instancia dentro de un método estático",
                        varTk.getLexeme());
            };
            Type baseType = attr.getType();
            if(chain != null){
                return chain.check(baseType);
            } else {
                return baseType;
            }
        }

        LocalVarNode localVar = associatedBlock.getLocalVar(varTk.getLexeme());
        if(localVar != null){
            evar = localVar;
            Type baseType = localVar.getLocalVarType();
            if(chain != null){
                return chain.check(baseType);
            } else {
                return baseType;
            }
        }
        throw new SemanticException(varTk.getLineNumber(), "La variabe " + varTk.getLexeme() + " no esta declarada", varTk.getLexeme());

    }

    public void generate(boolean isLeftAssignment) {
        if(evar instanceof Attribute){
            OutputManager.gen("LOAD 3 ; Cargo this desde var accesnode");
            if(!isLeftAssignment || chain != null){
                OutputManager.gen("LOADREF " + evar.getOffset());
            } else{
                OutputManager.gen("SWAP");
                OutputManager.gen("STOREREF " + evar.getOffset());
            }
        } else {
            if(!isLeftAssignment || chain != null){
                OutputManager.gen("LOAD " + evar.getOffset() + " ; Apilo el valor de la memoria del offset de " + varTk.getLexeme());
            } else {
                OutputManager.gen("STORE " + evar.getOffset());
            }
        }
        if(chain != null){
            chain.generate(isLeftAssignment);
        }
    }

    public int getOffset(){
        return evar.getOffset();
    }
}
