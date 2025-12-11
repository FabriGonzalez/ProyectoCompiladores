package model.ast;

import Analyzers.SymbolTable;
import model.ReferenceType;
import model.Token;
import model.Type;
import sourcemanager.OutputManager;

public class StringNode extends PrimaryNode{
    Token stringTk;
    private static int cont = 1;


    public StringNode(Token tk){
        stringTk = tk;
    }
    @Override
    public Token getExpressionToken() {
        return stringTk;
    }

    @Override
    public Type check() {
        SymbolTable ts = SymbolTable.getInstance();
        ReferenceType stringType = new ReferenceType(stringTk);
        stringType.setAssociatedClass(ts.getClass("String"));
        if(chain != null){
            return chain.check(stringType);
        } else {
            return stringType;
        }
    }

    @Override
    public void generate(boolean a) {
        OutputManager.gen(".DATA");
        String lblString = "string" + cont++;
        OutputManager.gen(lblString + ": DW " + stringTk.getLexeme() + ",0 ; apilo el string");
        OutputManager.gen(".CODE");
        OutputManager.gen("PUSH " + lblString);
        if(chain != null){
            chain.generate(a);
        }
    }
}
