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
    
    // Deklarasi FXML
    @FXML private TableView<Book> bookTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> statusColumn;
    @FXML private VBox formVBox;
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField borrowMemberIdField;
    @FXML private TextField borrowBookIdField;
    @FXML private TextField returnBookIdField;
    @FXML private Button editButton;
    @FXML private Button deleteButton;

    @FXML private VBox borrowVBox; // Form pinjam
    @FXML private VBox returnVBox; // Form kembali


    private Book selectedBookForEdit = null;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        
        statusColumn.setCellValueFactory(cellData -> {
            boolean isBorrowed = cellData.getValue().getIsBorrowed();
            String statusText = isBorrowed ? "Dipinjam" : "Tersedia";
            return new javafx.beans.property.SimpleStringProperty(statusText);
        });

        statusColumn.setCellFactory(column -> new TableCell<Book, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Dipinjam".equals(item)) {
                        setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        refreshBookTable();
        // Sembunyikan semua form pada awalnya
        formVBox.setVisible(false);
        formVBox.managedProperty().bind(formVBox.visibleProperty());
        borrowVBox.setVisible(false);
        borrowVBox.managedProperty().bind(borrowVBox.visibleProperty());
        returnVBox.setVisible(false);
        returnVBox.managedProperty().bind(returnVBox.visibleProperty());



        deleteButton.setDisable(true);

        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isItemSelected = newSelection != null;
            editButton.setDisable(!isItemSelected);
            deleteButton.setDisable(!isItemSelected);
            if (isItemSelected) {
                hideAllForms();
                selectedBookForEdit = null;
            }
        });
    }

    private void refreshBookTable() {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        for (var item : App.library.items) {
            if (item instanceof Book) {
            bookList.add((Book) item);
            }
        }
        bookTable.setItems(bookList);
        bookTable.refresh();
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
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku baru berhasil ditambahkan.");
        } else {
            selectedBookForEdit.setTitle(title);
            selectedBookForEdit.setAuthor(author);
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Data buku berhasil diperbarui.");
        }
        
        saveDataAndRefresh();
        hideAllForms();
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
                    showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil dihapus.");
                }
            });
        }
    }
    
    @FXML
    private void handleBorrowAction() {
        String memberId = borrowMemberIdField.getText();
        String bookIdText = borrowBookIdField.getText();
        if (memberId.trim().isEmpty() || bookIdText.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "ID Anggota dan ID Buku harus diisi.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdText);
            int duration = 14;
            App.library.processBorrowing(memberId, bookId, duration);
            saveDataAndRefresh();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil dipinjamkan!");
            hideAllForms();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "ID Buku harus berupa angka.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Proses Gagal", e.getMessage());
        }
    }
    
    @FXML
    private void handleReturnAction() {
        String bookIdText = returnBookIdField.getText();
        if (bookIdText.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Lengkap", "ID Buku untuk pengembalian harus diisi.");
            return;
        }

        try {
            int bookId = Integer.parseInt(bookIdText);
            App.library.processReturning(bookId);
            saveDataAndRefresh();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku berhasil dikembalikan!");
            hideAllForms();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Tidak Valid", "ID Buku harus berupa angka.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Proses Gagal", e.getMessage());
        }
    }
    
    @FXML private void handleAddButton() { showFormVBox(); }
    @FXML private void handleEditButton() { Book book = bookTable.getSelectionModel().getSelectedItem(); if (book != null) { showFormVBox(book); } }
    @FXML private void handleBackButton() throws IOException { App.setRoot("HomeView"); }
    @FXML private void showBorrowForm() { hideAllForms(); borrowVBox.setVisible(true); borrowMemberIdField.clear(); borrowBookIdField.clear(); }
    @FXML private void showReturnForm() { hideAllForms(); returnVBox.setVisible(true); returnBookIdField.clear(); }
    @FXML private void hideBorrowForm() { borrowVBox.setVisible(false); }
    @FXML private void hideReturnForm() { returnVBox.setVisible(false); }
    
    private void showFormVBox() {
        hideAllForms();
        formVBox.setVisible(true);
        selectedBookForEdit = null;
        titleField.clear();
        authorField.clear();
    }
    
    private void showFormVBox(Book book) {
        hideAllForms();
        formVBox.setVisible(true);
        selectedBookForEdit = book;
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
    }

    private void hideAllForms() {
        formVBox.setVisible(false);
        borrowVBox.setVisible(false);
        returnVBox.setVisible(false);
    }

    private void saveDataAndRefresh() {
        try {
            PersistenceService.saveData();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal Menyimpan", "Terjadi error saat menyimpan data.");
        }
        refreshBookTable();
    }
    

    private void showAlert(Alert.AlertType type, String title, String msg) { 
        Alert a = new Alert(type, msg); 
        a.setTitle(title); 
        a.setHeaderText(null); 
        a.showAndWait(); 

    }
}