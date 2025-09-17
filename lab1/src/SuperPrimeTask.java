void main() throws Exception {
    System.out.print("Введіть число (≤1000): ");
    int n = 0;
    int ch;

    // Зчитування числа з клавіатури
    while ((ch = System.in.read()) != '\n') {
        if (ch >= '0' && ch <= '9') {
            n = n * 10 + (ch - '0');
        }
    }

    if (n <= 0 || n > 1000) {
        System.out.println("Число повинно бути від 1 до 1000!");
        return;
    }

    int count = 0;
    for (int i = 2; i <= n; i++) {
        if (isPrime(i) && isPrime(reverse(i))) {
            count++;
        }
    }

    System.out.println("Кількість надпростих чисел до " + n + ": " + count);
}

static boolean isPrime(int x) {
    if (x < 2) return false;
    for (int i = 2; i * i <= x; i++) {
        if (x % i == 0) return false;
    }
    return true;
}

static int reverse(int x) {
    int r = 0;
    while (x > 0) {
        r = r * 10 + x % 10;
        x /= 10;
    }
    return r;
}
