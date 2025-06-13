package Models;

public class Book extends LibraryItem {
    public String author;

    public Book(String title, int itemId, boolean isBorrowed, String author) {
        super(title, itemId, isBorrowed);
        this.author = author;
    }
    
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    @Override
    public String getDescription() {
        return "Buku: " + getTitle() + " oleh " + author + ", ID: " + getItemId();
    }

    @Override
    public String borrowItem(int days) {
        if (isBorrowed) { // Menggunakan boolean biasa
            throw new IllegalArgumentException("Item sudah dipinjam.");
        }
        if (days > 14) {
            throw new IllegalArgumentException("Maksimal peminjaman 14 hari.");
        }
        isBorrowed = true; // Langsung mengubah nilai
        return "Item " + getTitle() + " berhasil dipinjam selama " + days + " hari";
    }

    @Override
    public double calculateFine(int daysLate) {
        return daysLate * 10000;
    }
}