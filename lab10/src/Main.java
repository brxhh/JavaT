import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.*;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static ResourceBundle bundle;
    private static Locale currentLocale;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setupLogger();
        currentLocale = new Locale("en", "US");
        loadBundle();

        logger.info("Application started.");
        if (bundle != null) {
            System.out.println(bundle.getString("msg.welcome"));
        }

        boolean running = true;
        while (running) {
            printMenu();
            String input = scanner.nextLine();

            try {
                int choice = Integer.parseInt(input);
                logger.fine("User selected option: " + choice);

                switch (choice) {
                    case 1:
                        performTask();
                        break;
                    case 2:
                        changeLanguage();
                        break;
                    case 3:
                        running = false;
                        System.out.println(bundle.getString("msg.bye"));
                        logger.info("Application closed by user.");
                        break;
                    default:
                        System.err.println(bundle.getString("error.invalid"));
                        logger.warning("User entered invalid menu option: " + choice);
                }
            } catch (NumberFormatException e) {
                System.err.println(bundle.getString("error.invalid"));
                logger.log(Level.SEVERE, "Invalid input format: " + input, e);
            }
        }
    }

    private static void setupLogger() {
        try {
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);

            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            try {
                consoleHandler.setEncoding("UTF-8");
            } catch (Exception ex) {
            }
            logger.addHandler(consoleHandler);

            FileHandler fileHandler = new FileHandler("app_log.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            fileHandler.setEncoding("UTF-8");
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }
    }

    private static void loadBundle() {
        try {
            bundle = ResourceBundle.getBundle("resources.location.messages", currentLocale);
        } catch (Exception e) {
            System.err.println("ПОМИЛКА: Не вдалося знайти файл ресурсів!");
            System.err.println("Переконайтеся, що папка 'resources' позначена як 'Resources Root',");
            System.err.println("а всередині неї є папка 'location' з файлами 'messages_uk.properties' та 'messages_en.properties'.");
            logger.severe("ResourceBundle missing: " + e.getMessage());
        }
    }

    private static void changeLanguage() {
        System.out.println("1. English");
        System.out.println("2. Українська");
        String choice = scanner.nextLine();

        if ("2".equals(choice)) {
            currentLocale = new Locale("uk", "UA");
        } else {
            currentLocale = new Locale("en", "US");
        }
        ResourceBundle.clearCache();
        loadBundle();

        System.out.println(bundle.getString("msg.lang_changed"));
        logger.config("Language changed to: " + currentLocale);
    }

    private static void printMenu() {
        if (bundle == null) return;
        System.out.println("\n" + bundle.getString("menu.title"));
        System.out.println(bundle.getString("menu.option1"));
        System.out.println(bundle.getString("menu.option2"));
        System.out.println(bundle.getString("menu.option3"));
        System.out.print(bundle.getString("menu.prompt") + " ");
    }

    private static void performTask() {
        double result = Math.random() * 100;
        System.out.println(bundle.getString("msg.calc") + String.format("%.2f", result));
        logger.info("Calculation task executed. Result: " + result);
    }
}