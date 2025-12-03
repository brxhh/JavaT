import java.lang.reflect.Field;
import java.util.Scanner;

public class StringReflection {
    public static void main() {
        Scanner scanner = new Scanner(System.in);
        String literalString = "Hello";
        System.out.println("Введіть будь-який рядок:");
        String inputString = new String(scanner.nextLine());

        System.out.println("\n--- До змін ---");
        System.out.println("Літерал: " + literalString);
        System.out.println("Введений: " + inputString);

        System.out.println("\nВведіть текст, на який хочете замінити вміст рядків:");
        String replacement = scanner.nextLine();

        try {
            modifyStringValue(literalString, replacement);
            modifyStringValue(inputString, replacement);
        } catch (Exception e) {
            System.err.println("Помилка рефлексії: " + e.getMessage());
            System.out.println("Якщо у вас Java 9+, додайте VM option: --add-opens java.base/java.lang=ALL-UNNAMED");
        }

        System.out.println("\n--- Після змін ---");
        System.out.println("Літерал: " + literalString);
        System.out.println("Введений: " + inputString);

        System.out.println("Перевірка ефекту пулу (новий літерал \"Hello\"): " + "Hello");
    }

    private static void modifyStringValue(String target, String newValue) throws Exception {
        Field valueField = String.class.getDeclaredField("value");
        valueField.setAccessible(true);

        Object value = valueField.get(target);

        if (value instanceof byte[]) {
            byte[] newBytes = newValue.getBytes();
            valueField.set(target, newBytes);

            try {
                Field hashField = String.class.getDeclaredField("hash");
                hashField.setAccessible(true);
                hashField.setInt(target, 0);
            } catch (Exception e) {
            }

        } else if (value instanceof char[]) {
            char[] newChars = newValue.toCharArray();
            valueField.set(target, newChars);
        }
    }
}