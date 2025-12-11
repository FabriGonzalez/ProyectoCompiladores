///10&20&20&30&30&10&10&10&-10&-10&10&10&10&exitosamente


class TestAritmetica {

    static void m1(int x, int y){
        var local = 1000;
        var local2 = 2000;
    }

    static void main() {
        var x = 10;
        var y = 10;
        var z = 10;

        {
            var w = 20;
            var v = 20;
            var t = 20;
            debugPrint(x);
            debugPrint(v);
            debugPrint(t);
            m1(10,20);
            System.println();
        }

        m1(10,20);
        {
            var r = 30;
            var s = 30;
            debugPrint(r);
            debugPrint(s);
            debugPrint(x);

            m1(10,20);
            debugPrint(y);
            debugPrint(z);

            System.println();
        }

        var a = -10;
        var b = -10;
        debugPrint(a);
        debugPrint(b);

        m1(10,20);
        debugPrint(x);
        debugPrint(y);
        debugPrint(z);

        var nueva = 1000;
        debugPrint(nueva);
    }
}

