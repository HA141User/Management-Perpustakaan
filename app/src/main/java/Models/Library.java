package Models;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class Library {
    public List<LibraryItem> items;
    public List<Member> members;
    public LibraryLogger logger;

    public Library() {
        items = new ArrayList<>();
        members = new ArrayList<>();
        logger = new LibraryLogger();
    }

    public String addItem(LibraryItem item) {
        items.add(item);
        return item.title + " berhasil ditambahkan";
    }

    public LibraryItem findItemById(int itemId) {
        for (LibraryItem item : items) {
            if (item.itemId == itemId) {
                return item;
            }
        }
        throw new NoSuchElementException("Item dengan ID " + itemId + " tidak ditemukan.");
    }

    public Member findMemberById(String memberId) {
        for (Member member : members) {
            if (member.getMemberId().equals(memberId)) {
                return member;
            }
        }
        throw new NoSuchElementException("Anggota dengan ID " + memberId + " tidak ditemukan.");
    }

    // --- METODE BARU YANG SEBELUMNYA HILANG ---
    public boolean isMemberIdExists(String id) {
        return members.stream().anyMatch(member -> member.getMemberId().equals(id));
    }
    // ------------------------------------

    public void processBorrowing(String memberId, int bookId, int duration) throws Exception {
        Member member = findMemberById(memberId);
        LibraryItem item = findItemById(bookId);

        if (!(item instanceof Book)) {
            throw new Exception("ID " + bookId + " bukan merupakan sebuah buku.");
        }
        Book book = (Book) item;

        if (!member.getBorrowedBooks().isEmpty()) {
            throw new Exception("Gagal: Anggota '" + member.getName() + "' sudah meminjam buku lain.");
        }

        if (book.getIsBorrowed()) {
            throw new Exception("Gagal: Buku '" + book.getTitle() + "' sedang dipinjam.");
        }

        book.borrowItem(duration);
        member.borrowBook(book);
        logger.logActivity("BUKU DIPINJAM: '" + book.getTitle() + "' oleh '" + member.getName() + "'.");
    }

    public void processReturning(int bookId) throws Exception {
        LibraryItem item = findItemById(bookId);

        if (!(item instanceof Book)) {
            throw new Exception("ID " + bookId + " bukan merupakan sebuah buku.");
        }
        Book book = (Book) item;

        if (!book.getIsBorrowed()) {
            throw new Exception("Gagal: Buku '" + book.getTitle() + "' tidak dalam status dipinjam.");
        }

        Member borrower = null;
        for (Member member : members) {
            if (member.getBorrowedBooks().contains(book)) {
                borrower = member;
                break;
            }
        }

        if (borrower == null) {
            book.returnItem();
            throw new Exception("Peringatan: Buku dikembalikan, tapi data peminjam tidak ditemukan.");
        }
        
        book.returnItem();
        borrower.returnBook(book);
        logger.logActivity("BUKU DIKEMBALIKAN: '" + book.getTitle() + "' oleh '" + borrower.getName() + "'.");
    }
}