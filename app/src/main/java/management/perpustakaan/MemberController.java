package management.perpustakaan;

import Models.Member;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// <-- PASTIKAN IMPORT INI ADA -->
import javafx.scene.control.TableView;

public class MemberController implements Initializable {
    
    // Deklarasi FXML
    @FXML private TextField txtName;
    @FXML private TextField txtCustomId;
    @FXML private CheckBox chkUseCustomId;
    @FXML private TableView<Member> tblMembers;
    @FXML private TableColumn<Member, String> colMemberId;
    @FXML private TableColumn<Member, String> colName;
    @FXML private TableColumn<Member, Integer> colActivities;
    @FXML private ListView<String> listActivities;
    @FXML private Label lblTotalMembers;
    @FXML private Label lblSelectedMember;
    
    private Member selectedMember;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // <-- INI BARIS KUNCI UNTUK MENGGABUNGKAN RUANG KOSONG -->
        // Ini memerintahkan kolom-kolom untuk melebar mengisi seluruh tabel.
        tblMembers.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        // Kode yang sudah ada sebelumnya
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colActivities.setCellValueFactory(new PropertyValueFactory<>("activitiesCount"));
        
        refreshMemberTable();
        
        tblMembers.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            selectedMember = nv;
            updateSelectedMemberInfo();
        });
        
        txtCustomId.disableProperty().bind(chkUseCustomId.selectedProperty().not());
    }

    private void refreshMemberTable() {
        tblMembers.setItems(FXCollections.observableArrayList(App.library.members));
        updateUI();
    }
    
    @FXML
    private void handleAddMember() {
        try {
            String name = txtName.getText();
            if (name == null || name.trim().isEmpty()) {
                showAlert("Error", "Nama anggota tidak boleh kosong!", Alert.AlertType.ERROR);
                return;
            }

            Member newMember;
            if (chkUseCustomId.isSelected()) {
                String customId = txtCustomId.getText();
                if (customId == null || customId.trim().isEmpty()) {
                    showAlert("Error", "ID custom tidak boleh kosong!", Alert.AlertType.ERROR);
                    return;
                }
                
                // Check if ID already exists
                if (isIdExists(customId)) {
                    showAlert("Error", "ID sudah digunakan!", Alert.AlertType.ERROR);
                    return;
                }
                
                newMember = new Member(name, customId);
            } else {
                // Generate unique auto ID
                String autoId = generateUniqueAutoId();
                newMember = new Member(name, autoId);
            }
            
            App.library.members.add(newMember);
            saveDataAndRefresh();
            clearForm();
            showAlert("Success", "Anggota berhasil ditambahkan dengan ID: " + newMember.getMemberId(), Alert.AlertType.INFORMATION);
            
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleUpdateMember() {
        if (selectedMember == null) return;
        try {
            String newName = txtName.getText();
            if (newName == null || newName.trim().isEmpty()) {
                showAlert("Error", "Nama baru tidak boleh kosong!", Alert.AlertType.ERROR);
                return;
            }
            selectedMember.updateInfo(newName);
            saveDataAndRefresh();
        } catch (IllegalArgumentException e) {
            showAlert("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleDeleteMember() {
        if (selectedMember == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Yakin hapus: " + selectedMember.getName() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                App.library.members.remove(selectedMember);
                saveDataAndRefresh();
            }
        });
    }
    
    private void saveDataAndRefresh() {
        try {
            PersistenceService.saveData();
        } catch (IOException e) {
            showAlert("Gagal Menyimpan", e.getMessage(), Alert.AlertType.ERROR);
        }
        refreshMemberTable();
    }
    
    @FXML private void handleClearForm() { clearForm(); tblMembers.getSelectionModel().clearSelection(); }
    @FXML private void handleMemberDoubleClick() { if (selectedMember != null) txtName.setText(selectedMember.getName()); }
    @FXML private void handleBackButton() throws IOException { App.setRoot("HomeView"); }

    private void updateSelectedMemberInfo() {
        if (selectedMember != null) {
            lblSelectedMember.setText("Terpilih: " + selectedMember.getName());
            listActivities.setItems(FXCollections.observableArrayList(selectedMember.getActivities()));
        } else {
            lblSelectedMember.setText("Tidak ada anggota terpilih");
            listActivities.getItems().clear();
        }
    }

    private void clearForm() { 
        txtName.clear(); 
        txtCustomId.clear(); 
        chkUseCustomId.setSelected(false); 
    }
    
    private void updateUI() { 
        lblTotalMembers.setText("Total Anggota: " + App.library.members.size()); 
    }
    
    private void showAlert(String title, String msg, Alert.AlertType type) { 
        Alert a = new Alert(type, msg); 
        a.setTitle(title); 
        a.setHeaderText(null); 
        a.showAndWait(); 
    }
    
    /**
     * Check if ID already exists in the member list
     */
    private boolean isIdExists(String id) {
        return App.library.members.stream()
               .anyMatch(member -> member.getMemberId().equals(id));
    }
    
    /**
     * Generate unique auto ID with format M001, M002, M003, etc.
     * Ensures no duplicate IDs exist in the current member list
     */
    private String generateUniqueAutoId() {
        int nextNumber = 1;
        String candidateId;
        
        // Find the next available number
        do {
            candidateId = "M" + String.format("%03d", nextNumber);
            nextNumber++;
        } while (isIdExists(candidateId));
        
        return candidateId;
    }
}