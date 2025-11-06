package model;

public class Triangle extends Shape {
    private final double base;
    private final double height;

    public Triangle(String color, double base, double height) {
        super(color);
        this.base = base;
        this.height = height;
    }

    @Override
    public double calcArea() {
        return 0.5 * base * height;
    }

    @Override
    public void draw() {
        System.out.println("Drawing triangle with color " + shapeColor);
    }

    @Override
    public String toString() {
        return "Triangle[color=" + shapeColor + ", base=" + base + ", height=" + height + ", area=" + calcArea() + "]";
    }
}