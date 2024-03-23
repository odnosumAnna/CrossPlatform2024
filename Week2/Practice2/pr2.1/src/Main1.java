
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Абстрактний базовий клас, що представляє людину
abstract class Human implements Serializable {
    private String firstName;
    private String lastName;

    public Human(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

// Клас, що представляє Автора книги
class Author extends Human {
    public Author(String firstName, String lastName) {
        super(firstName, lastName);
    }
}

// Клас, що представляє Читача книг
class BookReader extends Human {
    private String registrationNumber;
    private List<Book> borrowedBooks;

    public BookReader(String firstName, String lastName, String registrationNumber) {
        super(firstName, lastName);
        this.registrationNumber = registrationNumber;
        this.borrowedBooks = new ArrayList<>();
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void printReaderInfo() {
        System.out.println("BookReader: " + registrationNumber);
        System.out.println("Borrowed Books:");
        for (Book book : borrowedBooks) {
            System.out.println("- " + book);
        }
    }
}

// Клас, що представляє Книгу
class Book implements Serializable {
    private String title;
    private List<Author> authors;
    private int year;

    public Book(String title, List<Author> authors, int year) {
        this.title = title;
        this.authors = authors;
        this.year = year;
    }

    @Override
    public String toString() {
        return title + " (" + year + ") by " + authorsToString();
    }

    private String authorsToString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < authors.size(); i++) {
            stringBuilder.append(authors.get(i));
            if (i < authors.size() - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}

// Клас, що представляє Книгосховище
class BookStore implements Serializable {
    private String name;
    private List<Book> books;

    public BookStore(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void printStoreInfo() {
        System.out.println("BookStore: " + name);
        System.out.println("Books:");
        for (Book book : books) {
            System.out.println("- " + book);
        }
    }
}

// Клас, що представляє Бібліотеку
class Library implements Serializable {
    private String name;
    private List<BookStore> bookStores;
    private List<BookReader> readers;

    public Library(String name) {
        this.name = name;
        this.bookStores = new ArrayList<>();
        this.readers = new ArrayList<>();
    }

    public void addBookStore(BookStore bookStore) {
        bookStores.add(bookStore);
    }

    public void addReader(BookReader reader) {
        readers.add(reader);
    }

    public void printLibraryInfo() {
        System.out.println("Library: " + name);
        System.out.println("BookStores:");
        for (BookStore store : bookStores) {
            store.printStoreInfo();
        }
        System.out.println("Readers:");
        for (BookReader reader : readers) {
            reader.printReaderInfo();
        }
    }
}

// Клас, що представляє виконавця бібліотеки
class LibraryDriver {
    // Метод для серіалізації об'єкта у файл
    public static void serializeObject(String fileName, Serializable obj) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(obj);
            System.out.println("Serialization successful.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Метод для десеріалізації об'єкта з файлу
    public static Object deSerializeObject(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        // Створення бібліотеки
        Library library = new Library("My Library");

        // Створення книгосховища
        BookStore bookStore = new BookStore("Main BookStore");

        // Створення книг
        List<Author> authors1 = new ArrayList<>();
        authors1.add(new Author("John", "Doe"));
        List<Author> authors2 = new ArrayList<>();
        authors2.add(new Author("Jane", "Smith"));

        Book book1 = new Book("Book 1", authors1, 2000);
        Book book2 = new Book("Book 2", authors2, 2010);

        // Додавання книг до книгосховища
        bookStore.addBook(book1);
        bookStore.addBook(book2);

        // Створення читачів
        BookReader reader1 = new BookReader("Alice", "Johnson", "Reader 1");
        BookReader reader2 = new BookReader("Bob", "Smith", "Reader 2");

// Додавання читачів до бібліотеки
        library.addReader(reader1);
        library.addReader(reader2);

// Додавання книгосховища до бібліотеки
        library.addBookStore(bookStore);

// Виведення інформації про бібліотеку
        System.out.println("Initial library state:");
        library.printLibraryInfo();

// Серіалізація бібліотеки
        String fileName = "library.ser";
        serializeObject(fileName, library);

// Завантаження та виведення стану десеріалізованої бібліотеки
        Library deserializedLibrary = (Library) deSerializeObject(fileName);
        if (deserializedLibrary != null) {
            System.out.println("\nDeserialized library state:");
            deserializedLibrary.printLibraryInfo();
        }
    }}