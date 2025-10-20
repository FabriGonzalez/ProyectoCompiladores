package model.ast;

public abstract class PrimaryNode extends OperandNode {
    ChainedNode chain;

    public void setChain(ChainedNode chain) {
        this.chain = chain;
    }

    public ChainedNode getChain() {
        return chain;
    }
}
