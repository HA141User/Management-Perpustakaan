package management.perpustakaan;

import java.io.IOException;
import java.util.stream.Collectors;
import Models.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class BookController {
    // FXML fields...
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> statusColumn; // Ubah dari Boolean ke String
    @FXML private VBox formVBox;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField borrowMemberIdField;
    @FXML private TextField borrowBookIdField;
    @FXML private TextField returnBookIdField;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    private Book selectedBookForEdit = null;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        
        // Custom cell value factory untuk status yang user-friendly
        statusColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            String statusText = book.getIsBorrowed() ? "Dipinjam" : "Tersedia";
            return new javafx.beans.property.SimpleStringProperty(statusText);
        });

        // Optional: Tambahkan styling untuk status
        statusColumn.setCellFactory(column -> {
            return new TableCell<Book, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        // Warna berbeda untuk status yang berbeda
                        if ("Dipinjam".equals(item)) {
                            setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;"); // Merah
                        } else {
                            setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;"); // Hijau
                        }
                    }
                }
            };
        });

        refreshBookTable();
        formVBox.setVisible(false);
        formVBox.managedProperty().bind(formVBox.visibleProperty());
        editButton.setDisable(true);
        deleteButton.setDisable(true);

        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isItemSelected = newSelection != null;
            editButton.setDisable(!isItemSelected);
            deleteButton.setDisable(!isItemSelected);
            if (isItemSelected) {
                formVBox.setVisible(false);
                selectedBookForEdit = null;
            }
        });
    }

    private void refreshBookTable() {
        ObservableList<Book> bookList = FXCollections.observableArrayList(
                App.library.items.stream()
                        .filter(item -> item instanceof Book)
                        .map(item -> (Book) item)
                        .collect(Collectors.toList())
        );
        bookTable.setItems(bookList);
    }

    @FXML
    private void handleBorrowAction() {
        String memberId = borrowMemberIdField.getText();
        String bookIdText = borrowBookIdField.getText();

        // --- PERUBAHAN DI SINI: Tambah validasi input kosong ---
        if (memberId.trim().isEmpty() || bookIdText.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "ID Anggota dan ID Buku harus diisi.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdText);
            int duration = 14; // Menggunakan durasi default 14 hari
            
            App.library.processBorrowing(memberId, bookId, duration);

            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil dipinjamkan!");
            saveDataAndRefresh();
            borrowMemberIdField.clear();
            borrowBookIdField.clear();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "ID Buku harus berupa angka.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Proses Gagal", e.getMessage());
        }
    }
    
    @FXML
    private void handleReturnAction() {
        String bookIdText = returnBookIdField.getText();

        // --- PERUBAHAN DI SINI: Tambah validasi input kosong ---
        if (bookIdText.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "ID Buku untuk pengembalian harus diisi.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdText);
            App.library.processReturning(bookId);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil dikembalikan!");
            saveDataAndRefresh();
            returnBookIdField.clear();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "ID Buku harus berupa angka.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Proses Gagal", e.getMessage());
        }
    }

    @FXML
    private void handleSaveButton() {
        String title = titleField.getText();
        String author = authorField.getText();

        if (title.trim().isEmpty() || author.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "Judul dan Penulis tidak boleh kosong.");
            return;
        }

        if (selectedBookForEdit == null) {
            int newId = App.library.items.stream().mapToInt(item -> item.getItemId()).max().orElse(0) + 1;
            App.library.items.add(new Book(title, newId, false, author));
        } else {
            selectedBookForEdit.setTitle(title);
            selectedBookForEdit.setAuthor(author);
        }
        
        saveDataAndRefresh();
        formVBox.setVisible(false);
        titleField.clear();
        authorField.clear();
    }

    @FXML
    private void handleDeleteButton() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION, "Yakin hapus: " + selectedBook.getTitle() + "?", ButtonType.YES, ButtonType.NO);
            confirmation.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    App.library.items.remove(selectedBook);
                    saveDataAndRefresh();
                }
            });
        }
    }
    
    @FXML private void handleAddButton() { formVBox.setVisible(true); selectedBookForEdit = null; titleField.clear(); authorField.clear(); }
    @FXML private void handleEditButton() { Book book = bookTable.getSelectionModel().getSelectedItem(); if (book != null) { formVBox.setVisible(true); selectedBookForEdit = book; titleField.setText(book.getTitle()); authorField.setText(book.getAuthor()); } }
    @FXML private void handleBackButton() throws IOException { App.setRoot("HomeView"); }

    private void saveDataAndRefresh() {
        try {
            PersistenceService.saveData();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan", "Terjadi error saat menyimpan data.");
        }
        refreshBookTable();
    }
    
    private void showAlert(Alert.AlertType type, String title, String msg) { Alert a = new Alert(type, msg); a.setTitle(title); a.setHeaderText(null); a.showAndWait(); }
}