package controller;

import model.Shape;
import java.util.Arrays;
import java.util.Comparator;

public class ShapeController {
    public double totalArea(Shape[] shapes) {
        return Arrays.stream(shapes)
                .mapToDouble(Shape::calcArea)
                .sum();
    }

    public double totalAreaByType(Shape[] shapes, Class<?> clazz) {
        return Arrays.stream(shapes)
                .filter(clazz::isInstance)
                .mapToDouble(Shape::calcArea)
                .sum();
    }

    public void sortByArea(Shape[] shapes) {
        Arrays.sort(shapes, Comparator.comparingDouble(Shape::calcArea));
    }

    public void sortByColor(Shape[] shapes) {
        Arrays.sort(shapes, Comparator.comparing(Shape::getShapeColor));
    }
}