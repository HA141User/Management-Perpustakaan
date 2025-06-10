package Models;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private static int nextId = 1; // Static untuk auto-increment ID
    
    public String name;
    public String memberId;
    public List<LibraryItem> borrowedItems; // Hanya ada 1 kali disini
    
    // Constructor otomatis generate ID berurutan
    public Member(String name) {
        this.name = name;
        this.memberId = "M" + String.format("%03d", nextId++); // M001, M002, M003...
        this.borrowedItems = new ArrayList<>();
    }
    
    // Constructor dengan ID manual (untuk backward compatibility)
    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
        this.borrowedItems = new ArrayList<>();
    }
    
    // Getter methods untuk JavaFX TableView
    public String getName() { return name; }
    public String getMemberId() { return memberId; }
    public List<LibraryItem> getBorrowedItems() { return borrowedItems; }

    // Method untuk meminjam buku
    public String borrow(LibraryItem item, int days) {
        if (item.isBorrowed) {
            throw new IllegalStateException("Item tidak tersedia untuk dipinjam.");
        }
        item.borrowItem(days);
        borrowedItems.add(item);
        return "Item " + item.title + " berhasil dipinjam selama " + days + " hari";
    }

    // Method untuk mengembalikan buku
    public String returnItem(LibraryItem item) {
        item.returnItem();
        borrowedItems.remove(item);
        return "Item " + item.title + " berhasil dikembalikan";
    }
    
    // Method untuk reset counter ID (untuk testing)
    public static void resetIdCounter() {
        nextId = 1;
    }
    
    @Override
    public String toString() {
        return "Member{" +
                "ID='" + memberId + '\'' +
                ", Nama='" + name + '\'' +
                ", Jumlah Pinjaman=" + borrowedItems.size() +
                '}';
    }
}