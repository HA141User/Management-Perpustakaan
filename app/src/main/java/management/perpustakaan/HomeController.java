package management.perpustakaan;

import java.io.IOException;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    private void handleManageBooks() throws IOException {
        App.setRoot("BookView");
    }

    @FXML
    private void handleManageMembers() throws IOException {
        App.setRoot("MemberView");
    }
}