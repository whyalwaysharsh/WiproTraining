/*
Question 2: Multidimensional Arrays in Java
-------------------------------------------
Write a program that takes the row & columns input from the user to populate a 2D array and then prints the array.

Sample Input:
2
3
1 2 3
4 5 6

Sample Output:
1 2 3
4 5 6
*/

import java.util.Scanner;

public class MultidimensionalArray {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int rows = sc.nextInt();
        int cols = sc.nextInt();

        int[][] matrix = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = sc.nextInt();
            }
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        sc.close();
    }
}
