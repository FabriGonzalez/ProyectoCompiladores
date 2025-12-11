///1234&exitosamente

class A {
    void m1(int x){
        x = 1234;
        debugPrint(x);
    }
}

class Init{
    static void main()
    {
        var x = 10;
        var a = new A();
        a.m1(x);

    }
}


