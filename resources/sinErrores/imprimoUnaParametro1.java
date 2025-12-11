///1234&exitosamente

class A {
    void m1(int x){
        debugPrint(x);
    }
}

class Init{
    static void main()
    {
        var x = 1234;
        var a = new A();
        a.m1(x);

    }
}


