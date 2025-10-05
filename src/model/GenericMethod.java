package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GenericMethod {
    String name;
    List<Param> params = new LinkedList<>();

    public GenericMethod(String n){
        name = n;
    }

    public String getName(){
        return name;
    }

    public void addParam(Param p){
        params.add(p);
    }
}