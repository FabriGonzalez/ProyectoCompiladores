package model.ast;

import Analyzers.SymbolTable;
import model.ReferenceType;
import model.Token;
import model.Type;

public class StringNode extends PrimaryNode{
    Token stringTk;

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
}
