package Models;

import java.util.Objects;

public abstract class LibraryItem {
    public String title;
    public int itemId;
    public boolean isBorrowed; // Menggunakan boolean biasa

    public LibraryItem(String title, int itemId, boolean isBorrowed) {
        this.title = title;
        this.itemId = itemId;
        this.isBorrowed = isBorrowed;
    }
    
    // Getter dan Setter standar
    public String getTitle() { return title; }
    public int getItemId() { return itemId; }
    public boolean getIsBorrowed() { return isBorrowed; }
    public void setTitle(String title) { this.title = title; }
    public void setIsBorrowed(boolean isBorrowed) { this.isBorrowed = isBorrowed; }

    public abstract String getDescription();
    public abstract String borrowItem(int days);
    public abstract double calculateFine(int daysLate);

    public String returnItem() {
        this.isBorrowed = false;
        return title + " dikembalikan";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LibraryItem that = (LibraryItem) o;
        return itemId == that.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemId);
    }
}