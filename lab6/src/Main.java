import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Translator {
    private Map<String, String> dictionary = new HashMap<>();
    public void addWord(String english, String ukrainian) {
        dictionary.put(english.toLowerCase(), ukrainian.toLowerCase());
    }
    public String translatePhrase(String phrase) {
        StringBuilder translated = new StringBuilder();
        String[] words = phrase.toLowerCase().split(" ");

        for (String word : words) {
            if (dictionary.containsKey(word)) {
                translated.append(dictionary.get(word)).append(" ");
            } else {
                translated.append("[").append(word).append("] "); // if the word wasn’t found
            }
        }

        return translated.toString().trim();
    }
}

public class Main {
    static void main() {
        Scanner sc = new Scanner(System.in);
        Translator translator = new Translator();
        translator.addWord("hello", "привіт");
        translator.addWord("world", "світ");
        translator.addWord("my", "мій");
        translator.addWord("name", "ім'я");
        translator.addWord("is", "є");

        System.out.println("==== АНГЛО-УКРАЇНСЬКИЙ ПЕРЕКЛАДАЧ ====");
        while (true) {
            System.out.print("Бажаєте додати слово до словника? (yes/no): ");
            String answer = sc.nextLine().trim().toLowerCase();

            if (answer.equals("no")) break;

            System.out.print("Введіть англійське слово: ");
            String eng = sc.nextLine();

            System.out.print("Введіть український переклад: ");
            String ukr = sc.nextLine();

            translator.addWord(eng, ukr);
            System.out.println("✔ Слово додано!\n");
        }
        System.out.print("\nВведіть фразу англійською мовою: ");
        String phrase = sc.nextLine();

        String result = translator.translatePhrase(phrase);
        System.out.println("Переклад: " + result);
    }
}