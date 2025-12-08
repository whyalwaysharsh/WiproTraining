/*
Question 4: Volume Calculation using Constructor Overloading
------------------------------------------------------------
Write a program in Java to demonstrate constructor overloading 
for calculating cube and cuboid volume.

Class: ThreeDimensionShape
Constructors:
1. No-argument constructor → width=height=depth=0
2. One-argument constructor → width=height=depth=length
3. Three-argument constructor → width, height, depth

Method:
- volume() returns width*height*depth
*/

class ThreeDimensionShapeExample {
    public static void main(String args[]) {
        ThreeDimensionShape shape1 = new ThreeDimensionShape(5, 6, 7);
        ThreeDimensionShape shape2 = new ThreeDimensionShape();
        ThreeDimensionShape shape3 = new ThreeDimensionShape(8);

        double volume;

        volume = shape1.volume();
        System.out.println("Volume of shape1 is " + volume);

        volume = shape2.volume();
        System.out.println("Volume of shape2 is " + volume);

        volume = shape3.volume();
        System.out.println("Volume of shape3 is " + volume);
    }
}

class ThreeDimensionShape {
    double width, height, depth;

    ThreeDimensionShape(double w, double h, double d) {
        width = w;
        height = h;
        depth = d;
    }

    ThreeDimensionShape(double l) {
        width = height = depth = l;
    }

    ThreeDimensionShape() {
        width = height = depth = 0;
    }

    double volume() {
        return width * height * depth;
    }
}
