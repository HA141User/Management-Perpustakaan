package management.perpustakaan;

import Models.Book;
import Models.Library;
import Models.LibraryItem;
import Models.Member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PersistenceService {
    private static final String BOOKS_FILE = "books.json";
    private static final String MEMBERS_FILE = "members.json";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void saveData() throws IOException {
        for (Member member : App.library.members) {
            member.getBorrowedBookIds().clear();
            for (Book book : member.getBorrowedBooks()) {
                member.getBorrowedBookIds().add(book.getItemId());
            }
        }
        try (FileWriter writer = new FileWriter(BOOKS_FILE)) {
            gson.toJson(App.library.items, writer);
        }
        try (FileWriter writer = new FileWriter(MEMBERS_FILE)) {
            gson.toJson(App.library.members, writer);
        }
    }

    public static Library loadData() {
        Library library = new Library();
        library.items = loadItemsFromFile(BOOKS_FILE);
        library.members = loadMembersFromFile(MEMBERS_FILE);

        for (Member member : library.members) {
            for (Integer bookId : member.getBorrowedBookIds()) {
                try {
                    LibraryItem item = library.findItemById(bookId);
                    if (item instanceof Book) {
                        member.getBorrowedBooks().add((Book) item);
                    }
                } catch (Exception e) {
                    System.err.println("Gagal menghubungkan buku dengan ID " + bookId + " ke anggota " + member.getName());
                }
            }
        }
        return library;
    }

    private static List<LibraryItem> loadItemsFromFile(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            Type bookListType = new TypeToken<ArrayList<Book>>(){}.getType();
            List<LibraryItem> items = gson.fromJson(reader, bookListType);
            return items != null ? items : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    
    private static List<Member> loadMembersFromFile(String filename) {
        try (FileReader reader = new FileReader(filename)) {
            Type memberListType = new TypeToken<ArrayList<Member>>(){}.getType();
            List<Member> members = gson.fromJson(reader, memberListType);
            return members != null ? members : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}