package Models;

import java.util.ArrayList;
import java.util.List;

public class Member {
    public String name;
    public String memberId;
    public List<LibraryItem> borrowedItems;

    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
        this.borrowedItems = new ArrayList<>();
    }
    
    // Getter
    public String getName() { return name; }
    public String getMemberId() { return memberId; }

    public String borrow(LibraryItem item, int days) {
        if (item.isBorrowed) {
            throw new IllegalStateException("Item tidak tersedia untuk dipinjam.");
        }
        item.borrowItem(days);
        borrowedItems.add(item);
        return "Item " + item.title + " berhasil dipinjam selama " + days + " hari";
    }

    public String returnItem(LibraryItem item, int daysLate) {
        item.returnItem();
        borrowedItems.remove(item);
        double fine = item.calculateFine(daysLate);
        return "Item " + item.title + " berhasil dikembalikan dengan denda: Rp " + fine;
    }
}