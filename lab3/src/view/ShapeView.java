package view;

import model.Shape;

public class ShapeView {
    public void displayShapes(Shape[] shapes) {
        System.out.println("\n--- Shape list ---");
        for (Shape s : shapes) {
            System.out.println(s);
        }
    }

    public void displayMessage(String msg) {
        System.out.println(msg);
    }
}