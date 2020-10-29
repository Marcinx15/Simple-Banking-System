import java.util.Arrays;

class Book {
    String title;
    int yearOfPublishing;
    String[] authors;
    boolean completed;

    Book(String title, int yearOfPublishing, String[] authors) {
        this.title = title;
        this.yearOfPublishing = yearOfPublishing;
        this.authors = Arrays.copyOf(authors, authors.length);
    }


}