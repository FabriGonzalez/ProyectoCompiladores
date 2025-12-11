///1000&exitosamente

class B extends A {
    A objA;

    void especialB(){
        debugPrint(1000);
    }
}

class A {
    int nro1;
    int nro2;
    B   objB;

    void initVars(){
        this.nro1 = 10;
        this.nro2 = 20;
        this.objB = new B();
    }

}

class Init{
    static void main()
    {
        var objB = new B();
        objB.especialB();

    }
}


