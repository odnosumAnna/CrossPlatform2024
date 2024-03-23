import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Human implements Externalizable {
    private String firstName;
    private String lastName;

    public Human() {}

    public Human(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        firstName = (String) in.readObject();
        lastName = (String) in.readObject();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(firstName);
        out.writeObject(lastName);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

class Author extends Human {
    public Author() {}

    public Author(String firstName, String lastName) {
        super(firstName, lastName);
    }
}

class Book implements Externalizable {
    private String title;
    private List<Author> authors;
    private int publicationYear;
    private int editionNumber;

    public Book() {
        authors = new ArrayList<>();
    }

    public Book(String title, List<Author> authors, int publicationYear, int editionNumber) {
        this.title = title;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.editionNumber = editionNumber;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        title = (String) in.readObject();
        authors = (List<Author>) in.readObject();
        publicationYear = in.readInt();
        editionNumber = in.readInt();
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(title);
        out.writeObject(authors);
        out.writeInt(publicationYear);
        out.writeInt(editionNumber);
    }

    @Override
    public String toString() {
        return title + " (Edition: " + editionNumber + ", " + publicationYear + ")";
    }
}

class BookStore implements Externalizable {
    private String name;
    private List<Book> books;

    public BookStore() {
        books = new ArrayList<>();
    }

    public BookStore(String name) {
        this.name = name;
        books = new ArrayList<>();
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            Book book = new Book();
            book.readExternal(in);
            books.add(book);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(books.size());
        for (Book book : books) {
            book.writeExternal(out);
        }
    }

    @Override
    public String toString() {
        return "BookStore: " + name + "\nBooks: " + books;
    }
}

class Library implements Externalizable {
    private String name;
    private List<BookStore> bookStores;
    private List<BookReader> readers;

    public Library() {
        bookStores = new ArrayList<>();
        readers = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BookStore> getBookStores() {
        return bookStores;
    }

    public void setBookStores(List<BookStore> bookStores) {
        this.bookStores = bookStores;
    }

    public List<BookReader> getReaders() {
        return readers;
    }

    public void setReaders(List<BookReader> readers) {
        this.readers = readers;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        name = (String) in.readObject();
        int storeCount = in.readInt();
        for (int i = 0; i < storeCount; i++) {
            BookStore store = new BookStore();
            store.readExternal(in);
            bookStores.add(store);
        }

        int readerCount = in.readInt();
        for (int i = 0; i < readerCount; i++) {
            BookReader reader = new BookReader();
            reader.readExternal(in);
            readers.add(reader);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(name);
        out.writeInt(bookStores.size());
        for (BookStore store : bookStores) {
            store.writeExternal(out);
        }
        out.writeInt(readers.size());
        for (BookReader reader : readers) {
            reader.writeExternal(out);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Library: ").append(name).append("\n");
        sb.append("BookStores:\n");
        for (BookStore store : bookStores) {
            sb.append(store.toString()).append("\n");
        }
        sb.append("Readers:\n");
        for (BookReader reader : readers) {
            sb.append(reader.toString()).append("\n");
        }
        return sb.toString();
    }
}

class BookReader extends Human implements Externalizable {
    private int registrationNumber;
    private List<Book> borrowedBooks;

    public BookReader() {
        borrowedBooks = new ArrayList<>();
    }

    public BookReader(String firstName, String lastName, int registrationNumber) {
        super(firstName, lastName);
        this.registrationNumber = registrationNumber;
        borrowedBooks = new ArrayList<>();
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        registrationNumber = in.readInt();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            Book book = new Book();
            book.readExternal(in);
            borrowedBooks.add(book);
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(registrationNumber);
        out.writeInt(borrowedBooks.size());
        for (Book book : borrowedBooks) {
            book.writeExternal(out);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " (Registration Number: " + registrationNumber + ", Borrowed Books: " + borrowedBooks + ")";
    }
}

public class LibraryDriver {
    public static void main(String[] args) {
        Library library = new Library();
        library.setName("City Library");

        // Adding bookstores
        BookStore scienceFictionStore = new BookStore("Science Fiction Store");
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("Douglas", "Adams"));
        List<Book> books = new ArrayList<>();
        books.add(new Book("The Hitchhiker's Guide to the Galaxy", authors, 1979, 1));
        scienceFictionStore.setBooks(books);
        library.getBookStores().add(scienceFictionStore);

        // Adding readers
        BookReader reader1 = new BookReader("John", "Doe", 1001);
        List<Book> borrowedBooks = new ArrayList<>();
        borrowedBooks.add(new Book("1984", List.of(new Author("George", "Orwell")), 1949, 1));
        reader1.setBorrowedBooks(borrowedBooks);
        library.getReaders().add(reader1);

        // Serializing library object
        serialize(library);

        // Deserializing library object
        Library deserializedLibrary = deserialize();
        System.out.println(deserializedLibrary);
    }

    private static void serialize(Library library) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("library.ser"))) {
            outputStream.writeObject(library);
            System.out.println("Library serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Library deserialize() {
        Library library = null;
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("library.ser"))) {
            library = (Library) inputStream.readObject();
            System.out.println("Library deserialized successfully.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return library;
    }
}

