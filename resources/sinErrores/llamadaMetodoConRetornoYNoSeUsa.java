///exitosamente

class A {
    void m1(int x){
        x = this.m2();
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
        a.m2();

    }
}


