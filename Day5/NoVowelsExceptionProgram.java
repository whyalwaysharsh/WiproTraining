/*
Question 3: Exception Handling - Custom Exception
-------------------------------------------------
Create a method that takes a string as input and throws an exception 
if the string does not contain vowels.
*/

import java.util.Scanner;

public class NoVowelsExceptionProgram {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        System.out.println("Original string: " + text);

        try {
            checkVowels(text);
            System.out.println("String contains vowels");
        } catch (NoVowelsException e) {
            System.out.println(e.getMessage());
        }

        sc.close();
    }

    public static void checkVowels(String text) throws NoVowelsException {
        text = text.toLowerCase();
        if (!(text.contains("a") || text.contains("e") || text.contains("i") || text.contains("o") || text.contains("u"))) {
            throw new NoVowelsException("String does not contain any vowels");
        }
    }
}

class NoVowelsException extends Exception {
    public NoVowelsException(String message) {
        super(message);
    }
}
