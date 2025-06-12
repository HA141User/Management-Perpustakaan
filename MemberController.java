package management.perpustakaan;

import Models.Member;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MemberController implements Initializable {
    
    // FXML Components
    @FXML private TextField txtName;
    @FXML private TextField txtCustomId;
    @FXML private CheckBox chkUseCustomId;
    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;
    @FXML private Button btnClear;
    @FXML private Button btnAddActivity;
    @FXML private TextField txtActivity;
    @FXML private TextArea txtDetails;
    @FXML private TableView<Member> tblMembers;
    @FXML private TableColumn<Member, String> colMemberId;
    @FXML private TableColumn<Member, String> colName;
    @FXML private TableColumn<Member, Integer> colActivities;
    @FXML private TableColumn<Member, Boolean> colValid;
    @FXML private ListView<String> listActivities;
    @FXML private Label lblTotalMembers;
    @FXML private Label lblSelectedMember;
    
    // Data Storage
    private ObservableList<Member> memberList;
    private Member selectedMember;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize data
        memberList = FXCollections.observableArrayList();
        
        // Setup table columns
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colActivities.setCellValueFactory(new PropertyValueFactory<>("activitiesCount"));
        colValid.setCellValueFactory(new PropertyValueFactory<>("validMember"));
        
        // Bind table data
        tblMembers.setItems(memberList);
        
        // Setup table selection listener
        tblMembers.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                selectedMember = newValue;
                updateSelectedMemberInfo();
            }
        );
        
        // Setup custom ID checkbox
        chkUseCustomId.selectedProperty().addListener(
            (observable, oldValue, newValue) -> {
                txtCustomId.setDisable(!newValue);
                if (!newValue) {
                    txtCustomId.clear();
                }
            }
        );
        
        // Initial state
        txtCustomId.setDisable(true);
        updateUI();
        
        // Sample data
        addSampleData();
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
                newMember = new Member(name);
            }
            
            memberList.add(newMember);
            clearForm();
            updateUI();
            showAlert("Success", "Anggota berhasil ditambahkan!", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleUpdateMember() {
        if (selectedMember == null) {
            showAlert("Warning", "Pilih anggota yang akan diupdate!", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            String newName = txtName.getText();
            if (newName == null || newName.trim().isEmpty()) {
                showAlert("Error", "Nama baru tidak boleh kosong!", Alert.AlertType.ERROR);
                return;
            }
            
            selectedMember.updateInfo(newName);
            tblMembers.refresh();
            updateSelectedMemberInfo();
            clearForm();
            showAlert("Success", "Data anggota berhasil diupdate!", Alert.AlertType.INFORMATION);
            
        } catch (Exception e) {
            showAlert("Error", "Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleDeleteMember() {
        if (selectedMember == null) {
            showAlert("Warning", "Pilih anggota yang akan dihapus!", Alert.AlertType.WARNING);
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi");
        alert.setHeaderText("Hapus Anggota");
        alert.setContentText("Yakin ingin menghapus anggota: " + selectedMember.getName() + "?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            memberList.remove(selectedMember);
            selectedMember = null;
            clearForm();
            updateUI();
            showAlert("Success", "Anggota berhasil dihapus!", Alert.AlertType.INFORMATION);
        }
    }
    
    @FXML
    private void handleClearForm() {
        clearForm();
        tblMembers.getSelectionModel().clearSelection();
        selectedMember = null;
        updateSelectedMemberInfo();
    }
    
    @FXML
    private void handleAddActivity() {
        if (selectedMember == null) {
            showAlert("Warning", "Pilih anggota terlebuh dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        String activity = txtActivity.getText();
        if (activity == null || activity.trim().isEmpty()) {
            showAlert("Error", "Aktivitas tidak boleh kosong!", Alert.AlertType.ERROR);
            return;
        }
        
        selectedMember.addActivity(activity);
        txtActivity.clear();
        tblMembers.refresh();
        updateSelectedMemberInfo();
        showAlert("Success", "Aktivitas berhasil ditambahkan!", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    private void handleMemberDoubleClick() {
        if (selectedMember != null) {
            txtName.setText(selectedMember.getName());
        }
    }

       @FXML
    private void handleBackButton() throws java.io.IOException {
        App.setRoot("HomeView");
    }


    private void updateSelectedMemberInfo() {
        if (selectedMember != null) {
            lblSelectedMember.setText("Selected: " + selectedMember.getName() + " (" + selectedMember.getMemberId() + ")");
            txtDetails.setText(selectedMember.getMemberDetails());
            
            // Update activities list
            ObservableList<String> activities = FXCollections.observableArrayList(selectedMember.getActivities());
            listActivities.setItems(activities);
        } else {
            lblSelectedMember.setText("No member selected");
            txtDetails.clear();
            listActivities.getItems().clear();
        }
    }
    
    private void updateUI() {
        lblTotalMembers.setText("Total Members: " + memberList.size());
    }
    
    private void clearForm() {
        txtName.clear();
        txtCustomId.clear();
        txtActivity.clear();
        chkUseCustomId.setSelected(false);
    }
    
    private boolean isIdExists(String id) {
        return memberList.stream().anyMatch(member -> member.getMemberId().equals(id));
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void addSampleData() {
        try {
            Member member1 = new Member("John Doe");
            member1.addActivity("Terdaftar di Perpustakaan");
            member1.addActivity("Buku yang dipinjam: Java Programming");
            
            Member member2 = new Member("Jane Smith");
            member2.addActivity("Terdaftar di Perpustakaan");
            member2.addActivity("Buku yang dipinjam: Python Basics");
            member2.addActivity("Buku yang dikembalikan: Python Basics");
            
            memberList.addAll(member1, member2);
            updateUI();
        } catch (Exception e) {
            System.err.println("Error adding sample data: " + e.getMessage());
        }
    }
}