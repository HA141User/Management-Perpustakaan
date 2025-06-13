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

    // Di dalam class Library.java

// Metode ini untuk mencari Anggota berdasarkan ID String
public Member findMemberById(String memberId) {
    for (Member member : members) {
        if (member.getMemberId().equals(memberId)) {
            return member;
        }
    }
    throw new NoSuchElementException("Anggota dengan ID " + memberId + " tidak ditemukan.");
}

/**
 * Memproses peminjaman buku berdasarkan ID.
 * Aturan: Satu anggota hanya boleh pinjam satu buku.
 */
public void processBorrowing(String memberId, int bookId, int duration) throws Exception {
    Member member = findMemberById(memberId);
    LibraryItem item = findItemById(bookId);

    // Pastikan item yang ditemukan adalah Buku
    if (!(item instanceof Book)) {
        throw new Exception("ID " + bookId + " bukan merupakan sebuah buku.");
    }
    Book book = (Book) item;

    // VALIDASI SESUAI ATURAN ANDA
    // 1. Cek apakah anggota sudah meminjam buku lain
    if (!member.getBorrowedBooks().isEmpty()) {
        throw new Exception("Gagal: Anggota '" + member.getName() + "' sudah meminjam buku lain dan harus mengembalikannya terlebih dahulu.");
    }

    // 2. Cek apakah buku tersedia
    if (book.getIsBorrowed()) {
        throw new Exception("Gagal: Buku '" + book.getTitle() + "' sedang dipinjam.");
    }

    // Jika semua validasi lolos
    book.borrowItem(duration);
    member.borrowBook(book); // Metode ini sudah kita buat sebelumnya
    logger.logActivity("BUKU DIPINJAM: '" + book.getTitle() + "' oleh '" + member.getName() + "'.");
}

/**
 * Memproses pengembalian buku berdasarkan ID.
 */
public void processReturning(int bookId) throws Exception {
    LibraryItem item = findItemById(bookId);

    if (!(item instanceof Book)) {
        throw new Exception("ID " + bookId + " bukan merupakan sebuah buku.");
    }
    Book book = (Book) item;

    // Validasi apakah buku memang sedang dipinjam
    if (!book.getIsBorrowed()) {
        throw new Exception("Gagal: Buku '" + book.getTitle() + "' tidak dalam status dipinjam.");
    }

    // Cari anggota yang meminjam buku ini
    Member borrower = null;
    for (Member member : members) {
        if (member.getBorrowedBooks().contains(book)) {
            borrower = member;
            break;
        }
    }

    if (borrower == null) {
        // Ini seharusnya tidak terjadi jika data konsisten
        book.returnItem(); // Tetap kembalikan bukunya
        throw new Exception("Peringatan: Buku dikembalikan, tapi data peminjam tidak ditemukan.");
    }
    
    // Jika semua validasi lolos
    book.returnItem();
    borrower.returnBook(book); // Metode ini sudah kita buat sebelumnya
    logger.logActivity("BUKU DIKEMBALIKAN: '" + book.getTitle() + "' oleh '" + borrower.getName() + "'.");
}
}