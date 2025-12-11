///1&2&3&exitosamente


class TestControlIf {
    static void main() {
        var x = 10;
        if (x > 5) {
            debugPrint(1); // Debe imprimir
        } else {
            debugPrint(0);
        }

        if (x < 5) {
            debugPrint(0);
        } else {
            debugPrint(2); // Debe imprimir
        }

        if (x == 10) {
            debugPrint(3); // Debe imprimir
        }
    }
}
