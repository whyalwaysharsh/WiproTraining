/*
Question 3: Book Class
----------------------
Create a Java class named Book to represent a book with the following features:

Instance Variables:
- title (String)
- author (String)
- year (int)

Constructors:
- Default constructor (sets default values)
- Parameterized constructor (sets values from arguments)

Methods:
- displayInfo() to print details of the book.

Application:
In main(), create two objects — one using the default constructor and one using the parameterized constructor.
*/

public class BookClass {
    String title;
    String author;
    int year;

    // Default Constructor
    BookClass() {
        title = "Not specified";
        author = "Unknown";
        year = 0;
    }

    // Parameterized Constructor
    BookClass(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Method to display book info
    public void displayInfo() {
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Year: " + year);
        System.out.println();
    }

    public static void main(String[] args) {
        BookClass book1 = new BookClass();  // Calls default constructor
        BookClass book2 = new BookClass("Java Programming", "Sakshi", 2024);  // Calls parameterized constructor

        System.out.println("Book 1:");
        book1.displayInfo();

        System.out.println("Book 2:");
        book2.displayInfo();
    }
}
