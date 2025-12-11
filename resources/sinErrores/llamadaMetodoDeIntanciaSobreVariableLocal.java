///20&exitosamente

class A{
    int x;
    int m1(){
        return 20;
    }
    int m2(int numero){
        return numero;
    }
}


class Init{
    static void main()
    {
        var a = new A();
        var x = a.m2(20);
        debugPrint(x);
    }
}


