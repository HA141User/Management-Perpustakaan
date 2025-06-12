package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

// ABSTRACTION - Abstract class sebagai base class
abstract class Person {
    protected String name;
    protected String id;
    
    // ENCAPSULATION - Protected constructor
    protected Person(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        this.name = name.trim();
    }
    
    // ABSTRACTION - Abstract method yang harus diimplementasi
    public abstract String getDisplayInfo();
    public abstract String getPersonType();
    
    // POLYMORPHISM - Method yang bisa di-override
    public String getFormattedInfo() {
        return getPersonType() + ": " + getDisplayInfo();
    }
    
    // ENCAPSULATION - Getter dan Setter dengan validation
    public String getName() { 
        return name; 
    }
    
    public String getId() { 
        return id; 
    }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        this.name = name.trim();
    }
}

// ABSTRACTION - Interface untuk contract
interface MemberOperations {
    void updateInfo(String newName);
    String getMemberDetails();
    boolean isValidMember();
}

// INHERITANCE - Member extends Person
// ABSTRACTION - Member implements MemberOperations
public class Member extends Person implements MemberOperations {
    private static int nextId = 1;
    
    private String memberId;
    private List<String> activities;
    private List<Book> borrowedBooks; // <-- TAMBAHKAN FIELD INI

    public Member(String name) {
        super(name);
        this.memberId = "M" + String.format("%03d", nextId++);
        this.id = this.memberId;
        this.activities = new ArrayList<>();
        this.borrowedBooks = new ArrayList<>(); // <-- TAMBAHKAN INISIALISASI
        this.activities.add("Member created: " + this.memberId);
    }
    
    public Member(String name, String customId) {
        super(name);
        if (customId == null || customId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID tidak boleh kosong!");
        }
        this.memberId = customId.trim();
        this.id = this.memberId;
        this.activities = new ArrayList<>();
        this.borrowedBooks = new ArrayList<>(); // <-- TAMBAHKAN INISIALISASI
        this.activities.add("Member created with custom ID: " + this.memberId);
    }
    
    // --- TAMBAHKAN TIGA METODE BARU INI ---
    public List<Book> getBorrowedBooks() {
        return this.borrowedBooks;
    }

    public void borrowBook(Book book) {
        this.borrowedBooks.add(book);
        this.addActivity("Meminjam buku: " + book.getTitle());
    }

    public void returnBook(Book book) {
        this.borrowedBooks.remove(book);
        this.addActivity("Mengembalikan buku: " + book.getTitle());
    }
    // ------------------------------------

    @Override
    public String getDisplayInfo() {
        return "ID: " + memberId + ", Nama: " + name;
    }
    
    @Override
    public String getPersonType() {
        return "ANGGOTA PERPUSTAKAAN";
    }
    
    @Override
    public String getFormattedInfo() {
        return "=== " + getPersonType() + " ===\n" + getDisplayInfo() + 
               "\nTotal Aktivitas: " + activities.size();
    }
    
    @Override
    public void updateInfo(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama baru tidak boleh kosong!");
        }
        String oldName = this.name;
        setName(newName);
        activities.add("Name updated from '" + oldName + "' to '" + this.name + "'");
    }
    
    @Override
    public String getMemberDetails() {
        StringBuilder details = new StringBuilder();
        details.append("DETAIL ANGGOTA PERPUSTAKAAN\n");
        details.append("==========================\n");
        details.append("ID Anggota: ").append(memberId).append("\n");
        details.append("Nama: ").append(name).append("\n");
        details.append("Status: ").append(isValidMember() ? "AKTIF" : "TIDAK AKTIF").append("\n");
        details.append("Buku Dipinjam: ").append(borrowedBooks.size()).append("\n"); // Info tambahan
        details.append("\nRiwayat Aktivitas:\n");
        details.append("==================\n");
        
        if (activities.isEmpty()) {
            details.append("  Belum ada aktivitas\n");
        } else {
            for (int i = 0; i < activities.size(); i++) {
                details.append("  ").append(i + 1).append(". ").append(activities.get(i)).append("\n");
            }
        }
        return details.toString();
    }
    
    @Override
    public boolean isValidMember() {
        return name != null && !name.trim().isEmpty() && 
               memberId != null && !memberId.trim().isEmpty();
    }
    
    public String getMemberId() { 
        return memberId; 
    }
    
    public List<String> getActivities() { 
        return new ArrayList<>(activities);
    }
    
    public int getActivitiesCount() {
        return activities.size();
    }
    
    public void setMemberId(String memberId) {
        if (memberId == null || memberId.trim().isEmpty()) {
            throw new IllegalArgumentException("Member ID tidak boleh kosong!");
        }
        String oldId = this.memberId;
        this.memberId = memberId.trim();
        this.id = this.memberId;
        activities.add("ID updated from '" + oldId + "' to '" + this.memberId + "'");
    }
    
    public void addActivity(String activity) {
        if (activity != null && !activity.trim().isEmpty()) {
            activities.add(activity.trim());
        }
    }
    
    // ... (sisa metode lainnya tidak perlu diubah) ...
    @Override
    public String toString() {
        return name + " (" + memberId + ")"; // Disederhanakan untuk ChoiceDialog
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return Objects.equals(memberId, member.memberId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(memberId);
    }
}