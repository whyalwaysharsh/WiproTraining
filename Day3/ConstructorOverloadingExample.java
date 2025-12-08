/*
Question 4: Constructor Overloading
-----------------------------------
Create one default constructor to initialize the fields with default values 
to calculate volume of the cube and one parameterized constructor to pass parameters and initialize them accordingly.

Sample Output:
Constructor without parameter
Volume is 1000.0
Constructor with parameter
Volume is 192.0
*/

import java.util.Scanner;

public class ConstructorOverloadingExample {
    private int length;
    private int width;
    private int height;

    public ConstructorOverloadingExample() {
        this.length = 10;
        this.width = 10;
        this.height = 10;
        System.out.println("Constructor without parameter");
        displayVolume();
    }

    public ConstructorOverloadingExample(int length, int width, int height) {
        this.length = length;
        this.width = width;
        this.height = height;
        System.out.println("Constructor with parameter");
        displayVolume();
    }

    public void displayVolume() {
        double volume = (double) this.length * this.width * this.height;
        System.out.println("Volume is " + volume);
    }

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        ConstructorOverloadingExample cube1 = new ConstructorOverloadingExample();

        int l = s.nextInt();
        int w = s.nextInt();
        int h = s.nextInt();

        ConstructorOverloadingExample cube2 = new ConstructorOverloadingExample(l, w, h);

        s.close();
    }
}
