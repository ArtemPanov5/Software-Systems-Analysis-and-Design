package SSAD_Assingment4;

import java.util.*;

/**
 * The Assignment4_BookStore class represents a bookstore management system that uses several design patterns
 * to organize the interaction between books, users, and their behaviors.
 */
public class Assignment4_BookStore {

    /**
     * The nested static class Book represents a book with its title, author, and price.
     */
    static class Book {
        String title;
        String author;
        String price;

        /**
         * Constructor for the Book class.
         *
         * @param title  The title of the book.
         * @param author The author of the book.
         * @param price  The price of the book.
         */
        public Book(String title, String author, String price) {
            this.title = title;
            this.author = author;
            this.price = price;
        }

        /**
         * Method to update the price of the book.
         *
         * @param newPrice The new price of the book.
         */
        public void updatePrice(String newPrice) {
            this.price = newPrice;
        }
    }

    /**
     * The Subscriber interface defines the strategy for notifying about a book price update.
     * This is an example of the Strategy pattern.
     */
    interface Subscriber {
        /**
         * Method called when the price of a book is updated.
         *
         * @param book  The book whose price was updated.
         * @param price The new price of the book.
         */
        void priceWasUpdated(Book book, String price);
    }

    /**
     * The User interface extends the Subscriber interface and defines additional methods for reading and listening to a book.
     * This is an example of the Adapter pattern.
     */
    interface User extends Subscriber {
        /**
         * Method to notify the user about a book price update.
         *
         * @param title    The title of the book.
         * @param newPrice The new price of the book.
         */
        void update(String title, String newPrice);

        /**
         * Method for the user to read a book.
         *
         * @param book The book the user is reading.
         */
        void readBook(Book book);

        /**
         * Method for the user to listen to a book.
         *
         * @param book The book the user is listening to.
         */
        void listenBook(Book book);
    }

    /**
     * The StandardUser class represents a standard user who can read books.
     * This class implements the User interface and is part of the Strategy pattern.
     */
    static class StandardUser implements User {
        String username;

        /**
         * Constructor for the StandardUser class.
         *
         * @param username The username of the user.
         */
        public StandardUser(String username) {
            this.username = username;
        }

        /**
         * Implementation of the update method from the User interface.
         *
         * @param title    The title of the book.
         * @param newPrice The new price of the book.
         */
        public void update(String title, String newPrice) {
            System.out.println(username + " notified about price update for " + title + " to " + newPrice);
        }

        /**
         * Implementation of the readBook method from the User interface.
         *
         * @param book The book the user is reading.
         */
        public void readBook(Book book) {
            System.out.println(username + " reading " + book.title + " by " + book.author);
        }

        /**
         * Implementation of the listenBook method from the User interface.
         * Standard users do not have access to listen to books.
         *
         * @param book The book the user is listening to.
         */
        public void listenBook(Book book) {
            System.out.println("No access");
        }

        /**
         * Implementation of the priceWasUpdated method from the Subscriber interface.
         *
         * @param book  The book whose price was updated.
         * @param price The new price of the book.
         */
        public void priceWasUpdated(Book book, String price) {
            update(book.title, price);
        }
    }

    /**
     * The PremiumUser class represents a premium user who can read and listen to books.
     * This class also implements the User interface and is part of the Strategy pattern.
     */
    static class PremiumUser implements User {
        String username;

        /**
         * Constructor for the PremiumUser class.
         *
         * @param username The username of the user.
         */
        public PremiumUser(String username) {
            this.username = username;
        }

        /**
         * Implementation of the update method from the User interface.
         *
         * @param title    The title of the book.
         * @param newPrice The new price of the book.
         */
        public void update(String title, String newPrice) {
            System.out.println(username + " notified about price update for " + title + " to " + newPrice);
        }

        /**
         * Implementation of the readBook method from the User interface.
         *
         * @param book The book the user is reading.
         */
        public void readBook(Book book) {
            System.out.println(username + " reading " + book.title + " by " + book.author);
        }

        /**
         * Implementation of the listenBook method from the User interface.
         *
         * @param book The book the user is listening to.
         */
        public void listenBook(Book book) {
            System.out.println(username + " listening " + book.title + " by " + book.author);
        }

        /**
         * Implementation of the priceWasUpdated method from the Subscriber interface.
         *
         * @param book  The book whose price was updated.
         * @param price The new price of the book.
         */
        public void priceWasUpdated(Book book, String price) {
            update(book.title, price);
        }
    }

