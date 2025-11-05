/*
Question 1: Arithmetic Operators in Java
----------------------------------------
Write a program that takes two numbers as input from the user and performs arithmetic operations on them using the arithmetic operators (sum, difference, product, quotient, remainder) in Java.

Sample Input:
Enter the first number: 10
Enter the second number: 5

Sample Output:
Sum: 15
Difference: 5
Product: 50
Quotient: 2
Remainder: 0
*/

import java.util.Scanner;

public class ArithmeticOperations {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the first number: ");
        int n1 = scanner.nextInt();

        System.out.print("Enter the second number: ");
        int n2 = scanner.nextInt();

        if (n2 == 0) {
            System.out.println("Division by zero is not allowed.");
        } else {
            int sum = n1 + n2;
            int difference = n1 - n2;
            int product = n1 * n2;
            int quotient = n1 / n2;
            int remainder = n1 % n2;

            System.out.println("Sum: " + sum);
            System.out.println("Difference: " + difference);
            System.out.println("Product: " + product);
            System.out.println("Quotient: " + quotient);
            System.out.println("Remainder: " + remainder);
        }

        scanner.close();
    }
}
