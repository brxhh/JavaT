import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.*;

// ===== SHAPES =====
abstract class Shape implements Drawable, Serializable {
    private static final long serialVersionUID = 1L;
    protected String shapeColor;

    public Shape(String shapeColor) { this.shapeColor = shapeColor; }

    public String getShapeColor() { return shapeColor; }

    public abstract double calcArea();

    @Override
    public String toString() {
        return "Shape[color=" + shapeColor + "]";
    }
}

interface Drawable { void draw(); }

class Rectangle extends Shape {
    private static final long serialVersionUID = 1L;
    private double width;
    private double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double calcArea() { return width * height; }

    @Override
    public void draw() { System.out.println("Drawing rectangle color=" + shapeColor); }

    @Override
    public String toString() {
        return String.format("Rectangle[color=%s, w=%.2f, h=%.2f, area=%.2f]",
                shapeColor, width, height, calcArea());
    }
}

class Triangle extends Shape {
    private static final long serialVersionUID = 1L;
    private double base;
    private double height;

    public Triangle(String color, double base, double height) {
        super(color);
        this.base = base;
        this.height = height;
    }

    @Override
    public double calcArea() { return 0.5 * base * height; }

    @Override
    public void draw() { System.out.println("Drawing triangle color=" + shapeColor); }

    @Override
    public String toString() {
        return String.format("Triangle[color=%s, base=%.2f, h=%.2f, area=%.2f]",
                shapeColor, base, height, calcArea());
    }
}

class Circle extends Shape {
    private static final long serialVersionUID = 1L;
    private double radius;

    public Circle(String color, double radius) {
        super(color);
        this.radius = radius;
    }

    @Override
    public double calcArea() { return Math.PI * radius * radius; }

    @Override
    public void draw() { System.out.println("Drawing circle color=" + shapeColor); }

    @Override
    public String toString() {
        return String.format("Circle[color=%s, r=%.2f, area=%.2f]",
                shapeColor, radius, calcArea());
    }
}

// ===== FILE MANAGER (Task1 + Serialization for Task2) =====
class FileManager {
    public static void saveShapes(Shape[] shapes, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeInt(shapes.length);
            for (Shape s : shapes) oos.writeObject(s);
        }
    }

    public static Shape[] loadShapes(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            int n = ois.readInt();
            Shape[] arr = new Shape[n];
            for (int i = 0; i < n; i++) arr[i] = (Shape) ois.readObject();
            return arr;
        }
    }

    // ==== Task1 ====
    public static Map.Entry<String,Integer> findLineWithMaxWords(String filePath) throws IOException {
        String bestLine = null;
        int bestCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                int cnt = line.trim().isEmpty() ? 0 : line.trim().split("\\s+").length;
                if (cnt > bestCount) {
                    bestCount = cnt;
                    bestLine = line;
                }
            }
        }
        return new AbstractMap.SimpleEntry<>(bestLine == null ? "" : bestLine, bestCount);
    }
}

// ===== ENCRYPTOR (Task3) =====
class Encryptor {
    public static void encryptBinaryFile(String inputPath, String outputPath, char keyChar) throws IOException {
        int key = keyChar;
        try (FilterOutputStream fos = new FilterOutputStream(new FileOutputStream(outputPath));
             FileInputStream fis = new FileInputStream(inputPath)) {
            int b;
            while ((b = fis.read()) != -1) fos.write((b + key) & 0xFF);
        }
    }

    static class MyFilterInputStream extends FilterInputStream {
        public MyFilterInputStream(InputStream in) {
            super(in);
        }
    }

    public static void decryptBinaryFile(String inputPath, String outputPath, char keyChar) throws IOException {
        int key = keyChar;
        try (MyFilterInputStream fis = new MyFilterInputStream(new FileInputStream(inputPath));
             FileOutputStream fos = new FileOutputStream(outputPath)) {

            int b;
            while ((b = fis.read()) != -1) {
                fos.write((b - key) & 0xFF);
            }
        }
    }

