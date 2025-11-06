import controller.ShapeController;
import model.Circle;
import model.Rectangle;
import model.Shape;
import model.Triangle;
import view.ShapeView;

void main() {
    ShapeView view = new ShapeView();
    ShapeController controller = new ShapeController();

    String[] colors = {"red", "green", "blue", "yellow", "black"};
    Random rand = new Random();

    Shape[] shapes = new Shape[10];
    for (int i = 0; i < shapes.length; i++) {
        int type = rand.nextInt(3);
        String color = colors[rand.nextInt(colors.length)];

        switch (type) {
            case 0 -> shapes[i] = new Rectangle(color, rand.nextInt(10) + 1, rand.nextInt(10) + 1);
            case 1 -> shapes[i] = new Triangle(color, rand.nextInt(10) + 1, rand.nextInt(10) + 1);
            case 2 -> shapes[i] = new Circle(color, rand.nextInt(10) + 1);
        }
    }

    view.displayShapes(shapes);
    view.displayMessage("\nTotal area of all shapes: " + controller.totalArea(shapes));
    view.displayMessage("Total area of circles: " + controller.totalAreaByType(shapes, Circle.class));

    controller.sortByArea(shapes);
    view.displayMessage("\n--- Sorted by area ---");
    view.displayShapes(shapes);

    controller.sortByColor(shapes);
    view.displayMessage("\n--- Sorted by color ---");
    view.displayShapes(shapes);
}