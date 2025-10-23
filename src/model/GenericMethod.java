package model;

import exceptions.SemanticException;
import model.ast.BlockNode;
import java.util.LinkedList;
import java.util.List;

public class GenericMethod {
    String name;
    List<Param> params = new LinkedList<>();
    int declaredLine;
    boolean hasBlock;
    BlockNode block;

    public GenericMethod(String n, int declaredLine){
        name = n;
        this.declaredLine = declaredLine;
    }

    public void setBlock(BlockNode b){
        block = b;
    }

    public int getDeclaredLine(){
        return declaredLine;
    }

    public String getName(){
        return name;
    }

    public void setHasBlock(){
        hasBlock = true;
    }


    public boolean getHasBlock(){
        return hasBlock;
    }

    public void addParam(Param p){
        for (Param param : params) {
            if (param.getName().equals(p.getName())) {
                throw new SemanticException(p.getDeclaredLine(), "El parametro con nombre " + p.getName() + " se repite", p.getName());
            }
        }
        params.add(p);
    }

    public Type getEffectiveReturnType() {
        return null;
    }

    public boolean isConstructor(){
        return false;
    }

    public Param getParamByName(String name){
        Param param = null;
        for (Param p : params){
            if(p.getName().equals(name)){
                param = p;
            }
        }
        return param;
    }

    public List<Param> getParams(){
        return params;
    }

    public void check(){
        if(this.block != null){
            this.block.check();
        }
    }
}