    public static void encryptTextFile(String inputPath, String outputPath, char keyChar) throws IOException {
        int key = keyChar;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath, StandardCharsets.UTF_8));
             FilterWriter writer = new FilterWriter(new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {}) {
            int ch;
            while ((ch = reader.read()) != -1) writer.write((char) (ch + key));
        }
    }

    public static void decryptTextFile(String inputPath, String outputPath, char keyChar) throws IOException {
        int key = keyChar;
        try (FilterReader reader = new FilterReader(new BufferedReader(new FileReader(inputPath, StandardCharsets.UTF_8))) {};
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath, StandardCharsets.UTF_8))) {
            int ch;
            while ((ch = reader.read()) != -1) writer.write((char) (ch - key));
        }
    }
}

// ===== HTML TAG COUNTER (Task4) =====
class HTMLTagCounter {
    public static Map<String, Integer> countTagsFromURL(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Java TagCounter");

        Map<String, Integer> counts = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            Pattern p = Pattern.compile("<\\s*([a-zA-Z0-9]+)");
            while ((line = br.readLine()) != null) {
                Matcher m = p.matcher(line);
                while (m.find()) {
                    String tag = m.group(1).toLowerCase();
                    counts.put(tag, counts.getOrDefault(tag, 0) + 1);
                }
            }
        }
        return counts;
    }

    public static List<Map.Entry<String,Integer>> sortLexicographically(Map<String,Integer> map) {
        List<Map.Entry<String,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getKey));
        return list;
    }

    public static List<Map.Entry<String,Integer>> sortByFrequency(Map<String,Integer> map) {
        List<Map.Entry<String,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry<String,Integer>::getValue)
                .thenComparing(Map.Entry::getKey));
        return list;
    }
}

// ===== CONTROLLER / VIEW =====
class ShapeController {
    public double totalArea(Shape[] shapes) {
        return Arrays.stream(shapes).mapToDouble(Shape::calcArea).sum();
    }

    public double totalAreaByType(Shape[] shapes, Class<?> type) {
        return Arrays.stream(shapes).filter(type::isInstance).mapToDouble(Shape::calcArea).sum();
    }

    public void sortByArea(Shape[] shapes) {
        Arrays.sort(shapes, Comparator.comparingDouble(Shape::calcArea));
    }

    public void sortByColor(Shape[] shapes) {
        Arrays.sort(shapes, Comparator.comparing(Shape::getShapeColor));
    }
}

class ShapeView {
    public void displayShapes(Shape[] shapes) {
        for (Shape s : shapes) System.out.println(s);
    }
    public void displayMessage(String msg) { System.out.println(msg); }
}

