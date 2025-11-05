/*
Question 4: Generic Data Analysis using Java
--------------------------------------------
Implement a generic class DataAnalyzer that:
- Calculates the average of numeric data
- Finds the maximum element of comparable data
*/

import java.util.*;

public class DataAnalyzer<T extends Number & Comparable<T>> {

    private T[] data;

    // Constructor
    public DataAnalyzer(T[] data) {
        this.data = data;
    }

    // Method to calculate average
    public double calculateAverage() {
        double sum = 0.0;
        for (T value : data) {
            sum += value.doubleValue();
        }
        return sum / data.length;
    }

    // Method to find maximum
    public T findMaximum() {
        T max = data[0];
        for (T value : data) {
            if (value.compareTo(max) > 0) {
                max = value;
            }
        }
        return max;
    }

    // Data Summary
    public void dataSummary() {
        System.out.println("Data Summary:");
        System.out.println("Average: " + String.format("%.2f", calculateAverage()));
        System.out.println("Maximum: " + findMaximum());
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ch = sc.nextInt();   // choice: 1=Integer, 2=Double, 3=Short, 4=Float
        int size = sc.nextInt(); // array size

        if (ch == 1) {
            Integer[] intData = new Integer[size];
            for (int i = 0; i < size; i++) {
                intData[i] = sc.nextInt();
            }
            DataAnalyzer<Integer> analyzer = new DataAnalyzer<>(intData);
            analyzer.dataSummary();

        } else if (ch == 2) {
            Double[] doubleData = new Double[size];
            for (int i = 0; i < size; i++) {
                doubleData[i] = sc.nextDouble();
            }
            DataAnalyzer<Double> analyzer = new DataAnalyzer<>(doubleData);
            analyzer.dataSummary();

        } else if (ch == 3) {
            Short[] shortData = new Short[size];
            for (int i = 0; i < size; i++) {
                shortData[i] = sc.nextShort();
            }
            DataAnalyzer<Short> analyzer = new DataAnalyzer<>(shortData);
            analyzer.dataSummary();

        } else if (ch == 4) {
            Float[] floatData = new Float[size];
            for (int i = 0; i < size; i++) {
                floatData[i] = sc.nextFloat();
            }
            DataAnalyzer<Float> analyzer = new DataAnalyzer<>(floatData);
            analyzer.dataSummary();
        }
        sc.close();
    }
}
