///30&10&1&-1&exitosamente


class TestAritmetica {
    static void main() {
        var x = 10;
        var y = 3;
        var z = (x + 5) * 2; // (10+5)*2 = 30
        debugPrint(z);
        z = z / y; // 30 / 3 = 10
        debugPrint(z);
        z = x % y; // 10 % 3 = 1
        debugPrint(z);
        z = -z; // -1
        debugPrint(z);
    }
}

