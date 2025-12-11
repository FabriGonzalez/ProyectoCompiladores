///

class A {
    public A(){
        var x = "constructorA";
        System.printSln(x);
    }

    void ma(){
        var x = "metodoA";
        System.printSln(x);
    }

}

class B extends A{
    void mb(){
        var x = "metodoB";
        System.printSln(x);
        ma();
    }
}

class C extends B {
    void mc(){
        var x = "metodoC";
        System.printSln(x);
        mb();
    }
}

class D extends C{
    void md(){
        var x = "metodoD";
        System.printSln(x);
        mc();
    }
}


class Test {

    static void main(){
        var objD = new D();
        objD.md();
    }

}

