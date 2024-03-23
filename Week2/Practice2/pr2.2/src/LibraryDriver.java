
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Абстрактний базовий клас, що представляє людину
class Human implements Serializable {
    private String firstName;
    private String lastName;

    public Human(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName;
    }
}

// Клас Author, що представляє автора книги
class Author extends Human {
    public Author(String firstName, String lastName) {
        super(firstName, lastName);
    }
}

// Клас Book, що представляє книгу
class Book implements Serializable {
    private String name;
    private List<Author> authors;
    private int publicationYear;
    private int editionNumber;

    public Book(String name, List<Author> authors, int publicationYear, int editionNumber) {
        this.name = name;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.editionNumber = editionNumber;
    }

    @Override
    public String toString() {
        return "Book: " + name + ", Authors: " + authors + ", Year: " + publicationYear + ", Edition: " + editionNumber;
    }
}

// Клас Library, що представляє бібліотеку
class Library implements Serializable {
    private String name;
    private List<Book> books;
    private List<BookReader> readers;

    public Library(String name) {
        this.name = name;
        this.books = new ArrayList<>();
        this.readers = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addReader(BookReader reader) {
        readers.add(reader);
    }

    @Override
    public String toString() {
        return "Library: " + name + "\nBooks: " + books + "\nReaders: " + readers;
    }
}

// Клас BookReader, що представляє читача
class BookReader extends Human {
    private int readerID;
    private List<Book> borrowedBooks;

    public BookReader(String firstName, String lastName, int readerID) {
        super(firstName, lastName);
        this.readerID = readerID;
        this.borrowedBooks = new ArrayList<>();
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    @Override
    public String toString() {
        return super.toString() + ", Reader ID: " + readerID + ", Borrowed Books: " + borrowedBooks;
    }
}

// Клас LibraryDriver для серіалізації та десеріалізації бібліотеки
public class LibraryDriver {
    public static void serializeLibrary(Library library, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(library);
            System.out.println("Library serialized successfully.");
        }
    }

    public static Library deserializeLibrary(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Library library = (Library) in.readObject();
            System.out.println("Library deserialized successfully.");
            return library;
        }
    }

    public static void main(String[] args) {
        // Створення об'єктів для тестування
        Author author1 = new Author("John", "Doe");
        Author author2 = new Author("Jane", "Smith");
        Book book1 = new Book("Java Programming", List.of(author1, author2), 2020, 1);
        Book book2 = new Book("Data Structures", List.of(author1), 2018, 2);
        BookReader reader1 = new BookReader("Alice", "Johnson", 1001);
        BookReader reader2 = new BookReader("Bob", "Smith", 1002);
        reader1.borrowBook(book1);
        reader2.borrowBook(book2);

        Library library = new Library("Central Library");
        library.addBook(book1);
        library.addBook(book2);
        library.addReader(reader1);
        library.addReader(reader2);

        try {
            // Серіалізація та десеріалізація бібліотеки
            serializeLibrary(library, "library.ser");
            Library deserializedLibrary = deserializeLibrary("library.ser");
            System.out.println(deserializedLibrary);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
