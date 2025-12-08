/*
Question 3: Relational Operators in Java
----------------------------------------
Write a program that takes two integers as input and compares them using relational operators.
Print 'true' if the first integer is greater than the second integer, and 'false' otherwise.

Sample Input:
5
3

Sample Output:
true
*/

import java.util.Scanner;

public class RelationalOperatorExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int num1 = scanner.nextInt();
        int num2 = scanner.nextInt();

        System.out.println(num1 > num2);

        scanner.close();
    }
}