// ===== MAIN MENU (All Tasks) =====
public class Main {
    private static final Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);

    public static void main(String[] args) {
        Shape[] shapes = createSampleShapes();
        ShapeController controller = new ShapeController();
        ShapeView view = new ShapeView();

        while (true) {
            System.out.println("\n=== Меню ===");
            System.out.println("1 - Task1: рядок з найбільшою кількістю слів");
            System.out.println("2 - Task2: робота з Shape");
            System.out.println("3 - Task3: шифрування/дешифрування");
            System.out.println("4 - Task4: підрахунок HTML-тегів по URL");
            System.out.println("0 - Вихід");
            System.out.print(">> ");

            switch (sc.nextLine().trim()) {
                case "1" -> doTask1();
                case "2" -> doTask2(shapes, controller, view);
                case "3" -> doTask3();
                case "4" -> doTask4();
                case "0" -> { return; }
            }
        }
    }

    private static void doTask1() {
        System.out.print("Файл: ");
        String path = sc.nextLine();
        try {
            var res = FileManager.findLineWithMaxWords(path);
            System.out.println("Максимум слів: " + res.getValue());
            System.out.println(res.getKey());
        } catch (IOException e) { System.out.println(e.getMessage()); }
    }

    private static void doTask2(Shape[] shapes, ShapeController controller, ShapeView view) {
        while (true) {
            System.out.println("\n--- Task2 ---");
            System.out.println("a - показати");
            System.out.println("b - загальна площа");
            System.out.println("c - площа по типу");
            System.out.println("d - сортувати за площею");
            System.out.println("e - сортувати за кольором");
            System.out.println("s - зберегти");
            System.out.println("l - завантажити");
            System.out.println("x - назад");
            System.out.print(">> ");

            String cmd = sc.nextLine().trim();
            try {
                switch (cmd) {
                    case "a" -> view.displayShapes(shapes);
                    case "b" -> System.out.println("Total = " + controller.totalArea(shapes));
                    case "c" -> {
                        System.out.print("Тип (Rectangle/Triangle/Circle): ");
                        String t = sc.nextLine().trim();
                        Class<?> cls = switch (t.toLowerCase()) {
                            case "rectangle" -> Rectangle.class;
                            case "triangle" -> Triangle.class;
                            case "circle" -> Circle.class;
                            default -> null;
                        };
                        if (cls != null)
                            System.out.println(controller.totalAreaByType(shapes, cls));
                    }
                    case "d" -> controller.sortByArea(shapes);
                    case "e" -> controller.sortByColor(shapes);
                    case "s" -> {
                        System.out.print("Файл: ");
                        FileManager.saveShapes(shapes, sc.nextLine());
                    }
                    case "l" -> {
                        System.out.print("Файл: ");
                        shapes = FileManager.loadShapes(sc.nextLine());
                    }
                    case "x" -> { return; }
                }
            } catch (Exception ex) { System.out.println(ex.getMessage()); }
        }
    }

    private static void doTask3() {
        while (true) {
            System.out.println("\n--- Task3 ---");
            System.out.println("1 - encrypt (binary)");
            System.out.println("2 - decrypt (binary)");
            System.out.println("3 - encrypt (text)");
            System.out.println("4 - decrypt (text)");
            System.out.println("0 - back");
            System.out.print(">> ");

            String c = sc.nextLine().trim();
            try {
                switch (c) {
                    case "1" -> {
                        System.out.print("Вхід: "); String in = sc.nextLine();
                        System.out.print("Вихід: "); String out = sc.nextLine();
                        System.out.print("Ключ: "); char key = sc.nextLine().charAt(0);
                        Encryptor.encryptBinaryFile(in, out, key);
                    }
                    case "2" -> {
                        System.out.print("Вхід: "); String in = sc.nextLine();
                        System.out.print("Вихід: "); String out = sc.nextLine();
                        System.out.print("Ключ: "); char key = sc.nextLine().charAt(0);
                        Encryptor.decryptBinaryFile(in, out, key);
                    }
                    case "3" -> {
                        System.out.print("Вхід: "); String in = sc.nextLine();
                        System.out.print("Вихід: "); String out = sc.nextLine();
                        System.out.print("Ключ: "); char key = sc.nextLine().charAt(0);
                        Encryptor.encryptTextFile(in, out, key);
                    }
                    case "4" -> {
                        System.out.print("Вхід: "); String in = sc.nextLine();
                        System.out.print("Вихід: "); String out = sc.nextLine();
                        System.out.print("Ключ: "); char key = sc.nextLine().charAt(0);
                        Encryptor.decryptTextFile(in, out, key);
                    }
                    case "0" -> { return; }
                }
            } catch (Exception e) { System.out.println(e.getMessage()); }
        }
    }

    private static void doTask4() {
        System.out.print("URL: ");
        String url = sc.nextLine();
        try {
            Map<String,Integer> counts = HTMLTagCounter.countTagsFromURL(url);

            System.out.println("\nЛексикографічно:");
            HTMLTagCounter.sortLexicographically(counts)
                    .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));

            System.out.println("\nЗа частотою:");
            HTMLTagCounter.sortByFrequency(counts)
                    .forEach(e -> System.out.println(e.getKey() + " -> " + e.getValue()));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Shape[] createSampleShapes() {
        String[] colors = {"Red", "Green", "Blue", "Yellow", "Black"};
        Random r = new Random();
        Shape[] arr = new Shape[10];
        for (int i = 0; i < arr.length; i++) {
            int t = r.nextInt(3);
            String c = colors[r.nextInt(colors.length)];
            switch (t) {
                case 0 -> arr[i] = new Rectangle(c, 1 + r.nextInt(10), 1 + r.nextInt(10));
                case 1 -> arr[i] = new Triangle(c, 1 + r.nextInt(10), 1 + r.nextInt(10));
                default -> arr[i] = new Circle(c, 1 + r.nextInt(9));
            }
        }
        return arr;
    }
}