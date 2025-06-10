package management.perpustakaan;

import Models.Member;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class MemberController {

    @FXML private TableView<Member> memberTable;
    @FXML private TableColumn<Member, String> memberIdColumn;
    @FXML private TableColumn<Member, String> nameColumn;

    @FXML
    public void initialize() {
        memberIdColumn.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        ObservableList<Member> memberList = FXCollections.observableArrayList(App.library.members);
        memberTable.setItems(memberList);
    }

    @FXML
    private void handleBackButton() throws IOException {
        App.setRoot("HomeView");
    }
}