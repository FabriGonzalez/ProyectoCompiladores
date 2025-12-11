///1234&exitosamente

class A {
    void m1(int x){
        x = m2();
        debugPrint(x);
    }

    int m2(){
        return 1234;
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


