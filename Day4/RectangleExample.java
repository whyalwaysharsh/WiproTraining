/*
Question 1: OOPS Rectangle
--------------------------
Create a Java class called "Rectangle" that represents a rectangle shape. 
The class should have instance variables for the length and width of the rectangle. 
It should also have methods to calculate the area and perimeter of the rectangle. 
Demonstrate usage by creating objects and invoking the methods.
*/

import java.util.Scanner;

class RectangleExample {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double length = sc.nextDouble();
        double width = sc.nextDouble();

        Rectangle rectangle1 = new Rectangle(length, width);
        double area1 = rectangle1.calculateArea();
        double perimeter1 = rectangle1.calculatePerimeter();

        System.out.println("Rectangle 1: ");
        System.out.println("Length: " + rectangle1.getLength());
        System.out.println("Width: " + rectangle1.getWidth());
        System.out.println("Area: " + area1);
        System.out.println("Perimeter: " + perimeter1);

        sc.close();
    }
}

class Rectangle {
    private double length;
    private double width;

    public Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double calculateArea() {
        return length * width;
    }

    public double calculatePerimeter() {
        return 2 * (length + width);
    }
}
