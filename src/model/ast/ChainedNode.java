package model.ast;

import model.Type;

public abstract class ChainedNode {
    ChainedNode chain;

    public void setChain(ChainedNode chain){
        this.chain = chain;
    }

    public ChainedNode getChain(){
        return chain;
    }

    public abstract Type check(Type baseType);

    public ChainedNode getLastChain() {
        ChainedNode current = this;
        while (current.getChain() != null) {
            current = current.getChain();
        }
        return current;
    }

}
