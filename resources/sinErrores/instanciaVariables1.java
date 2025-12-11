///10&20&10&20&exitosamente

class A {
    int x;
    int y;

    void m1(int x){
        debugPrint(x);
    }

    void initVars(){
        x = 10;
        y = 20;
    }

}

class Init{
    static void main()
    {
        var a = new A();
        a.initVars();
        a.m1(a.x);
        a.m1(a.y);
        debugPrint(a.x);
        debugPrint(a.y);

    }
}


