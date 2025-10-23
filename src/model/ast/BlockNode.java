package model.ast;

import java.util.LinkedList;
import java.util.List;

public class BlockNode extends StatementNode{
    List<StatementNode> sents;
    BlockNode parent;
    List<LocalVarNode> localVars;

    public BlockNode(){
        localVars = new LinkedList<>();
    }

    public void setSents(List<StatementNode> l){
        sents = l;
    }

    public void check(){
        for (StatementNode sent : sents) {
            sent.check();
        }
    }

    public void setParentBlock(BlockNode b){
        parent = b;
    }

    public BlockNode getParentBlock(){
        return parent;
    }

    public void setLocalVar(LocalVarNode l){
        localVars.add(l);
    }

    public boolean hasLocalVar(String name){
        for(LocalVarNode v : localVars){
            if(v.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public LocalVarNode getLocalVar(String name){
        BlockNode currentBlock = this;
        while(currentBlock != null){
            if(currentBlock.hasLocalVar(name)){
                return currentBlock.getVar(name);
            }
            currentBlock = currentBlock.parent;
        }
        return null;
    }

    public boolean isLocalVarDeclared(String name){
        BlockNode currentBlock = this;
        while(currentBlock != null){
            if(currentBlock.hasLocalVar(name)){
                return true;
            }
            currentBlock = currentBlock.parent;
        }
        return false;
    }

    private LocalVarNode getVar(String name){
        for(LocalVarNode v : localVars){
            if(v.getName().equals(name)){
                return v;
            }
        }
        return null;
    }
}
