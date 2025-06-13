package management.perpustakaan;

import java.io.IOException;
import java.util.Optional;

import Models.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class BookController {

    // FXML Table and Columns
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> statusColumn;

    // FXML Form Elements
    @FXML private VBox formVBox; // VBox yang berisi form
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private Button saveButton;
    @FXML private Button addButton;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private Book selectedBookForEdit = null; // Untuk melacak buku yang sedang diedit

    @FXML
    public void initialize() {
        // Setup kolom tabel
        idColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("isBorrowed"));

        // Muat data buku ke dalam tabel
        refreshBookTable();

        // Sembunyikan form pada awalnya
        formVBox.setVisible(false);
        formVBox.managedProperty().bind(formVBox.visibleProperty());

        // Nonaktifkan tombol edit dan hapus sampai ada item yang dipilih
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        // Tambahkan listener ke tabel untuk mendeteksi item yang dipilih
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isItemSelected = newSelection != null;
            editButton.setDisable(!isItemSelected);
            deleteButton.setDisable(!isItemSelected);
            
            // Jika ada item yang dipilih, sembunyikan form dan reset mode edit
            if (isItemSelected) {
                formVBox.setVisible(false);
                selectedBookForEdit = null;
            }
        });
    }

    /**
     * Memuat ulang data dari App.library dan menampilkannya di tabel.
     */
    private void refreshBookTable() {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        for (var item : App.library.items) {
            if (item instanceof Book) {
                bookList.add((Book) item);
            }
        }
        bookTable.setItems(bookList);
        bookTable.refresh(); // Memastikan tabel di-render ulang
    }

    @FXML
    private void handleAddButton() {
        // Bersihkan seleksi tabel agar tidak membingungkan
        bookTable.getSelectionModel().clearSelection();
        
        // Kosongkan form dan siapkan untuk input baru
        selectedBookForEdit = null;
        titleField.clear();
        authorField.clear();
        saveButton.setText("Simpan Buku Baru");

        // Tampilkan form
        formVBox.setVisible(true);
    }

    @FXML
    private void handleEditButton() {
        // Ambil buku yang dipilih dari tabel
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Set buku yang akan diedit dan isi form dengan datanya
            selectedBookForEdit = selectedBook;
            titleField.setText(selectedBook.getTitle());
            authorField.setText(selectedBook.getAuthor());
            saveButton.setText("Simpan Perubahan");

            // Tampilkan form untuk mengedit
            formVBox.setVisible(true);
        }
    }

    @FXML
    private void handleSaveButton() {
        String title = titleField.getText();
        String author = authorField.getText();

        if (title.isEmpty() || author.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Judul dan Penulis tidak boleh kosong.");
            return;
        }

        if (selectedBookForEdit == null) {
            // Mode Tambah Buku Baru
            int maxId = 0;
            for (var item : App.library.items) {
                if (item.getItemId() > maxId) {
                    maxId = item.getItemId();
                }
            }
            int newId = maxId + 1;
            Book newBook = new Book(title, newId, false, author);
            App.library.items.add(newBook);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku baru berhasil ditambahkan.");
        } else {
            // Mode Edit Buku
            selectedBookForEdit.setTitle(title);
            selectedBookForEdit.setAuthor(author);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data buku berhasil diperbarui.");
        }
        
        // Reset form dan refresh tabel
        formVBox.setVisible(false);
        selectedBookForEdit = null;
        titleField.clear();
        authorField.clear();
        refreshBookTable();
    }

    @FXML
    private void handleDeleteButton() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            // Tampilkan dialog konfirmasi
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Konfirmasi Hapus");
            confirmation.setHeaderText("Anda yakin ingin menghapus buku ini?");
            confirmation.setContentText("Judul: " + selectedBook.getTitle());

            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Hapus buku dari list utama
                App.library.items.remove(selectedBook);
                refreshBookTable();
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil dihapus.");
            }
        }
    }

    @FXML
    private void handleBackButton() throws IOException {
        App.setRoot("HomeView");
    }
    
    /**
     * Helper untuk menampilkan dialog Alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
