package management.perpustakaan;

import Models.Book;
import Models.Library;
import Models.Member;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    public static Library library = new Library();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("HomeView"), 640, 480);
        stage.setTitle("Sistem Manajemen Perpustakaan");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    private static Parent loadFXML(String fxml) throws IOException {
        String fxmlPath = "/management/perpustakaan/" + fxml + ".fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxmlPath));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        library.addItem(new Book("The Lord of the Rings", 101, false, "J.R.R. Tolkien"));
        library.addItem(new Book("Laskar Pelangi", 102, true, "Andrea Hirata"));
        library.members.add(new Member("Budi Santoso", "M001"));
        library.members.add(new Member("Citra Lestari", "M002"));
        
        launch();
    }
}