///10&20&10&20&exitosamente

class A {
    int x;
    int y;

    void m1(int x){
        debugPrint(x);
    }

    int m2(){
        return 1234;
    }
}

class Init{
    static void main()
    {
        var a = new A();
        a.x = 10;
        a.y = 20;
        a.m1(a.x);
        a.m1(a.y);
        debugPrint(a.x);
        debugPrint(a.y);

    }
}


