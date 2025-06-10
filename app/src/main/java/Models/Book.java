package Models;

public class Book extends LibraryItem {
    public String author;

    public Book(String title, int itemId, boolean isBorrowed, String author) {
        super(title, itemId, isBorrowed);
        this.author = author;
    }
    
    // Getter
    public String getAuthor() { return author; }

    @Override
    public String getDescription() {
        return "Buku: " + title + " oleh " + author + ", ID: " + itemId;
    }

    @Override
    public String borrowItem(int days) {
        if (isBorrowed) {
            throw new IllegalArgumentException("Item sudah dipinjam.");
        }
        if (days > 14) {
            throw new IllegalArgumentException("Maksimal peminjaman 14 hari.");
        }
        isBorrowed = true;
        return "Item " + title + " berhasil dipinjam selama " + days + " hari";
    }

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 10000; // Denda 10.000 per hari keterlambatan
    }
}