///10&exitosamente

class A {
    int numero;

    int m1(A objetoA){

        objetoA.numero = 20;
        return (1000 + objetoA.numero);

    }

}

class Init{
    static void main()
    { 
        var a = new A();
        debugPrint( a.m1(a) );
    }
}


