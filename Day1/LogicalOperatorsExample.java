/*
Question 2: Logical Operators in Java
-------------------------------------
Write a program that takes two boolean values as input and performs logical operations (AND, OR) on them using the logical operators & print the result.

Sample Input:
true
false

Sample Output:
false
true
*/

import java.util.Scanner;

public class LogicalOperatorsExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean input1 = scanner.nextBoolean();
        boolean input2 = scanner.nextBoolean();

        boolean andResult = input1 && input2;
        boolean orResult = input1 || input2;

        System.out.println(andResult);
        System.out.println(orResult);

        scanner.close();
    }
}
