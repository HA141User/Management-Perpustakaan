package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

abstract class Person {
    protected String name;
    protected String id;
    
    protected Person(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        this.name = name.trim();
    }
    
    public abstract String getDisplayInfo();
    public abstract String getPersonType();
    
    public String getFormattedInfo() {
        return getPersonType() + ": " + getDisplayInfo();
    }
    
    public String getName() { return name; }
    public String getId() { return id; }
    
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama tidak boleh kosong!");
        }
        this.name = name.trim();
    }
}

interface MemberOperations {
    void updateInfo(String newName);
    String getMemberDetails();
    boolean isValidMember();
}

public class Member extends Person implements MemberOperations {
    private static int nextId = 1;
    private String memberId;
    private List<String> activities;
    private transient List<Book> borrowedBooks; 
    private List<Integer> borrowedBookIds;

    public Member(String name) {
        super(name);
        this.memberId = "M" + String.format("%03d", nextId++);
        this.id = this.memberId;
        this.activities = new ArrayList<>();
        this.borrowedBooks = new ArrayList<>();
        this.borrowedBookIds = new ArrayList<>();
        this.activities.add("Anggota telah terdaftar: " + this.memberId);
    }
    
    public Member(String name, String customId) {
        super(name);
        if (customId == null || customId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID tidak boleh kosong!");
        }
        this.memberId = customId.trim();
        this.id = this.memberId;
        this.activities = new ArrayList<>();
        this.borrowedBooks = new ArrayList<>();
        this.borrowedBookIds = new ArrayList<>();
        this.activities.add("Anggota telah terdaftar dengan custom ID: " + this.memberId);
    }
    
    public void borrowBook(Book book) {
        if (!this.getBorrowedBooks().contains(book)) {
            this.getBorrowedBooks().add(book);
        }
        this.addActivity("Meminjam buku: " + book.getTitle());
    }

    public void returnBook(Book book) {
        this.getBorrowedBooks().remove(book);
        this.addActivity("Mengembalikan buku: " + book.getTitle());
    }
    
    public List<Book> getBorrowedBooks() {
        if (this.borrowedBooks == null) {
             this.borrowedBooks = new ArrayList<>();
        }
        return this.borrowedBooks;
    }

    public List<Integer> getBorrowedBookIds() {
        if (this.borrowedBookIds == null) {
            this.borrowedBookIds = new ArrayList<>();
        }
        return this.borrowedBookIds;
    }

    public void setBorrowedBookIds(List<Integer> ids) {
        this.borrowedBookIds = ids;
    }

    @Override
    public String getDisplayInfo() { return "ID: " + memberId + ", Nama: " + name; }
    
    @Override
    public String getPersonType() { return "ANGGOTA PERPUSTAKAAN"; }
    
    @Override
    public void updateInfo(String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama baru tidak boleh kosong!");
        }
        String oldName = this.name;
        setName(newName);
        activities.add("Nama telah diubah dari '" + oldName + "' menjadi '" + this.name + "'");
    }
    
    @Override
    public String getMemberDetails() {
        StringBuilder details = new StringBuilder();
        details.append("ID Anggota: ").append(memberId).append("\n");
        details.append("Nama: ").append(name).append("\n");
        details.append("Buku Dipinjam: ").append(getBorrowedBooks().size()).append("\n\n");
        details.append("Riwayat Aktivitas:\n");
        for (String activity : activities) {
            details.append("- ").append(activity).append("\n");
        }
        return details.toString();
    }
    
    @Override
    public boolean isValidMember() { return name != null && !name.trim().isEmpty() && memberId != null && !memberId.trim().isEmpty(); }
    
    public String getMemberId() { return memberId; }
    public List<String> getActivities() { return new ArrayList<>(activities); }
    public int getActivitiesCount() { return activities.size(); }
    
    public void addActivity(String activity) { if (activity != null && !activity.trim().isEmpty()) { activities.add(activity.trim()); } }
    @Override
    public String toString() { return name + " (" + memberId + ")"; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return Objects.equals(memberId, member.memberId);
    }
    
    @Override
    public int hashCode() { return Objects.hash(memberId); }
}