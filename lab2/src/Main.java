package src;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

record JournalEntry(String lastName, String firstName, Date birthDate, String phone, String address) {

    @Override
    public String toString() {
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        return lastName + " " + firstName +
                ", Дата нар.: " + df.format(birthDate) +
                ", Телефон: " + phone +
                ", Адреса: " + address;
    }
}

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    static {
        dateFormat.setLenient(false);
    }

    static void main() {
        List<JournalEntry> journal = new ArrayList<>();

        while (true) {
            System.out.println("\n--- Створення запису в журналі куратора ---");

            String lastName = readValidatedInput("Прізвище студента:", "^[А-Яа-яІіЇїЄєҐґA-Za-z]+$",
                    "Прізвище має містити тільки букви.");
            String firstName = readValidatedInput("Ім’я студента:", "^[А-Яа-яІіЇїЄєҐґA-Za-z]+$",
                    "Ім’я має містити тільки букви.");

            Date birthDate;
            while (true) {
                System.out.print("Дата народження (дд.ММ.рррр): ");
                String input = scanner.nextLine();
                try {
                    birthDate = dateFormat.parse(input);
                    break;
                } catch (ParseException e) {
                    System.out.println("Невірний формат дати!");
                }
            }

            String phone = readValidatedInput("Телефон (+380XXXXXXXXX):", "^\\+?\\d{10,13}$",
                    "Телефон має містити від 10 до 13 цифр (можливо з +).");

            String address;
            while (true) {
                System.out.print("Домашня адреса (вул., будинок, квартира): ");
                address = scanner.nextLine();
                if (!address.isBlank() && address.contains("вул.")) {
                    break;
                }
                System.out.println("Адреса повинна містити 'вул.' та номер будинку.");
            }

            journal.add(new JournalEntry(lastName, firstName, birthDate, phone, address));

            System.out.print("\nДодати ще запис? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("y")) break;
        }

        System.out.println("\n=== Усі записи журналу ===");
        for (JournalEntry entry : journal) {
            System.out.println(entry);
        }
    }

    private static String readValidatedInput(String prompt, String regex, String errorMessage) {
        while (true) {
            System.out.print(prompt + " ");
            String input = scanner.nextLine();
            if (Pattern.matches(regex, input)) {
                return input;
            }
            System.out.println(errorMessage);
        }
    }
}