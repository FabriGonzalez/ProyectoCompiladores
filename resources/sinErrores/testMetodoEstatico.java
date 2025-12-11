///30&7&exitosamente

class TestMetodosEstaticos {

    static int add(int a, int b) {
        return a + b;
    }

    static void printVal(int val) {
        debugPrint(val); // Método void
    }

    static void main() {
        var x = 10;
        var y = 20;
        var z = add(x, y); // Prueba valor de retorno
        printVal(z); // Prueba parámetros (30)
        printVal(add(5, 2)); // Prueba llamada anidada (7)
    }
}