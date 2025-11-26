void main() {
    Scanner scanner = new Scanner(System.in);
    IO.print("N: ");
    if (!scanner.hasNextInt()) return;
    int n = scanner.nextInt();
    IntPredicate isPrime = x -> x > 1 &&
            IntStream.rangeClosed(2, (int) Math.sqrt(x))
                    .noneMatch(i -> x % i == 0);
    IntUnaryOperator reverseNum = x ->
            Integer.parseInt(new StringBuilder(String.valueOf(x)).reverse().toString());
    long count = IntStream.rangeClosed(1, n)
            .filter(x -> isPrime.test(x) && isPrime.test(reverseNum.applyAsInt(x)))
            .count();

    IO.println(count);
}