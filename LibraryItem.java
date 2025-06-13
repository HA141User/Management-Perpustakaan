package Models;

public abstract class LibraryItem {
    public String title;
    public int itemId;
    public boolean isBorrowed;

    public LibraryItem(String title, int itemId, boolean isBorrowed) {
        this.title = title;
        this.itemId = itemId;
        this.isBorrowed = isBorrowed;
    }
    
    // Getter methods
    public String getTitle() { return title; }
    public int getItemId() { return itemId; }
    public boolean getIsBorrowed() { return isBorrowed; }
    
    // Setter methods (BARU)
    public void setTitle(String title) { this.title = title; }
    public void setIsBorrowed(boolean isBorrowed) { this.isBorrowed = isBorrowed; }

    public abstract String getDescription();
    public abstract String borrowItem(int days);
    public abstract double calculateFine(int daysLate);

    public String returnItem() {
        this.isBorrowed = false;
        return title + " dikembalikan";
    }
}
