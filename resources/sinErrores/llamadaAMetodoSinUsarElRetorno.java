///10&exitosamente

class A {
    int numero;

    int m1(){

        m2(  m3() );


    }

    int m3(){
        return 10;
    }

    void m2(int x){
        debugPrint(x);
    }


}

class Init{
    static void main()
    { 
        var a = new A();
        a.m1();
    }
}


