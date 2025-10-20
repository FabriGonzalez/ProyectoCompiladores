package model;

import java.util.HashMap;
import java.util.Map;

public class TypeTable {
    private final Map<String,Type> types = new HashMap<>();

    public TypeTable(){
        types.put("int", new IntType(new Token("int", "int", 0)));
        types.put("char", new CharType(new Token("char", "char", 0)));
        types.put("boolean", new BooleanType(new Token("boolean", "boolean", 0)));
        types.put("void", new VoidType(new Token("void", "void", 0)));
    }

    public Type getOrCreateType(Token tk){
        if(types.containsKey(tk.getLexeme())){
            return types.get(tk.getLexeme());
        } else {
            Type newType = new ReferenceType(tk);
            types.put(newType.getName(), newType);
            return newType;
        }

    }
}
