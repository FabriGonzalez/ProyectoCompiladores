///

class A {
    char caracterA;
    public A(){
        var x = "constructorA";
        System.printSln(x);
    }


    char getCaracter(){
        return caracterA;
    }

    void set(){
        caracterA = 'a';
    }

    void ma(){
        var x = "metodoA";
        System.printSln(x);
    }

}

class B extends A{
    char caracterB;


    char getCaracter(){
        return caracterB;
    }

    void set(){
        caracterB = 'b';
    }
    void mb(){
        var x = "metodoB";
        System.printSln(x);
        ma();
    }
    void ma(){
        var x = "B redefine a metodo ma de A";
        System.printSln(x);
    }
}


class D extends C{
    char caracterD;


    char getCaracter(){
        return caracterD;
    }

    void set(){
        caracterD = 'd';
    }

    void md(){
        var x = "metodoD";
        System.printSln(x);
        mc();
        mb();
        ma();
    }

    void mc(){
        var a = 'a';
        System.printCln(a);
    }

    void mb(){
        var x = "D redefine a metodo mb de B";
        System.printSln(x);
    }
}



class C extends B {
    char caracterC;

    char getCaracter(){
        return caracterC;
    }

    void set(){
        caracterC = 'c';
    }
    void mc(){
        var x = "metodoC";
        System.printSln(x);
        mb();
    }
}
class Test {

    static void main(){
        var objD = new D();
        var objA = new A();
        var objB = new B();
        var objC = new C();

        objD.set();
        objA.set();
        objB.set();
        objC.set();


        objD.md();

        System.printCln(objD.getCaracter());
        System.printCln(objC.getCaracter());
        System.printCln(objB.getCaracter());
        System.printCln(objA.getCaracter());

    }

}

