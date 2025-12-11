///0&1&2&3&exitosamente

class TestControlWhile {
    static void main() {
        var i = 0;
        while (i < 3) {
            debugPrint(i);
            i = i + 1;
        }
        debugPrint(i); // Debe ser 3
    }
}
