///1000&exitosamente

class B extends A {
    int privada;

   public B (int nroEspecial){
       privada = nroEspecial;
   }

    void especialB(){
        debugPrint(privada);
    }
}

class A {
    B objB;

    public A(int nroEspecial){
        objB = new B(nroEspecial);
    }
}

class Init{
    static void main()
    {
        var objA = new A(1000);

        debugPrint(objA.objB.privada);

    }
}


