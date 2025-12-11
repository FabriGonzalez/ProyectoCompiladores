///10&exitosamente
class A{
    static void main(){
        var b = new B();
        b.m1();
    }

    static void m1(){
        debugPrint(1);
    }
}

class B{
    void m1(){
        this.m3().m2();
    }

    B m3(){
        return new B();
    }

    static void m2(){
        debugPrint(10);
    }
}