    /**
     * The UserFactory class represents a factory for creating users of different types.
     * This is an example of the Factory Method pattern.
     */
    static class UserFactory {
        /**
         * Method to create a user of a specific type.
         *
         * @param type     The type of the user ("standard" or "premium").
         * @param username The username of the user.
         * @return The created user or null if the type is not supported.
         */
        public User createUser(String type, String username) {
            if (type.equals("standard")) {
                return new StandardUser(username);
            } else if (type.equals("premium")) {
                return new PremiumUser(username);
            }
            return null;
        }
    }

    /**
     * The Bookstore class represents the bookstore that manages books and users.
     * This class uses the Observer pattern.
     */
    static class Bookstore {
        Map<String, Book> books = new HashMap<>();
        Map<String, User> users = new HashMap<>();
        LinkedHashSet<Subscriber> subscribedUsers = new LinkedHashSet<>();
        UserFactory userFactory = new UserFactory();

        /**
         * Method to create a book.
         *
         * @param title  The title of the book.
         * @param author The author of the book.
         * @param price  The price of the book.
         */
        public void createBook(String title, String author, String price) {
            if (books.containsKey(title)) {
                System.out.println("Book already exists");
            } else {
                books.put(title, new Book(title, author, price));
            }
        }

        /**
         * Method to create a user.
         *
         * @param type     The type of the user ("standard" or "premium").
         * @param username The username of the user.
         */
        public void createUser(String type, String username) {
            if (users.containsKey(username)) {
                System.out.println("User already exists");
            } else {
                users.put(username, userFactory.createUser(type, username));
            }
        }

        /**
         * Method to subscribe a user to price
         *
         * @param username The username of the user to subscribe.
         */
        public void subscribe(String username) {
            User user = users.get(username);
            if (user != null && !subscribedUsers.contains(user)) {
                subscribedUsers.add(user);
            } else {
                System.out.println("User already subscribed");
            }
        }

        /**
         * Method to unsubscribe a user from price updates.
         *
         * @param username The username of the user to unsubscribe.
         */
        public void unsubscribe(String username) {
            User user = users.get(username);
            if (user != null && subscribedUsers.contains(user)) {
                subscribedUsers.remove(user);
            } else {
                System.out.println("User is not subscribed");
            }
        }

        /**
         * Method to update the price of a book and notify all subscribed users.
         *
         * @param title    The title of the book.
         * @param newPrice The new price of the book.
         */
        public void updatePrice(String title, String newPrice) {
            if (!books.containsKey(title)) {
                return;
            }
            books.get(title).updatePrice(newPrice);
            for (Subscriber sub : subscribedUsers) {
                sub.priceWasUpdated(books.get(title), newPrice);
            }
        }

        /**
         * Method for a user to read a book.
         *
         * @param username The username of the user.
         * @param title    The title of the book.
         */
        public void readBook(String username, String title) {
            User user = users.get(username);
            Book book = books.get(title);
            if (user != null && book != null) {
                user.readBook(book);
            }
        }

        /**
         * Method for a user to listen to a book.
         *
         * @param username The username of the user.
         * @param title    The title of the book.
         */
        public void listenBook(String username, String title) {
            User user = users.get(username);
            Book book = books.get(title);
            if (user != null && book != null) {
                user.listenBook(book);
            }
        }
    }

    /**
     * The main method that runs the bookstore system.
     * Users can interact with the system by entering commands.
     *
     * @param args Command line arguments (not used in this program).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Bookstore bookstore = new Bookstore();
        while (true) {
            String command = scanner.next();
            if (command.equals("createBook")) {
                String title = scanner.next();
                String author = scanner.next();
                String price = scanner.next();
                bookstore.createBook(title, author, price);
            } else if (command.equals("createUser")) {
                String type = scanner.next();
                String username = scanner.next();
                bookstore.createUser(type, username);
            } else if (command.equals("subscribe")) {
                String username = scanner.next();
                bookstore.subscribe(username);
            } else if (command.equals("unsubscribe")) {
                String username = scanner.next();
                bookstore.unsubscribe(username);
            } else if (command.equals("updatePrice")) {
                String title = scanner.next();
                String newPrice = scanner.next();
                bookstore.updatePrice(title, newPrice);
            } else if (command.equals("readBook")) {
                String username = scanner.next();
                String title = scanner.next();
                bookstore.readBook(username, title);
            } else if (command.equals("listenBook")) {
                String username = scanner.next();
                String title = scanner.next();
                bookstore.listenBook(username, title);
            } else if (command.equals("end")) {
                break;
            }
        }
        scanner.close();
    }
}