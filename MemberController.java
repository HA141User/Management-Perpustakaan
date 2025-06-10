package management.perpustakaan;

import Models.Member;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MemberController {

    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, String> memberIdColumn;
    @FXML private TableColumn<Member, String> nameColumn;
    
    // Form untuk tambah member baru
    @FXML private TextField nameField;
    @FXML private Button addMemberButton;
    @FXML private Button refreshButton;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        // Setup kolom tabel
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        // Load data awal
        refreshMemberTable();
    }
    
    @FXML
    private void handleAddMember() {
        String name = nameField.getText().trim();
        
        if (name.isEmpty()) {
            showStatus("Nama tidak boleh kosong!", false);
            return;
        }
        
        try {
            // Buat member baru dengan auto-increment ID
            Member newMember = new Member(name);
            App.library.members.add(newMember);
            
            // Refresh tabel dan clear form
            refreshMemberTable();
            nameField.clear();
            showStatus("Member " + newMember.getMemberId() + " berhasil ditambahkan!", true);
            
        } catch (Exception e) {
            showStatus("Error menambah member: " + e.getMessage(), false);
        }
    }
    
    @FXML
    private void handleRefreshTable() {
        refreshMemberTable();
        showStatus("Tabel berhasil di-refresh", true);
    }
    
    private void refreshMemberTable() {
        ObservableList<Member> memberList = FXCollections.observableArrayList(App.library.members);
        memberTable.setItems(memberList);
    }
    
    private void showStatus(String message, boolean isSuccess) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setStyle(isSuccess ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleBackButton() throws IOException {
        App.setRoot("HomeView");
    }
}