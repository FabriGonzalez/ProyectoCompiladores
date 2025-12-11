///true&false&true&false&false&true&true&true&exitosamente


class TestLogica {
    static void main() {
        var x = 10;
        var y = 5;
        var t = true;
        var f = false;

        System.printBln(x > y); // true (GT)
        System.printBln(x < y); // false (LT)
        System.printBln(x == 10); // true (EQ)
        System.printBln(y != 5); // false (NE)

        System.printBln(t && f); // false (AND)
        System.printBln(t || f); // true (OR)
        System.printBln(!f); // true (NOT)

        System.printBln( (x >= y) && (y <= 5)); // true (GE, LE, AND)
    }
}

